package com.asgtech.api_cadastro_clientes_imobiliaria.controller;

import java.net.URI;
import java.math.BigDecimal;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.services.ImovelService;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping
    public Page<ImovelResponseDTO> listarTodos(
            @RequestParam(required = false) TipoImovel tipo,
            @RequestParam(required = false) FinalidadeImovel finalidade,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) Integer quartos,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) StatusImovel disponibilidade,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return imovelService.buscarComFiltros(
                tipo,
                finalidade,
                precoMinimo,
                precoMaximo,
                quartos,
                localizacao,
                disponibilidade,
                pageable);
    }

    @GetMapping("/{id}")
    public ImovelResponseDTO buscarPorId(@PathVariable Long id) {
        return imovelService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ImovelResponseDTO> criar(@Valid @RequestBody ImovelRequestDTO imovel) {
        ImovelResponseDTO imovelCriado = imovelService.criar(imovel);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(imovelCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(imovelCriado);
    }

    @PutMapping("/{id}")
    public ImovelResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ImovelRequestDTO imovel) {
        return imovelService.atualizar(id, imovel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        imovelService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ImovelResponseDTO ativar(@PathVariable Long id) {
        return imovelService.ativar(id);
    }
}
