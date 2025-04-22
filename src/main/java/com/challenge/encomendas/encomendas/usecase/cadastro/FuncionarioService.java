package com.challenge.encomendas.encomendas.usecase.cadastro;

import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class FuncionarioService {
    private final FuncionarioGateway funcionarioGateway;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioGateway funcionarioGateway, PasswordEncoder passwordEncoder) {
        this.funcionarioGateway = funcionarioGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public Funcionario cadastrar(String nome, String email, String senha) {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome(nome);
        novoFuncionario.setEmail(email);
        novoFuncionario.setSenha(passwordEncoder.encode(senha)); // Codifica a senha antes de salvar
        return funcionarioGateway.save(novoFuncionario);
    }
    public Funcionario buscarPorId(Long id) {
        Optional<Funcionario> funcionario = funcionarioGateway.findById(id);
        return funcionario.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }
    public Funcionario buscarPorEmail(String email) {
        Optional<Funcionario> funcionario = funcionarioGateway.findByEmail(email);
        return funcionario.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }
}
