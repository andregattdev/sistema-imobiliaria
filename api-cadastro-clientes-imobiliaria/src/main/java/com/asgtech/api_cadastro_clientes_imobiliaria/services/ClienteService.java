package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Endereco;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAllByAtivoTrue(pageable)
                .map(ClienteResponseDTO::from);
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        return ClienteResponseDTO.from(buscarClienteAtivo(id));
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dados) {
        Cliente cliente = new Cliente();
        cliente.setNome(dados.nome());
        cliente.setEmail(dados.email());
        cliente.setTelefone(dados.telefone());
        atualizarEndereco(cliente, dados.endereco());
        cliente.setAtivo(true);
        return ClienteResponseDTO.from(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dadosAtualizados) {
        Cliente cliente = buscarClienteAtivo(id);
        cliente.setNome(dadosAtualizados.nome());
        cliente.setEmail(dadosAtualizados.email());
        cliente.setTelefone(dadosAtualizados.telefone());
        atualizarEndereco(cliente, dadosAtualizados.endereco());
        return ClienteResponseDTO.from(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO inativar(Long id) {
        Cliente cliente = buscarClienteAtivo(id);
        cliente.setAtivo(false);
        return ClienteResponseDTO.from(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO ativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        cliente.setAtivo(true);
        return ClienteResponseDTO.from(clienteRepository.save(cliente));
    }

    private Cliente buscarClienteAtivo(Long id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
    }

    private void atualizarEndereco(Cliente cliente, EnderecoRequestDTO dados) {
        if (dados == null) {
            return;
        }

        Endereco endereco = cliente.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
            cliente.setEndereco(endereco);
        }
        endereco.setCep(dados.cep());
        endereco.setLogradouro(dados.logradouro());
        endereco.setNumero(dados.numero());
        endereco.setComplemento(dados.complemento());
        endereco.setUnidade(dados.unidade());
        endereco.setBairro(dados.bairro());
        endereco.setLocalidade(dados.cidade());
        endereco.setUf(dados.uf());
        endereco.setEstado(dados.estado());
        endereco.setRegiao(dados.regiao());
        endereco.setIbge(dados.ibge());
        endereco.setGia(dados.gia());
        endereco.setDdd(dados.ddd());
        endereco.setSiafi(dados.siafi());
    }
}
