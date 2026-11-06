package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.AtendimentoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Atendimento;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Endereco;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Imovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoAtendimento;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.AtendimentoRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;

class AtendimentoServiceTests {

    private final AtendimentoRepository atendimentoRepository = mock(AtendimentoRepository.class);
    private final ClienteRepository clienteRepository = mock(ClienteRepository.class);
    private final ImovelRepository imovelRepository = mock(ImovelRepository.class);
    private final AtendimentoService atendimentoService =
            new AtendimentoService(atendimentoRepository, clienteRepository, imovelRepository);

    @Test
    void registraEventoVinculadoAoClienteEImovel() {
        Cliente cliente = new Cliente();
        cliente.setId(5L);
        cliente.setNome("Ana");
        Imovel imovel = new Imovel();
        imovel.setId(9L);
        imovel.setEndereco(new Endereco());
        when(clienteRepository.findByIdAndAtivoTrue(5L)).thenReturn(Optional.of(cliente));
        when(imovelRepository.findByIdAndAtivoTrueAndProprietarioAtivoTrue(9L))
                .thenReturn(Optional.of(imovel));
        when(atendimentoRepository.save(org.mockito.ArgumentMatchers.any(Atendimento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = atendimentoService.registrar(new AtendimentoRequestDTO(
                5L,
                9L,
                TipoAtendimento.INTERESSE,
                LocalDateTime.of(2026, 11, 6, 10, 30),
                "  Cliente demonstrou interesse  ",
                null));

        assertEquals(5L, resultado.clienteId());
        assertEquals(9L, resultado.imovelId());
        assertEquals("Cliente demonstrou interesse", resultado.descricao());
    }

    @Test
    void listaHistoricoDoClienteEmOrdemDefinidaPeloRepositorio() {
        Cliente cliente = new Cliente();
        cliente.setId(5L);
        cliente.setNome("Ana");
        Atendimento atendimento = new Atendimento();
        atendimento.setCliente(cliente);
        atendimento.setImovel(new Imovel());
        atendimento.getImovel().setEndereco(new Endereco());
        atendimento.setTipo(TipoAtendimento.INTERESSE);
        atendimento.setDataHora(LocalDateTime.of(2026, 11, 6, 10, 30));
        PageRequest pageable = PageRequest.of(0, 10);
        when(clienteRepository.findByIdAndAtivoTrue(5L)).thenReturn(Optional.of(cliente));
        when(atendimentoRepository
                .findAllByClienteIdAndClienteAtivoTrueOrderByDataHoraDesc(5L, pageable))
                .thenReturn(new PageImpl<>(java.util.List.of(atendimento), pageable, 1));

        Page<?> resultado = atendimentoService.listarPorCliente(5L, pageable);

        assertEquals(1, resultado.getTotalElements());
    }
}
