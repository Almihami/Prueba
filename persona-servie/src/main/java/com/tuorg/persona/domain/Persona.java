package com.tuorg.persona.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String genero;
    private Integer edad;

    @Column(unique = true)
    private String identificacion;

    private String direccion;
    private String telefono;
}