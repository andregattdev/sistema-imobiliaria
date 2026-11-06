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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.AtendimentoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.AtendimentoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.AtendimentoService;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @GetMapping
    public Page<AtendimentoResponseDTO> listarPorCliente(
            @RequestParam Long clienteId,
            @PageableDefault(size = 20) Pageable pageable) {
        return atendimentoService.listarPorCliente(clienteId, pageable);
    }

    @GetMapping("/{id}")
    public AtendimentoResponseDTO buscarPorId(@PathVariable Long id) {
        return atendimentoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> registrar(
            @Valid @RequestBody AtendimentoRequestDTO atendimento) {
        AtendimentoResponseDTO registrado = atendimentoService.registrar(atendimento);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registrado.id())
                .toUri();
        return ResponseEntity.created(location).body(registrado);
    }
}
