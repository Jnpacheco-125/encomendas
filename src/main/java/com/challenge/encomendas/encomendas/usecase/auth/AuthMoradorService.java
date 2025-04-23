package com.challenge.encomendas.encomendas.usecase.auth;

import com.challenge.encomendas.encomendas.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthMoradorService {
    private final MoradorGateway moradorGateway;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthMoradorService(MoradorGateway moradorGateway, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.moradorGateway = moradorGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String senha) {
        Optional<Morador> moradorOptional = moradorGateway.findByEmail(email);

        if (moradorOptional.isEmpty()) {
            throw new BadCredentialsException("Morador não encontrado");
        }

        Morador morador = moradorOptional.get();

        if (!passwordEncoder.matches(senha, morador.getSenha())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        return jwtUtil.generateToken(morador.getEmail()); // Incluindo a "role" de morador
    }
}
