package com.challenge.encomendas.encomendas.adapters.controllers;


import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.CadastroFuncionarioDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.FuncionarioResponseDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios.UpdateFuncionarioDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginRequestDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.login.LoginResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.infrastructure.persistence.entities.FuncionarioEntity;
import com.challenge.encomendas.encomendas.infrastructure.persistence.mappers.FuncionarioMapper;
import com.challenge.encomendas.encomendas.usecase.auth.AuthService;
import com.challenge.encomendas.encomendas.usecase.cadastro.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
        try {
            // Use os métodos `email()` e `senha()` do record
            String token = authService.autenticar(request.email(), request.senha());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Cadastro de funcionário",
            description = "Registra um novo funcionário no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de cadastro de porteiro",
                                            value = """
                            {
                              "nome": "João Porteiro",
                              "email": "joao@condominio.com",
                              "senha": "senha123",
                              "role": "ROLE_PORTEIRO"
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo de cadastro de admin",
                                            value = """
                            {
                              "nome": "Ana Admin",
                              "email": "ana@admin.com",
                              "senha": "senha123",
                              "role": "ROLE_ADMIN"
                            }
                            """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou funcionário já existente", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@Valid @RequestBody CadastroFuncionarioDTO cadastroDTO) {
        log.info("Cadastro solicitado para o email: {}", cadastroDTO.email());

        Funcionario novoFuncionario = funcionarioService.cadastrar(cadastroDTO);

        FuncionarioResponseDTO response = FuncionarioMapper.toResponseDTO(novoFuncionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar funcionário por ID", description = "Retorna os detalhes de um funcionário específico com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        FuncionarioResponseDTO responseDTO = FuncionarioMapper.toResponseDTO(funcionario);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Buscar funcionário por e-mail", description = "Retorna os detalhes de um funcionário com base no seu e-mail.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar-por-email")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorEmail(@RequestParam String email) {
        Funcionario funcionario = funcionarioService.buscarPorEmail(email);
        FuncionarioResponseDTO responseDTO = FuncionarioMapper.toResponseDTO(funcionario);
        return ResponseEntity.ok(responseDTO);
    }
    @Operation(summary = "Listar todos os funcionários", description = "Retorna uma lista de todos os funcionários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todos os funcionários retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponseDTO.class))))
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/funcionarios/todos")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.buscarTodos();
        List<FuncionarioResponseDTO> funcionariosResposta = funcionarios.stream()
                .map(funcionario -> new FuncionarioResponseDTO(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getEmail()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(funcionariosResposta);
    }

    @Operation(summary = "Deletar funcionário por ID", description = "Deleta um funcionário do sistema com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/funcionarios/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        try {
            funcionarioService.deletarFuncionario(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content em caso de sucesso
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build(); // Retorna o status da exceção (ex: 404)
        } catch (Exception e) {
            // Lidar com outras exceções inesperadas (logar, retornar 500, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Atualiza os dados de um funcionário",
            description = "Atualiza nome e e-mail de um funcionário existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/funcionarios/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFuncionarioDTO dto) {
        Funcionario atualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(FuncionarioMapper.toResponseDTO(atualizado));
    }

}
