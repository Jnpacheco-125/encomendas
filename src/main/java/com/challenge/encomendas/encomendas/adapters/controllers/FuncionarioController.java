package com.challenge.encomendas.encomendas.adapters.controllers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.CadastroFuncionarioDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.FuncionarioResponseDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginRequestDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.usecase.auth.AuthService;
import com.challenge.encomendas.encomendas.usecase.cadastro.FuncionarioService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/funcionarios")
@Tag(name = "Funcionários", description = "Operações relacionadas a autenticação e gestão de funcionários")
public class FuncionarioController {
    private final AuthService authService;
    private final FuncionarioService funcionarioService; // Injete o serviço de funcionário

    public FuncionarioController(AuthService authService, FuncionarioService funcionarioService) {
        this.authService = authService;
        this.funcionarioService = funcionarioService;
    }

    @Operation(summary = "Login de funcionário", description = "Autentica um funcionário e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request.email(), request.senha());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Cadastro de funcionário", description = "Registra um novo funcionário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou funcionário já existente", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@Valid @RequestBody CadastroFuncionarioDTO cadastroDTO) {
        log.info("Cadastro solicitado para o email: {}", cadastroDTO.email());

        Funcionario novoFuncionario = funcionarioService.cadastrar(
                cadastroDTO.nome(),
                cadastroDTO.email(),
                cadastroDTO.senha()
        );

        FuncionarioResponseDTO response = new FuncionarioResponseDTO(
                novoFuncionario.getId(),
                novoFuncionario.getNome(),
                novoFuncionario.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
