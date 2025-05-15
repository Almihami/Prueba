package com.tuorg.persona.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void whenBuildCliente_thenFieldsAreSet() {
        Persona persona = Persona.builder()
                .id(42L)
                .nombre("Juan Pérez")
                .genero("M")
                .edad(30)
                .identificacion("ABC123")
                .direccion("Calle Falsa 123")
                .telefono("555-0123")
                .build();

        Cliente cliente = Cliente.builder()
                .clienteId(persona.getId())      // <-- esto faltaba
                .persona(persona)
                .contrasena("passSecreta")
                .estado(true)
                .build();

        assertEquals(42L, cliente.getClienteId());
        assertSame(persona, cliente.getPersona());
        assertEquals("passSecreta", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }
}