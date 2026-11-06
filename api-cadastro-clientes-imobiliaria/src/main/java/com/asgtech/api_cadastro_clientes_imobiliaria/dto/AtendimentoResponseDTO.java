package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Atendimento;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoAtendimento;

public record AtendimentoResponseDTO(
        Long id,
        Long clienteId,
        String nomeCliente,
        Long imovelId,
        String resumoImovel,
        TipoAtendimento tipo,
        LocalDateTime dataHora,
        String descricao,
        BigDecimal valorProposta) {

    public static AtendimentoResponseDTO from(Atendimento atendimento) {
        var endereco = atendimento.getImovel().getEndereco();
        return new AtendimentoResponseDTO(
                atendimento.getId(),
                atendimento.getCliente().getId(),
                atendimento.getCliente().getNome(),
                atendimento.getImovel().getId(),
                atendimento.getImovel().getTipo() + " · "
                        + endereco.getLogradouro() + ", " + endereco.getNumero()
                        + " · " + endereco.getLocalidade() + "/" + endereco.getUf(),
                atendimento.getTipo(),
                atendimento.getDataHora(),
                atendimento.getDescricao(),
                atendimento.getValorProposta());
    }
}
