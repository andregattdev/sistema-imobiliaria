package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.StatusConfirmacaoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.VisitaRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.VisitaResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Visita;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.CorretorRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.VisitaRepository;

@Service
@Transactional(readOnly = true)
public class VisitaService {

    private final VisitaRepository visitaRepository;
    private final ClienteRepository clienteRepository;
    private final ImovelRepository imovelRepository;
    private final CorretorRepository corretorRepository;

    public VisitaService(
            VisitaRepository visitaRepository,
            ClienteRepository clienteRepository,
            ImovelRepository imovelRepository,
            CorretorRepository corretorRepository) {
        this.visitaRepository = visitaRepository;
        this.clienteRepository = clienteRepository;
        this.imovelRepository = imovelRepository;
        this.corretorRepository = corretorRepository;
    }

    public List<VisitaResponseDTO> listar(LocalDateTime inicio, LocalDateTime fim) {
        return visitaRepository
                .findAllByDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc(inicio, fim)
                .stream()
                .map(VisitaResponseDTO::from)
                .toList();
    }

    @Transactional
    public VisitaResponseDTO agendar(VisitaRequestDTO dados) {
        Visita visita = new Visita();
        visita.setCliente(clienteRepository.findByIdAndAtivoTrue(dados.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente ativo não encontrado.")));
        visita.setImovel(imovelRepository
                .findByIdAndAtivoTrueAndProprietarioAtivoTrue(dados.imovelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Imóvel ativo não encontrado.")));
        visita.setCorretor(corretorRepository.findByIdAndAtivoTrue(dados.corretorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Corretor ativo não encontrado.")));
        visita.setDataHora(dados.dataHora());
        visita.setDataHoraLembrete(dados.dataHoraLembrete());
        visita.setObservacoes(dados.observacoes().trim());
        return VisitaResponseDTO.from(visitaRepository.save(visita));
    }

    @Transactional
    public VisitaResponseDTO atualizarConfirmacao(Long id, StatusConfirmacaoRequestDTO dados) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visita não encontrada."));
        visita.setStatusConfirmacao(dados.statusConfirmacao());
        return VisitaResponseDTO.from(visitaRepository.save(visita));
    }
}
