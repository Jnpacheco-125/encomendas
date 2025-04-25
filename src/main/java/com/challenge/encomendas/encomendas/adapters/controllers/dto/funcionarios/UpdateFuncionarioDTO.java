package com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateFuncionarioDTO(
        String nome,

        @Email(message = "Formato de e-mail inválido")
        String email) {

}
