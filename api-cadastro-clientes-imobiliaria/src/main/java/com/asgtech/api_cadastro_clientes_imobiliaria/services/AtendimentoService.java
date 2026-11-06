package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.AtendimentoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.AtendimentoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Atendimento;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.AtendimentoRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;

@Service
@Transactional(readOnly = true)
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ClienteRepository clienteRepository;
    private final ImovelRepository imovelRepository;

    public AtendimentoService(
            AtendimentoRepository atendimentoRepository,
            ClienteRepository clienteRepository,
            ImovelRepository imovelRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.clienteRepository = clienteRepository;
        this.imovelRepository = imovelRepository;
    }

    public Page<AtendimentoResponseDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        clienteRepository.findByIdAndAtivoTrue(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        return atendimentoRepository
                .findAllByClienteIdAndClienteAtivoTrueOrderByDataHoraDesc(clienteId, pageable)
                .map(AtendimentoResponseDTO::from);
    }

    public AtendimentoResponseDTO buscarPorId(Long id) {
        return atendimentoRepository.findById(id)
                .map(AtendimentoResponseDTO::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado."));
    }

    @Transactional
    public AtendimentoResponseDTO registrar(AtendimentoRequestDTO dados) {
        var cliente = clienteRepository.findByIdAndAtivoTrue(dados.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente ativo não encontrado."));
        var imovel = imovelRepository
                .findByIdAndAtivoTrueAndProprietarioAtivoTrue(dados.imovelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Imóvel ativo não encontrado."));

        Atendimento atendimento = new Atendimento();
        atendimento.setCliente(cliente);
        atendimento.setImovel(imovel);
        atendimento.setTipo(dados.tipo());
        atendimento.setDataHora(dados.dataHora());
        atendimento.setDescricao(dados.descricao().trim());
        atendimento.setValorProposta(dados.valorProposta());
        return AtendimentoResponseDTO.from(atendimentoRepository.save(atendimento));
    }
}
