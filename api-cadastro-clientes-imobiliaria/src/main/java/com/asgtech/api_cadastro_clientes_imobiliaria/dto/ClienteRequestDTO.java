package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,
        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,
        @NotBlank(message = "O telefone é obrigatório")
        @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres")
        String telefone,
        @Valid EnderecoRequestDTO endereco) {

    public ClienteRequestDTO(String nome, String email, String telefone) {
        this(nome, email, telefone, null);
    }
}
