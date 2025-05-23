package com.challenge.encomendas.encomendas.usecase.cadastro;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.CadastroFuncionarioDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.UpdateFuncionarioDTO;
import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.domain.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    private final FuncionarioGateway funcionarioGateway;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioGateway funcionarioGateway, PasswordEncoder passwordEncoder) {
        this.funcionarioGateway = funcionarioGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public Funcionario cadastrar(CadastroFuncionarioDTO dto) {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome(dto.nome());
        novoFuncionario.setEmail(dto.email());
        novoFuncionario.setSenha(passwordEncoder.encode(dto.senha()));
        novoFuncionario.adicionarRole(dto.role());

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
    public List<Funcionario> buscarTodos() {
        return funcionarioGateway.findAll();
    }

    public void deletarFuncionario(Long id) {
        // Opcional: Verificar se o funcionário existe antes de deletar
        if (funcionarioGateway.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário com o ID " + id + " não encontrado.");
        }
        funcionarioGateway.deleteById(id);
    }

    public Funcionario atualizar(Long id, UpdateFuncionarioDTO dto) {
        Funcionario funcionario = funcionarioGateway.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            funcionario.setNome(dto.nome());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            funcionario.setEmail(dto.email());
        }

        return funcionarioGateway.save(funcionario);
    }


}
