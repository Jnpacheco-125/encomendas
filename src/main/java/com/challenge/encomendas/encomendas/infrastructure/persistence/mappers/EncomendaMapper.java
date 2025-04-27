package com.challenge.encomendas.encomendas.infrastructure.persistence.mappers;

import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.EncomendaEntity;

public class EncomendaMapper {

    public static EncomendaEntity toEntity(Encomenda encomenda) {
        if (encomenda == null) return null;

        return new EncomendaEntity(
                null, // O ID deve ser gerado pelo banco
                encomenda.getNomeDestinatario(),
                encomenda.getApartamento(),
                encomenda.getDescricao(),
                encomenda.getDataRecebimento(),
                encomenda.getRetirada(),
                encomenda.getDataRetirada(),
                FuncionarioMapper.toEntity(encomenda.getFuncionarioRecebimento()), // Conversão adequada
                MoradorMapper.toEntity(encomenda.getMoradorDestinatario()) // Conversão adequada
        );
    }

    public static Encomenda toDomain(EncomendaEntity entity) {
        if (entity == null) return null;

        return new Encomenda(
                entity.getId(),
                entity.getNomeDestinatario(),
                entity.getApartamento(),
                entity.getDescricao(),
                entity.getDataRecebimento(),
                entity.getRetirada(),
                entity.getDataRetirada(),
                FuncionarioMapper.toDomain(entity.getFuncionarioRecebimento()), // Conversão adequada
                MoradorMapper.toDomain(entity.getMoradorDestinatario()) // Conversão adequada
        );
    }
}

