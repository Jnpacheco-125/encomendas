package com.challenge.encomendas.encomendas.infrastructure.persistence.mappers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.MoradorResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.MoradorEntity;
import org.springframework.stereotype.Component;

@Component
public class MoradorMapper {
    public static MoradorEntity toEntity(Morador morador) {
        MoradorEntity entity = new MoradorEntity();
        entity.setId(morador.getId());
        entity.setNome(morador.getNome());
        entity.setTelefone(morador.getTelefone());
        entity.setApartamento(morador.getApartamento());
        entity.setEmail(morador.getEmail());
        entity.setSenha(morador.getSenha());
        return entity;
    }

    public static Morador toDomain(MoradorEntity entity) {
        return new Morador(
                entity.getId(),
                entity.getNome(),
                entity.getTelefone(),
                entity.getApartamento(),
                entity.getEmail(),
                entity.getSenha()
        );
    }

    public static MoradorResponseDTO toResponseDTO(Morador morador) {
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getTelefone(),
                morador.getApartamento(),
                morador.getEmail()
        );
    }
}
