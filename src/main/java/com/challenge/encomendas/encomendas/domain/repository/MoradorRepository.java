package com.challenge.encomendas.encomendas.domain.repository;

import com.challenge.encomendas.encomendas.domain.entities.Morador;

import java.util.Optional;

public interface MoradorRepository {
    Optional<Morador> findById(Long id);
    Optional<Morador> findByEmail(String email);
    Optional<Morador> findByTelefone(String telefone);
    Optional<Morador> findByApartamento(String apartamento);
    Morador save(Morador morador);
    void deleteById(Long id);
}
