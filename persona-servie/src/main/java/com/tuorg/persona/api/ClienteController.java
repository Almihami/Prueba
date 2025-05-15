package com.tuorg.persona.api;

import com.tuorg.persona.api.dto.*;
import com.tuorg.persona.application.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService svc;

    public ClienteController(ClienteService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> list() {
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody CreateClienteRequest rq) {
        return ResponseEntity.status(201).body(svc.create(rq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id,
                                             @RequestBody UpdateClienteRequest rq) {
        return ResponseEntity.ok(svc.update(id, rq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}