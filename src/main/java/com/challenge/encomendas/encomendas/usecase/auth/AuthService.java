package com.challenge.encomendas.encomendas.usecase.auth;

import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final FuncionarioGateway funcionarioGateway;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(FuncionarioGateway funcionarioGateway, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.funcionarioGateway = funcionarioGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String senha) {
        Optional<Funcionario> funcionarioOptional = funcionarioGateway.findByEmail(email);

        if (funcionarioOptional.isEmpty()) {
            throw new BadCredentialsException("Usuário não encontrado");
        }

        Funcionario funcionario = funcionarioOptional.get();

        if (!passwordEncoder.matches(senha, funcionario.getSenha())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        return jwtUtil.generateToken(funcionario.getEmail());
    }
}
