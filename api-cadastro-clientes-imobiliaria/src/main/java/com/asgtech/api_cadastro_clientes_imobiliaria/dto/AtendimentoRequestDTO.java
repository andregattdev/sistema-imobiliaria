package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoAtendimento;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AtendimentoRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long imovelId,
        @NotNull TipoAtendimento tipo,
        @NotNull LocalDateTime dataHora,
        @NotBlank @Size(max = 1000) String descricao,
        @Positive BigDecimal valorProposta) {

    @AssertTrue(message = "Informe o valor da proposta.")
    public boolean isValorPropostaValido() {
        return tipo != TipoAtendimento.PROPOSTA
                || valorProposta != null && valorProposta.signum() > 0;
    }
}
