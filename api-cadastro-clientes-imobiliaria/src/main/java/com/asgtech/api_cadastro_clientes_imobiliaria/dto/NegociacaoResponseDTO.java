package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.time.LocalDateTime;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.EtapaNegociacao;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Negociacao;

public record NegociacaoResponseDTO(
        Long id,
        Long clienteId,
        String nomeCliente,
        Long imovelId,
        String resumoImovel,
        Long corretorId,
        String nomeCorretor,
        EtapaNegociacao etapa,
        String observacoes,
        LocalDateTime criadaEm,
        LocalDateTime atualizadaEm) {

    public static NegociacaoResponseDTO from(Negociacao negociacao) {
        var endereco = negociacao.getImovel().getEndereco();
        return new NegociacaoResponseDTO(
                negociacao.getId(),
                negociacao.getCliente().getId(),
                negociacao.getCliente().getNome(),
                negociacao.getImovel().getId(),
                negociacao.getImovel().getTipo() + " · "
                        + endereco.getLogradouro() + ", " + endereco.getNumero()
                        + " · " + endereco.getLocalidade() + "/" + endereco.getUf(),
                negociacao.getCorretor().getId(),
                negociacao.getCorretor().getNome(),
                negociacao.getEtapa(),
                negociacao.getObservacoes(),
                negociacao.getCriadaEm(),
                negociacao.getAtualizadaEm());
    }
}
