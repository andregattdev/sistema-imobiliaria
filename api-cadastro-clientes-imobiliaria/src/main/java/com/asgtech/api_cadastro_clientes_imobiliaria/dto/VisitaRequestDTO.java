package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VisitaRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long imovelId,
        @NotNull Long corretorId,
        @NotNull LocalDateTime dataHora,
        LocalDateTime dataHoraLembrete,
        @NotBlank @Size(max = 1000) String observacoes) {

    @AssertTrue(message = "O lembrete deve acontecer antes da visita.")
    public boolean isLembreteValido() {
        return dataHoraLembrete == null || dataHora != null && !dataHoraLembrete.isAfter(dataHora);
    }
}
