package com.challenge.encomendas.encomendas.adapters.controllers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginRequestDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginResponseDTO;
import com.challenge.encomendas.encomendas.usecase.auth.AuthService;
import com.challenge.encomendas.encomendas.usecase.cadastro.FuncionarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    private final AuthService authService;
    private final FuncionarioService funcionarioService; // Injete o serviço de funcionário

    public FuncionarioController(AuthService authService, FuncionarioService funcionarioService) {
        this.authService = authService;
        this.funcionarioService = funcionarioService; // Inicialização aqui
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request.email(), request.senha()); // Passando email e senha separadamente
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
