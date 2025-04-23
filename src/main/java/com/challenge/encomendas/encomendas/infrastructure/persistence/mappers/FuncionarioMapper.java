package com.challenge.encomendas.encomendas.infrastructure.persistence.mappers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.FuncionarioResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.FuncionarioEntity;

public class FuncionarioMapper {
    public static FuncionarioEntity toEntity(Funcionario funcionario) {
        FuncionarioEntity entity = new FuncionarioEntity();
        entity.setId(funcionario.getId()); // Ou outro getter
        entity.setNome(funcionario.getNome());
        entity.setEmail(funcionario.getEmail()); // Alterando de usuario para email
        entity.setSenha(funcionario.getSenha());
        return entity;
    }

    public static Funcionario toDomain(FuncionarioEntity entity) {
        return new Funcionario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(), // Alterando de usuario para email
                entity.getSenha()
        );
    }
    public static FuncionarioResponseDTO toResponseDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail()
        );
    }

}
