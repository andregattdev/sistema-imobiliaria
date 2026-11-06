package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusConfirmacaoVisita;

import jakarta.validation.constraints.NotNull;

public record StatusConfirmacaoRequestDTO(@NotNull StatusConfirmacaoVisita statusConfirmacao) {
}
