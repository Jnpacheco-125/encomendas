package com.challenge.encomendas.encomendas.adapters.gateways.impl;

import com.challenge.encomendas.encomendas.adapters.gateways.EncomendaGateway;
import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.EncomendaEntity;
import com.challenge.encomendas.encomendas.infrastructure.persistence.mappers.EncomendaMapper;


import com.challenge.encomendas.encomendas.infrastructure.persistence.repositories.EncomendaJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EncomendaGatewayImpl implements EncomendaGateway {

    private final EncomendaJpaRepository encomendaJpaRepository;

    @Autowired
    public EncomendaGatewayImpl(EncomendaJpaRepository encomendaJpaRepository) {
        this.encomendaJpaRepository = encomendaJpaRepository;
    }

    @Override
    public Encomenda save(Encomenda encomenda) {
        EncomendaEntity entity = EncomendaMapper.toEntity(encomenda);
        entity = encomendaJpaRepository.save(entity);
        return EncomendaMapper.toDomain(entity);
    }

    @Override
    public Optional<Encomenda> findById(Long id) {
        return encomendaJpaRepository.findById(id)
                .map(EncomendaMapper::toDomain);
    }

    @Override
    public List<Encomenda> findAllByRetiradaFalse() {
        return encomendaJpaRepository.findByRetiradaFalse()
                .stream()
                .map(EncomendaMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Encomenda> findAllByRetiradaTrue() {
        return encomendaJpaRepository.findByRetiradaTrue()
                .stream()
                .map(EncomendaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Encomenda> findByMoradorDestinatarioId(Long moradorId) {
        return encomendaJpaRepository.findByMoradorDestinatarioId(moradorId)
                .stream()
                .map(EncomendaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        encomendaJpaRepository.deleteById(id);
    }
}

