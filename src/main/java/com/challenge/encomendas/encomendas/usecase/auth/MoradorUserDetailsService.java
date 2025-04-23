package com.challenge.encomendas.encomendas.usecase.auth;

import com.challenge.encomendas.encomendas.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendas.domain.entities.Morador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;

@Service
public class MoradorUserDetailsService implements UserDetailsService {
    @Autowired
    private MoradorGateway moradorGateway;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Morador morador = moradorGateway.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Morador não encontrado com o email: " + email));

        return new User(morador.getEmail(), morador.getSenha(), Collections.emptyList());
    }
}
