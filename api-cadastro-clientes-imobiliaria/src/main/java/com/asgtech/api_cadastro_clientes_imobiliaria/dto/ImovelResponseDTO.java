package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import java.math.BigDecimal;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Imovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;

public record ImovelResponseDTO(
        Long id,
        boolean ativo,
        TipoImovel tipo,
        StatusImovel status,
        FinalidadeImovel finalidade,
        BigDecimal areaUtil,
        Integer quartos,
        Integer banheiros,
        Integer vagasGaragem,
        BigDecimal precoVenda,
        BigDecimal precoAluguel,
        BigDecimal precoCondominio,
        EnderecoResponseDTO endereco,
        ProprietarioResponseDTO proprietario) {

    public static ImovelResponseDTO from(Imovel imovel) {
        return new ImovelResponseDTO(
                imovel.getId(),
                imovel.isAtivo(),
                imovel.getTipo(),
                imovel.getStatus(),
                imovel.getFinalidade(),
                imovel.getAreaUtil(),
                imovel.getQuartos(),
                imovel.getBanheiros(),
                imovel.getVagasGaragem(),
                imovel.getPrecoVenda(),
                imovel.getPrecoAluguel(),
                imovel.getPrecoCondominio(),
                EnderecoResponseDTO.from(imovel.getEndereco()),
                ProprietarioResponseDTO.from(imovel.getProprietario()));
    }
}
