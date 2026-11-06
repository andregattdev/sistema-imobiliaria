package com.asgtech.api_cadastro_clientes_imobiliaria.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.NegociacaoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.NegociacaoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.NegociacaoService;

@RestController
@RequestMapping("/api/negociacoes")
public class NegociacaoController {

    private final NegociacaoService negociacaoService;

    public NegociacaoController(NegociacaoService negociacaoService) {
        this.negociacaoService = negociacaoService;
    }

    @GetMapping
    public Page<NegociacaoResponseDTO> listar(@PageableDefault(size = 100) Pageable pageable) {
        return negociacaoService.listar(pageable);
    }

    @PostMapping
    public ResponseEntity<NegociacaoResponseDTO> criar(
            @Valid @RequestBody NegociacaoRequestDTO dados) {
        NegociacaoResponseDTO negociacao = negociacaoService.criar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(negociacao.id())
                .toUri();
        return ResponseEntity.created(location).body(negociacao);
    }

    @PutMapping("/{id}")
    public NegociacaoResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NegociacaoRequestDTO dados) {
        return negociacaoService.atualizar(id, dados);
    }
}
