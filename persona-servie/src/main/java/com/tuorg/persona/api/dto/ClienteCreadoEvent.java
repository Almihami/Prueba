
package com.tuorg.persona.api.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteCreadoEvent {
    private Long clienteId;
    private String nombre;
    private String identificacion;
}