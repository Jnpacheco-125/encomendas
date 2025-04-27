package com.challenge.encomendas.encomendas.usecase.cadastro;

import com.challenge.encomendas.encomendas.adapters.gateways.EncomendaGateway;
import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.EncomendaEntity;
import com.challenge.encomendas.encomendas.infrastructure.persistence.mappers.EncomendaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncomendaService {

    private final EncomendaGateway encomendaGateway;
    private final FuncionarioGateway funcionarioGateway;
    private final MoradorGateway moradorGateway;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EncomendaService(EncomendaGateway encomendaGateway,
                            FuncionarioGateway funcionarioGateway,
                            MoradorGateway moradorGateway,
                            PasswordEncoder passwordEncoder) {
        this.encomendaGateway = encomendaGateway;
        this.funcionarioGateway = funcionarioGateway;
        this.moradorGateway = moradorGateway;
        this.passwordEncoder = passwordEncoder;
    }


    public Encomenda cadastrarEncomenda(Encomenda encomenda) {
        if (encomenda.getNomeDestinatario() == null || encomenda.getApartamento() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do destinatário e apartamento são obrigatórios.");
        }
        encomenda.setDataRecebimento(LocalDateTime.now());
        encomenda.setRetirada(false); // Inicialmente, a encomenda não está retirada

        // Exemplo de uso do PasswordEncoder (se necessário)
         //encomenda.setAlgumCampo(passwordEncoder.encode(encomenda.getAlgumCampo()));

        return encomendaGateway.save(encomenda);
    }

    public Encomenda buscarPorId(Long id) {
        return encomendaGateway.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encomenda não encontrada."));
    }

    public List<Encomenda> buscarEncomendasPendentes() {
        return encomendaGateway.findAllByRetiradaFalse(); // Removendo .map(EncomendaMapper::toDomain)
    }

    public List<Encomenda> buscarEncomendasPorMorador(Long moradorId) {
        return encomendaGateway.findByMoradorDestinatarioId(moradorId);
    }

    public void deletarEncomenda(Long id) {
        if (encomendaGateway.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encomenda com o ID " + id + " não encontrada.");
        }
        encomendaGateway.deleteById(id);
    }

    public Encomenda confirmarRetirada(Long id) {
        Encomenda encomenda = encomendaGateway.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encomenda não encontrada."));

        encomenda.setRetirada(true);
        encomenda.setDataRetirada(LocalDateTime.now());

        return encomendaGateway.save(encomenda);
    }
}


