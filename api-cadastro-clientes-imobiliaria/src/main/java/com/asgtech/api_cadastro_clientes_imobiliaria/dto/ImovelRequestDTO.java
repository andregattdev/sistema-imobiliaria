package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.math.BigDecimal;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ImovelRequestDTO(
        @NotNull TipoImovel tipo,
        @NotNull StatusImovel status,
        @NotNull FinalidadeImovel finalidade,
        @NotNull @Positive BigDecimal areaUtil,
        @NotNull @PositiveOrZero Integer quartos,
        @NotNull @PositiveOrZero Integer banheiros,
        @NotNull @PositiveOrZero Integer vagasGaragem,
        @NotNull @PositiveOrZero BigDecimal precoVenda,
        @PositiveOrZero BigDecimal precoAluguel,
        @NotNull @PositiveOrZero BigDecimal precoCondominio,
        @NotNull @Valid EnderecoRequestDTO endereco,
        @NotNull @Valid ClienteReferenciaDTO proprietario) {

    public ImovelRequestDTO {
        if (finalidade == null) {
            finalidade = FinalidadeImovel.VENDA;
        }
    }

    @AssertTrue(message = "Informe um preço maior que zero para a finalidade selecionada.")
    public boolean isPrecoFinalidadeValido() {
        return finalidade == FinalidadeImovel.ALUGUEL
                ? precoAluguel != null && precoAluguel.signum() > 0
                : precoVenda != null && precoVenda.signum() > 0;
    }
}
