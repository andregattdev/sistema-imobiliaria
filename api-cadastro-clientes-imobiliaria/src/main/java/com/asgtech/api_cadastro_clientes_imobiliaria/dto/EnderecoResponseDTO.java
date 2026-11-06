package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Endereco;

public record EnderecoResponseDTO(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String unidade,
        String bairro,
        String cidade,
        String uf) {

    public static EnderecoResponseDTO from(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getUnidade(),
                endereco.getBairro(),
                endereco.getLocalidade(),
                endereco.getUf());
    }
}
