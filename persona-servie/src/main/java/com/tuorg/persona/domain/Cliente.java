package com.tuorg.persona.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente implements Persistable<Long> {

    @Id
    @Column(name = "cliente_id")
    private Long clienteId;

    @OneToOne(optional = false,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "cliente_id")
    private Persona persona;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public Long getId() {
        return clienteId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}