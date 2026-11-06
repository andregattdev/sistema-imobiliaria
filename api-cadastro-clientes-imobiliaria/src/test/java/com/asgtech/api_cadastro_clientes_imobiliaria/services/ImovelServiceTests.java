package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteReferenciaDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RequisicaoInvalidaException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Endereco;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Imovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;

class ImovelServiceTests {

    private final ImovelRepository imovelRepository = mock(ImovelRepository.class);
    private final ClienteRepository clienteRepository = mock(ClienteRepository.class);
    private final ImovelService imovelService =
            new ImovelService(imovelRepository, clienteRepository);

    @Test
    void listaImoveisAtivosNaPaginaSolicitada() {
        Imovel imovel = new Imovel();
        imovel.setId(11L);
        imovel.setEndereco(new Endereco());
        imovel.setProprietario(new Cliente());
        PageRequest pageable = PageRequest.of(1, 2);
        when(imovelRepository.findAllByAtivoTrueAndProprietarioAtivoTrue(pageable))
                .thenReturn(new PageImpl<>(List.of(imovel), pageable, 5));

        Page<ImovelResponseDTO> resultado = imovelService.listarTodos(pageable);

        assertEquals(1, resultado.getNumber());
        assertEquals(2, resultado.getSize());
        assertEquals(5, resultado.getTotalElements());
        assertEquals(11L, resultado.getContent().getFirst().id());
    }

    @Test
    void aplicaFiltrosDeBuscaAoRepositorio() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(imovelRepository.buscarComFiltros(
                TipoImovel.CASA,
                FinalidadeImovel.VENDA,
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(800000),
                2,
                "pinheiros",
                StatusImovel.DISPONIVEL,
                FinalidadeImovel.ALUGUEL,
                pageable))
                .thenReturn(Page.empty(pageable));

        Page<ImovelResponseDTO> resultado = imovelService.buscarComFiltros(
                TipoImovel.CASA,
                FinalidadeImovel.VENDA,
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(800000),
                2,
                " pinheiros ",
                StatusImovel.DISPONIVEL,
                pageable);

        assertTrue(resultado.isEmpty());
        org.mockito.Mockito.verify(imovelRepository).buscarComFiltros(
                TipoImovel.CASA,
                FinalidadeImovel.VENDA,
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(800000),
                2,
                "pinheiros",
                StatusImovel.DISPONIVEL,
                FinalidadeImovel.ALUGUEL,
                pageable);
    }

    @Test
    void criaImovelAssociandoClientePersistido() {
        Cliente proprietario = new Cliente();
        proprietario.setId(7L);
        when(clienteRepository.findByIdAndAtivoTrue(7L)).thenReturn(Optional.of(proprietario));
        when(imovelRepository.save(org.mockito.ArgumentMatchers.any(Imovel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ImovelRequestDTO dados = dadosDoImovel(7L);

        ImovelResponseDTO criado = imovelService.criar(dados);

        assertEquals(7L, criado.proprietario().id());
        assertTrue(criado.ativo());
        org.mockito.ArgumentCaptor<Imovel> imovelSalvo =
                org.mockito.ArgumentCaptor.forClass(Imovel.class);
        org.mockito.Mockito.verify(imovelRepository).save(imovelSalvo.capture());
        assertSame(proprietario, imovelSalvo.getValue().getProprietario());
        org.junit.jupiter.api.Assertions.assertNull(imovelSalvo.getValue().getId());
        org.junit.jupiter.api.Assertions.assertNull(imovelSalvo.getValue().getEndereco().getId());
    }

    @Test
    void rejeitaImovelSemIdDoProprietario() {
        assertThrows(
                RequisicaoInvalidaException.class,
                () -> imovelService.criar(dadosDoImovel(null)));
    }

    @Test
    void inativaImovelSemRemoveLoDoRepositorio() {
        Imovel imovel = new Imovel();
        imovel.setId(11L);
        imovel.setEndereco(new Endereco());
        imovel.setProprietario(new Cliente());
        when(imovelRepository.findByIdAndAtivoTrueAndProprietarioAtivoTrue(11L))
                .thenReturn(Optional.of(imovel));
        when(imovelRepository.save(imovel)).thenReturn(imovel);

        ImovelResponseDTO resultado = imovelService.inativar(11L);

        assertFalse(resultado.ativo());
        org.mockito.Mockito.verify(imovelRepository).save(imovel);
        org.mockito.Mockito.verify(imovelRepository, never()).delete(imovel);
    }

    @Test
    void ativaImovelSeProprietarioEstiverAtivo() {
        Imovel imovel = new Imovel();
        imovel.setId(11L);
        imovel.setAtivo(false);
        imovel.setEndereco(new Endereco());
        imovel.setProprietario(new Cliente());
        when(imovelRepository.findByIdAndProprietarioAtivoTrue(11L))
                .thenReturn(Optional.of(imovel));
        when(imovelRepository.save(imovel)).thenReturn(imovel);

        ImovelResponseDTO resultado = imovelService.ativar(11L);

        assertTrue(resultado.ativo());
        org.mockito.Mockito.verify(imovelRepository).save(imovel);
    }

    private ImovelRequestDTO dadosDoImovel(Long proprietarioId) {
        return new ImovelRequestDTO(
                TipoImovel.APARTAMENTO,
                StatusImovel.DISPONIVEL,
                FinalidadeImovel.VENDA,
                BigDecimal.valueOf(75),
                2,
                1,
                1,
                BigDecimal.valueOf(450000),
                null,
                BigDecimal.valueOf(650),
                new EnderecoRequestDTO(
                        "01310-200",
                        "Avenida Paulista",
                        "1000",
                        null,
                        null,
                        "Bela Vista",
                        "São Paulo",
                        "SP",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                proprietarioId == null ? null : new ClienteReferenciaDTO(proprietarioId));
    }
}
