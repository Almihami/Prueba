package com.tuorg.persona.infrastructure;

import com.tuorg.persona.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}