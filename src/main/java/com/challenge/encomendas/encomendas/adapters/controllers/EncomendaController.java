package com.challenge.encomendas.encomendas.adapters.controllers;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.encomendas.AtualizarEncomendaDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.encomendas.EncomendaRequestDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.encomendas.EncomendaResponseDTO;
import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import com.challenge.encomendas.encomendas.infrastructure.persistence.mappers.EncomendaMapper;
import com.challenge.encomendas.encomendas.usecase.cadastro.EncomendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;

    @Autowired
    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    @Operation(
            summary = "Cadastro de encomenda",
            description = "Registra uma nova encomenda no sistema e envia uma notificação por e-mail ao morador.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de cadastro de encomenda",
                                            value = """
                            {
                              "nomeDestinatario": "Carlos Souza",
                              "apartamento": "101",
                              "descricao": "Caixa grande - Loja X",
                              "dataRecebimento": "2025-04-27T20:00:00",
                              "retirada": false,
                              "funcionarioRecebimento": {
                                "id": 1,
                                "nome": "João Porteiro"
                              },
                              "moradorDestinatario": {
                                "id": 10,
                                "nome": "Carlos Souza"
                              }
                            }
                            """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Encomenda cadastrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro no cadastro", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('PORTEIRO') or hasRole('ADMIN')")
    @PostMapping("/encomendas")
    public ResponseEntity<EncomendaResponseDTO> cadastrarEncomenda(@Valid @RequestBody EncomendaRequestDTO requestDTO) {
        log.info("Cadastro de encomenda solicitado para o morador: {}", requestDTO.moradorDestinatario().nome());

        Encomenda novaEncomenda = encomendaService.cadastrar(requestDTO);
        EncomendaResponseDTO response = EncomendaMapper.toResponseDTO(novaEncomenda);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Listar encomendas pendentes", description = "Retorna todas as encomendas que ainda não foram retiradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de encomendas pendentes retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PORTEIRO')")
    @GetMapping("/pendentes")
    public ResponseEntity<List<EncomendaResponseDTO>> listarPendentes() {
        List<Encomenda> encomendas = encomendaService.buscarEncomendasPendentes();
        List<EncomendaResponseDTO> response = encomendas.stream()
                .map(EncomendaMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Buscar encomenda por ID", description = "Retorna os detalhes de uma encomenda com base no seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encomenda encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Encomenda não encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PORTEIRO')")
    @GetMapping("/{id}")
    public ResponseEntity<EncomendaResponseDTO> buscarPorId(@PathVariable Long id) {
        Encomenda encomenda = encomendaService.buscarPorId(id); // esse método deve lançar exceção se não encontrar
        EncomendaResponseDTO responseDTO = EncomendaMapper.toResponseDTO(encomenda);
        return ResponseEntity.ok(responseDTO);
    }
    @Operation(summary = "Buscar encomendas por morador", description = "Retorna todas as encomendas associadas a um morador específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encomendas encontradas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PORTEIRO') or hasRole('MORADOR')")
    @GetMapping("/morador/{moradorId}")
    public ResponseEntity<List<EncomendaResponseDTO>> buscarPorMorador(@PathVariable Long moradorId) {
        List<Encomenda> encomendas = encomendaService.buscarEncomendasPorMorador(moradorId);
        List<EncomendaResponseDTO> response = encomendas.stream()
                .map(EncomendaMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Confirmar retirada de encomenda", description = "Atualiza uma encomenda para indicar que foi retirada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encomenda retirada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Encomenda não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Encomenda já foi retirada", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PORTEIRO')")
    @PatchMapping("/{id}/retirada")
    public ResponseEntity<EncomendaResponseDTO> confirmarRetirada(
            @PathVariable Long id,
            @RequestBody AtualizarEncomendaDTO dto) {
        Encomenda encomendaAtualizada = encomendaService.confirmarRetirada(id, dto);
        return ResponseEntity.ok(EncomendaMapper.toResponseDTO(encomendaAtualizada));
    }

    @Operation(summary = "Listar encomendas retiradas", description = "Retorna todas as encomendas que já foram retiradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de encomendas retiradas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EncomendaResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PORTEIRO')")
    @GetMapping("/retiradas")
    public ResponseEntity<List<EncomendaResponseDTO>> listarRetiradas() {
        List<Encomenda> encomendas = encomendaService.buscarEncomendasRetiradas();
        List<EncomendaResponseDTO> response = encomendas.stream()
                .map(EncomendaMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }
}

