package com.challenge.encomendas.encomendas.adapters.controllers.dto.funcionarios;

import com.challenge.encomendas.encomendas.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroFuncionarioDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        @Schema(description = "Tipo de funcionário: ROLE_ADMIN ou ROLE_PORTEIRO", example = "ROLE_ADMIN")
        @NotNull(message = "O papel (role) é obrigatório")
        Role role


) {
}
