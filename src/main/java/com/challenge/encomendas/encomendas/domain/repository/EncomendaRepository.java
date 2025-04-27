package com.challenge.encomendas.encomendas.domain.repository;

import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EncomendaRepository {

    @Transactional
    Encomenda save(Encomenda encomenda);

    Optional<Encomenda> findById(Long id);

    // Buscar todas as encomendas pendentes (ainda não retiradas)
    @Query("SELECT e FROM Encomenda e WHERE e.retirada = false")
    List<Encomenda> findAllByRetiradaFalse();

    // Buscar encomendas por ID do morador destinatário
    List<Encomenda> findByMoradorDestinatarioId(Long moradorId);

    @Transactional
    void deleteById(Long id);
}
