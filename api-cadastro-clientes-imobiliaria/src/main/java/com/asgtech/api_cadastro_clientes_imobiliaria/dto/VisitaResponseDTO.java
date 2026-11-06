package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.time.LocalDateTime;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusConfirmacaoVisita;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Visita;

public record VisitaResponseDTO(
        Long id,
        Long clienteId,
        String nomeCliente,
        Long imovelId,
        String resumoImovel,
        Long corretorId,
        String nomeCorretor,
        LocalDateTime dataHora,
        LocalDateTime dataHoraLembrete,
        StatusConfirmacaoVisita statusConfirmacao,
        String observacoes) {

    public static VisitaResponseDTO from(Visita visita) {
        var endereco = visita.getImovel().getEndereco();
        return new VisitaResponseDTO(
                visita.getId(),
                visita.getCliente().getId(),
                visita.getCliente().getNome(),
                visita.getImovel().getId(),
                visita.getImovel().getTipo() + " · "
                        + endereco.getLogradouro() + ", " + endereco.getNumero()
                        + " · " + endereco.getLocalidade() + "/" + endereco.getUf(),
                visita.getCorretor().getId(),
                visita.getCorretor().getNome(),
                visita.getDataHora(),
                visita.getDataHoraLembrete(),
                visita.getStatusConfirmacao(),
                visita.getObservacoes());
    }
}
