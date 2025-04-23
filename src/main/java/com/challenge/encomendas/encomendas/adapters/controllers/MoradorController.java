package com.challenge.encomendas.encomendas.adapters.controllers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.CadastroMoradorDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.LoginMoradorRequestDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.LoginMoradorResponseDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.MoradorResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.infrastructure.persistence.mappers.MoradorMapper;
import com.challenge.encomendas.encomendas.usecase.auth.AuthMoradorService;
import com.challenge.encomendas.encomendas.usecase.cadastro.MoradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/moradores")
@Tag(name = "Moradores", description = "Operações relacionadas ao gerenciamento de moradores")
public class MoradorController {
    private final MoradorService moradorService;
    private final AuthMoradorService authMoradorService; // Injete o serviço de autenticação para moradores

    public MoradorController(MoradorService moradorService, AuthMoradorService authMoradorService) {
        this.moradorService = moradorService;
        this.authMoradorService = authMoradorService;
    }

    @Operation(summary = "Login de morador", description = "Autentica um morador e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginMoradorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginMoradorResponseDTO> loginMorador(@Valid @RequestBody LoginMoradorRequestDTO request) {
        String token = authMoradorService.login(request.email(), request.senha());
        return ResponseEntity.ok(new LoginMoradorResponseDTO(token));
    }

    @Operation(summary = "Cadastro de morador", description = "Registra um novo morador no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Morador cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoradorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou morador já existente", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<MoradorResponseDTO> cadastrarMorador(@Valid @RequestBody CadastroMoradorDTO cadastroDTO) {
        log.info("Cadastro de morador solicitado para o email: {}", cadastroDTO.email());

        Morador novoMorador = moradorService.cadastrar(
                cadastroDTO.nome(),
                cadastroDTO.telefone(),
                cadastroDTO.apartamento(),
                cadastroDTO.email(),
                cadastroDTO.senha()
        );

        MoradorResponseDTO response = MoradorMapper.toResponseDTO(novoMorador);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
