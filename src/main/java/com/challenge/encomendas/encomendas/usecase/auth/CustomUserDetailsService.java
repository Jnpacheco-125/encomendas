package com.challenge.encomendas.encomendas.usecase.auth;

import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private FuncionarioGateway funcionarioGateway;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioGateway.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado com o email: " + email));

        return new User(funcionario.getEmail(), funcionario.getSenha(), Collections.emptyList());
    }
}
