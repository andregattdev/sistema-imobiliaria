package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.NegociacaoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.NegociacaoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Negociacao;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.CorretorRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.NegociacaoRepository;

@Service
@Transactional(readOnly = true)
public class NegociacaoService {

    private final NegociacaoRepository negociacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ImovelRepository imovelRepository;
    private final CorretorRepository corretorRepository;

    public NegociacaoService(
            NegociacaoRepository negociacaoRepository,
            ClienteRepository clienteRepository,
            ImovelRepository imovelRepository,
            CorretorRepository corretorRepository) {
        this.negociacaoRepository = negociacaoRepository;
        this.clienteRepository = clienteRepository;
        this.imovelRepository = imovelRepository;
        this.corretorRepository = corretorRepository;
    }

    public Page<NegociacaoResponseDTO> listar(Pageable pageable) {
        return negociacaoRepository.findAllByOrderByAtualizadaEmDesc(pageable)
                .map(NegociacaoResponseDTO::from);
    }

    @Transactional
    public NegociacaoResponseDTO criar(NegociacaoRequestDTO dados) {
        return NegociacaoResponseDTO.from(atualizarDados(new Negociacao(), dados));
    }

    @Transactional
    public NegociacaoResponseDTO atualizar(Long id, NegociacaoRequestDTO dados) {
        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Negociação não encontrada."));
        return NegociacaoResponseDTO.from(atualizarDados(negociacao, dados));
    }

    private Negociacao atualizarDados(Negociacao negociacao, NegociacaoRequestDTO dados) {
        negociacao.setCliente(clienteRepository.findByIdAndAtivoTrue(dados.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente ativo não encontrado.")));
        negociacao.setImovel(imovelRepository
                .findByIdAndAtivoTrueAndProprietarioAtivoTrue(dados.imovelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Imóvel ativo não encontrado.")));
        negociacao.setCorretor(corretorRepository.findByIdAndAtivoTrue(dados.corretorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Corretor ativo não encontrado.")));
        negociacao.setEtapa(dados.etapa());
        negociacao.setObservacoes(dados.observacoes().trim());
        return negociacaoRepository.save(negociacao);
    }
}
