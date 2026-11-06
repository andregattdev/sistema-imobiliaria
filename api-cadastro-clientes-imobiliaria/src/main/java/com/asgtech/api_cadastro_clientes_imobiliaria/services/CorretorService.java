package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.CorretorRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.CorretorResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Corretor;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.CorretorRepository;

@Service
@Transactional(readOnly = true)
public class CorretorService {

    private final CorretorRepository corretorRepository;

    public CorretorService(CorretorRepository corretorRepository) {
        this.corretorRepository = corretorRepository;
    }

    public List<CorretorResponseDTO> listarAtivos() {
        return corretorRepository.findAllByAtivoTrueOrderByNomeAsc().stream()
                .map(CorretorResponseDTO::from)
                .toList();
    }

    @Transactional
    public CorretorResponseDTO criar(CorretorRequestDTO dados) {
        Corretor corretor = new Corretor();
        corretor.setNome(dados.nome().trim());
        corretor.setCreci(dados.creci().trim().toUpperCase());
        corretor.setTelefone(dados.telefone().trim());
        return CorretorResponseDTO.from(corretorRepository.save(corretor));
    }
}
