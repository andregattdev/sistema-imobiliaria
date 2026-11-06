package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;

class ClienteServiceTests {

    private final ClienteRepository clienteRepository = mock(ClienteRepository.class);
    private final ClienteService clienteService = new ClienteService(clienteRepository);

    @Test
    void listaClientesAtivosNaPaginaSolicitada() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria Silva");
        PageRequest pageable = PageRequest.of(1, 2);
        when(clienteRepository.findAllByAtivoTrue(pageable))
                .thenReturn(new PageImpl<>(List.of(cliente), pageable, 5));

        Page<ClienteResponseDTO> resultado = clienteService.listarTodos(pageable);

        assertEquals(1, resultado.getNumber());
        assertEquals(2, resultado.getSize());
        assertEquals(5, resultado.getTotalElements());
        assertEquals("Maria Silva", resultado.getContent().getFirst().nome());
    }

    @Test
    void atualizaDadosDoClienteExistente() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        when(clienteRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(existente)).thenReturn(existente);

        ClienteRequestDTO alteracoes = new ClienteRequestDTO(
                "Maria Silva",
                "maria@example.com",
                "11999998888");

        ClienteResponseDTO atualizado = clienteService.atualizar(1L, alteracoes);

        assertEquals("Maria Silva", atualizado.nome());
        assertEquals("maria@example.com", atualizado.email());
        assertEquals("11999998888", atualizado.telefone());
        verify(clienteRepository).save(existente);
    }

    @Test
    void criaClienteComEnderecoAssociado() {
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacao -> {
            Cliente salvo = invocacao.getArgument(0);
            salvo.setId(1L);
            return salvo;
        });
        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
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
                null);

        ClienteResponseDTO criado = clienteService.criar(
                new ClienteRequestDTO("Maria Silva", "maria@example.com", "11999998888", endereco));

        assertEquals(1L, criado.id());
        assertEquals("01310-200", criado.endereco().cep());
        assertEquals("Avenida Paulista", criado.endereco().logradouro());
        assertEquals("São Paulo", criado.endereco().cidade());
        assertEquals("SP", criado.endereco().uf());
    }

    @Test
    void retornaNaoEncontradoAoBuscarClienteInexistente() {
        when(clienteRepository.findByIdAndAtivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> clienteService.buscarPorId(99L));
    }

    @Test
    void inativaClienteSemRemoveLoDoRepositorio() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.inativar(1L);

        assertFalse(resultado.ativo());
        verify(clienteRepository).save(cliente);
        verify(clienteRepository, never()).delete(cliente);
    }

    @Test
    void ativaClienteInativo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.ativar(1L);

        assertTrue(resultado.ativo());
        verify(clienteRepository).save(cliente);
    }
}
