package com.asgtech.api_cadastro_clientes_imobiliaria.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.StatusConfirmacaoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.VisitaRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.VisitaResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.VisitaService;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {

    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @GetMapping
    public List<VisitaResponseDTO> listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return visitaService.listar(inicio, fim);
    }

    @PostMapping
    public ResponseEntity<VisitaResponseDTO> agendar(@Valid @RequestBody VisitaRequestDTO dados) {
        VisitaResponseDTO visita = visitaService.agendar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(visita.id())
                .toUri();
        return ResponseEntity.created(location).body(visita);
    }

    @PatchMapping("/{id}/confirmacao")
    public VisitaResponseDTO atualizarConfirmacao(
            @PathVariable Long id,
            @Valid @RequestBody StatusConfirmacaoRequestDTO dados) {
        return visitaService.atualizarConfirmacao(id, dados);
    }
}
