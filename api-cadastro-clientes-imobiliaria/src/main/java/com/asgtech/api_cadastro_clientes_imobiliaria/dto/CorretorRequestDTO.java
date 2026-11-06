package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CorretorRequestDTO(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Size(max = 30) String creci,
        @NotBlank @Size(max = 20) String telefone) {
}
