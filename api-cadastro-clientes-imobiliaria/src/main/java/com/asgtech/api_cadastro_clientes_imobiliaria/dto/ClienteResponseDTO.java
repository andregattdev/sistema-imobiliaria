package com.asgtech.api_cadastro_clientes_imobiliaria.dto;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;

public record ClienteResponseDTO(
        Long id,
        boolean ativo,
        String nome,
        String email,
        String telefone,
        EnderecoResponseDTO endereco) {

    public static ClienteResponseDTO from(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.isAtivo(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco() == null ? null : EnderecoResponseDTO.from(cliente.getEndereco()));
    }
}
