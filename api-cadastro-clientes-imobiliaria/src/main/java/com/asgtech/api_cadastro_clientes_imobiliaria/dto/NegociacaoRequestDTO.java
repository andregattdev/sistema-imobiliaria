package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.EtapaNegociacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NegociacaoRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long imovelId,
        @NotNull Long corretorId,
        @NotNull EtapaNegociacao etapa,
        @NotBlank @Size(max = 2000) String observacoes) {
}
