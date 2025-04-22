package com.challenge.encomendas.encomendas.infrastructure.persistence.repositories;

import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioJpaRepository extends JpaRepository<FuncionarioEntity, Long> {
    // por exemplo:
    // Optional<FuncionarioEntity> findByUsuario(String usuario);
    Optional<FuncionarioEntity> findByEmail(String email); // ✅ aqui
    Optional<FuncionarioEntity> findById(Long id);
}
