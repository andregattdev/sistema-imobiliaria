package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Corretor;

public record CorretorResponseDTO(Long id, String nome, String creci, String telefone) {

    public static CorretorResponseDTO from(Corretor corretor) {
        return new CorretorResponseDTO(
                corretor.getId(),
                corretor.getNome(),
                corretor.getCreci(),
                corretor.getTelefone());
    }
}
