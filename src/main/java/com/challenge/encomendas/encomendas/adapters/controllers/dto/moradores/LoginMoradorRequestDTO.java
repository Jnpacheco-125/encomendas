package com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para a requisição de login de um morador")
public record LoginMoradorRequestDTO(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "Endereço de e-mail do morador para login", example = "morador@email.com", format = "email")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @Schema(description = "Senha do morador para login", example = "senha123")
        String senha
) {
}
