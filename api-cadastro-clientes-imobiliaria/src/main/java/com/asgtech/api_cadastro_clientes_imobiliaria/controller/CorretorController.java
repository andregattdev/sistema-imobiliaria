package com.asgtech.api_cadastro_clientes_imobiliaria.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.CorretorRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.CorretorResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.CorretorService;

@RestController
@RequestMapping("/api/corretores")
public class CorretorController {

    private final CorretorService corretorService;

    public CorretorController(CorretorService corretorService) {
        this.corretorService = corretorService;
    }

    @GetMapping
    public List<CorretorResponseDTO> listar() {
        return corretorService.listarAtivos();
    }

    @PostMapping
    public ResponseEntity<CorretorResponseDTO> criar(@Valid @RequestBody CorretorRequestDTO dados) {
        CorretorResponseDTO corretor = corretorService.criar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(corretor.id())
                .toUri();
        return ResponseEntity.created(location).body(corretor);
    }
}
