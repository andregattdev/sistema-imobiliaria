package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;

public record ProprietarioResponseDTO(Long id, String nome) {

    public static ProprietarioResponseDTO from(Cliente proprietario) {
        return new ProprietarioResponseDTO(proprietario.getId(), proprietario.getNome());
    }
}
