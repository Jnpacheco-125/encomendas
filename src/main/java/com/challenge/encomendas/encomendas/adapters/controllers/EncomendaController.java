package com.challenge.encomendas.encomendas.adapters.controllers;

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


    @PreAuthorize("hasRole('PORTEIRO')")
    @PutMapping("/{id}/retirada")
    public ResponseEntity<Encomenda> confirmarRetirada(@PathVariable Long id) {
        return ResponseEntity.ok(encomendaService.confirmarRetirada(id));
    }
}

