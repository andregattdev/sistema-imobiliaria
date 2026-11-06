package com.asgtech.api_cadastro_clientes_imobiliaria.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.EnderecoService;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/{cep}")
    public EnderecoResponseDTO buscarPorCep(@PathVariable String cep) {
        return enderecoService.buscarPorCep(cep);
    }
}
