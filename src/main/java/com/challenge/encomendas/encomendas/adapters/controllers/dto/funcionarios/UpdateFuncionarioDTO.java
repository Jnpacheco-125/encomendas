package com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para atualizar os dados de um funcionário")
public record UpdateFuncionarioDTO(
        @Schema(description = "Novo nome completo do funcionário", example = "Mariana Alves", nullable = true)
        String nome,

        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "Novo endereço de e-mail do funcionário", example = "mariana.alves@email.com", format = "email", nullable = true)
        String email
) {
}
