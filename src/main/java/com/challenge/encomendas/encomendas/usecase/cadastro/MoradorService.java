package com.challenge.encomendas.encomendas.usecase.cadastro;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.CadastroMoradorDTO;
import com.challenge.encomendas.encomendas.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.domain.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MoradorService {
    private final MoradorGateway moradorGateway;
    private final PasswordEncoder passwordEncoder;

    public MoradorService(MoradorGateway moradorGateway, PasswordEncoder passwordEncoder) {
        this.moradorGateway = moradorGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public Morador cadastrar(CadastroMoradorDTO dto) {
        Morador novoMorador = new Morador();
        novoMorador.setNome(dto.nome());
        novoMorador.setEmail(dto.email());
        novoMorador.setTelefone(dto.telefone());        // <-- estava faltando
        novoMorador.setApartamento(dto.apartamento());  // <-- estava faltando
        novoMorador.setSenha(passwordEncoder.encode(dto.senha()));
        novoMorador.adicionarRole(Role.ROLE_MORADOR);

        return moradorGateway.save(novoMorador);
    }

    public Morador buscarPorId(Long id) {
        Optional<Morador> morador = moradorGateway.findById(id);
        return morador.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Morador não encontrado"));
    }

    public Morador buscarPorEmail(String email) {
        Optional<Morador> morador = moradorGateway.findByEmail(email);
        return morador.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Morador não encontrado"));
    }

    public Morador buscarPorTelefone(String telefone) {
        Optional<Morador> morador = moradorGateway.findByTelefone(telefone);
        return morador.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Morador não encontrado"));
    }

    public Morador buscarPorApartamento(String apartamento) {
        Optional<Morador> morador = moradorGateway.findByApartamento(apartamento);
        return morador.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Morador não encontrado"));
    }

    public List<Morador> buscarTodos() {
        return moradorGateway.findAll();
    }

    public void deletarMorador(Long id) {
        // Opcional: Verificar se o morador existe antes de deletar
        if (moradorGateway.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Morador com o ID " + id + " não encontrado.");
        }
        moradorGateway.deleteById(id);
    }

    public Morador atualizarMorador(Long id, String nome, String telefone, String apartamento, String email, String novaSenha) {
        Morador moradorExistente = buscarPorId(id); // Garante que o morador existe

        if (nome != null) {
            moradorExistente.setNome(nome);
        }
        if (telefone != null) {
            moradorExistente.setTelefone(telefone);
        }
        if (apartamento != null) {
            moradorExistente.setApartamento(apartamento);
        }
        if (email != null) {
            // Opcional: Adicionar lógica para verificar se o novo email já existe
            moradorExistente.setEmail(email);
        }
        if (novaSenha != null && !novaSenha.isEmpty()) {
            moradorExistente.setSenha(passwordEncoder.encode(novaSenha));
        }

        return moradorGateway.save(moradorExistente);
    }
}
