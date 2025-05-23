package com.challenge.encomendas.encomendas.adapters.gateways;

import com.challenge.encomendas.encomendas.domain.entities.Funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioGateway {
    Funcionario save(Funcionario funcionario);
    Optional<Funcionario> findById(Long id);
    Optional<Funcionario> findByEmail(String email);
    List<Funcionario> findAll(); // Se precisar listar todos
    void deleteById(Long id);
}
