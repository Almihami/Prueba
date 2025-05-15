package com.tuorg.persona.application;

import com.tuorg.persona.api.dto.ClienteCreadoEvent;
import com.tuorg.persona.api.dto.ClienteDTO;
import com.tuorg.persona.api.dto.CreateClienteRequest;
import com.tuorg.persona.api.dto.UpdateClienteRequest;
import com.tuorg.persona.domain.Cliente;
import com.tuorg.persona.domain.Persona;
import com.tuorg.persona.infrastructure.ClienteRepository;
import com.tuorg.persona.infrastructure.PersonaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    private final PersonaRepository personaRepo;
    private final ClienteRepository clienteRepo;
    private final RabbitTemplate rabbit;

    @PersistenceContext
    private EntityManager em;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    public ClienteService(PersonaRepository personaRepo,
                          ClienteRepository clienteRepo,
                          RabbitTemplate rabbit) {
        this.personaRepo = personaRepo;
        this.clienteRepo = clienteRepo;
        this.rabbit      = rabbit;
    }

    public List<ClienteDTO> findAll() {
        return clienteRepo.findAll()
                .stream()
                .map(ClienteService::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO findById(Long id) {
        return clienteRepo.findById(id)
                .map(ClienteService::toDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public ClienteDTO create(CreateClienteRequest rq) {
        Persona p = personaRepo.save(
                Persona.builder()
                        .nombre(rq.getNombre())
                        .genero(rq.getGenero())
                        .edad(rq.getEdad())
                        .identificacion(rq.getIdentificacion())
                        .direccion(rq.getDireccion())
                        .telefono(rq.getTelefono())
                        .build()
        );

        Cliente c = clienteRepo.save(
                Cliente.builder()
                        .clienteId(p.getId())
                        .persona(p)
                        .contrasena(rq.getContrasena())
                        .estado(rq.getEstado())
                        .build()
        );

        ClienteCreadoEvent evt = ClienteCreadoEvent.builder()
                .clienteId(c.getClienteId())
                .nombre(p.getNombre())
                .identificacion(p.getIdentificacion())
                .build();
        rabbit.convertAndSend(exchange, "", evt);

        return toDTO(c);
    }

    public ClienteDTO update(Long id, UpdateClienteRequest rq) {
        Cliente c = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Persona p = c.getPersona();
        p.setNombre(rq.getNombre());
        p.setGenero(rq.getGenero());
        p.setEdad(rq.getEdad());
        p.setIdentificacion(rq.getIdentificacion());
        p.setDireccion(rq.getDireccion());
        p.setTelefono(rq.getTelefono());

        c.setContrasena(rq.getContrasena());
        c.setEstado(rq.getEstado());

        // JPA hace flush automático
        return toDTO(c);
    }


    public void delete(Long id) {
        int borrados = em.createQuery(
                        "DELETE FROM Cliente c WHERE c.clienteId = :id")
                .setParameter("id", id)
                .executeUpdate();

        if (borrados == 0) {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    private static ClienteDTO toDTO(Cliente c) {
        Persona p = c.getPersona();
        return ClienteDTO.builder()
                .clienteId(c.getClienteId())
                .nombre(p.getNombre())
                .genero(p.getGenero())
                .edad(p.getEdad())
                .identificacion(p.getIdentificacion())
                .direccion(p.getDireccion())
                .telefono(p.getTelefono())
                .contrasena(c.getContrasena())
                .estado(c.getEstado())
                .build();
    }
}