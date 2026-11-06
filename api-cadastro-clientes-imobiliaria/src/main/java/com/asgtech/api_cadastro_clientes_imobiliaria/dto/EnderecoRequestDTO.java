package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

public record EnderecoRequestDTO(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String unidade,
        String bairro,
        String cidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String gia,
        String ddd,
        String siafi) {
}
