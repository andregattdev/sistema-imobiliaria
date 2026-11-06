package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ClienteReferenciaDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelRequestDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RequisicaoInvalidaException;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Cliente;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Endereco;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Imovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ClienteRepository;
import com.asgtech.api_cadastro_clientes_imobiliaria.repository.ImovelRepository;

@Service
@Transactional(readOnly = true)
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final ClienteRepository clienteRepository;

    public ImovelService(
            ImovelRepository imovelRepository,
            ClienteRepository clienteRepository) {
        this.imovelRepository = imovelRepository;
        this.clienteRepository = clienteRepository;
    }

    public Page<ImovelResponseDTO> listarTodos(Pageable pageable) {
        return imovelRepository.findAllByAtivoTrueAndProprietarioAtivoTrue(pageable)
                .map(ImovelResponseDTO::from);
    }

    public Page<ImovelResponseDTO> buscarComFiltros(
            TipoImovel tipo,
            FinalidadeImovel finalidade,
            BigDecimal precoMinimo,
            BigDecimal precoMaximo,
            Integer quartos,
            String localizacao,
            StatusImovel disponibilidade,
            Pageable pageable) {
        String localizacaoNormalizada = localizacao == null || localizacao.isBlank()
                ? null
                : localizacao.trim();
        if (tipo == null && finalidade == null && precoMinimo == null && precoMaximo == null
                && quartos == null && localizacaoNormalizada == null && disponibilidade == null) {
            return listarTodos(pageable);
        }

        return imovelRepository.buscarComFiltros(
                        tipo,
                        finalidade,
                        precoMinimo,
                        precoMaximo,
                        quartos,
                        localizacaoNormalizada,
                        disponibilidade,
                        FinalidadeImovel.ALUGUEL,
                        pageable)
                .map(ImovelResponseDTO::from);
    }

    public ImovelResponseDTO buscarPorId(Long id) {
        return ImovelResponseDTO.from(buscarImovelAtivo(id));
    }

    @Transactional
    public ImovelResponseDTO criar(ImovelRequestDTO dados) {
        Imovel imovel = new Imovel();
        copiarDados(dados, imovel);
        imovel.setAtivo(true);
        imovel.setProprietario(buscarProprietario(dados.proprietario()));
        Endereco endereco = converterEndereco(dados.endereco());
        endereco.setId(null);
        imovel.setEndereco(endereco);
        return ImovelResponseDTO.from(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelResponseDTO atualizar(Long id, ImovelRequestDTO dadosAtualizados) {
        Imovel imovel = buscarImovelAtivo(id);
        copiarDados(dadosAtualizados, imovel);
        imovel.setProprietario(buscarProprietario(dadosAtualizados.proprietario()));
        atualizarEndereco(imovel, dadosAtualizados.endereco());
        return ImovelResponseDTO.from(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelResponseDTO inativar(Long id) {
        Imovel imovel = buscarImovelAtivo(id);
        imovel.setAtivo(false);
        return ImovelResponseDTO.from(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelResponseDTO ativar(Long id) {
        Imovel imovel = imovelRepository.findByIdAndProprietarioAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Imóvel ou proprietário não encontrado."));
        imovel.setAtivo(true);
        return ImovelResponseDTO.from(imovelRepository.save(imovel));
    }

    private Imovel buscarImovelAtivo(Long id) {
        return imovelRepository.findByIdAndAtivoTrueAndProprietarioAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Imóvel não encontrado."));
    }

    private void copiarDados(ImovelRequestDTO dados, Imovel imovel) {
        imovel.setTipo(dados.tipo());
        imovel.setStatus(dados.status());
        imovel.setFinalidade(dados.finalidade());
        imovel.setAreaUtil(dados.areaUtil());
        imovel.setQuartos(dados.quartos());
        imovel.setBanheiros(dados.banheiros());
        imovel.setVagasGaragem(dados.vagasGaragem());
        imovel.setPrecoVenda(dados.precoVenda());
        imovel.setPrecoAluguel(dados.precoAluguel());
        imovel.setPrecoCondominio(dados.precoCondominio());
    }

    private Cliente buscarProprietario(ClienteReferenciaDTO referencia) {
        if (referencia == null || referencia.id() == null) {
            throw new RequisicaoInvalidaException(
                    "O proprietário deve ser informado pelo ID do cliente.");
        }

        return clienteRepository.findByIdAndAtivoTrue(referencia.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Proprietário ativo não encontrado."));
    }

    private Endereco converterEndereco(EnderecoRequestDTO dados) {
        Endereco endereco = new Endereco();
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
        return endereco;
    }

    private void atualizarEndereco(Imovel imovel, EnderecoRequestDTO dadosRecebidos) {
        Endereco endereco = imovel.getEndereco();
        if (endereco == null) {
            imovel.setEndereco(converterEndereco(dadosRecebidos));
            return;
        }

        Endereco enderecoAtualizado = converterEndereco(dadosRecebidos);
        endereco.setCep(enderecoAtualizado.getCep());
        endereco.setLogradouro(enderecoAtualizado.getLogradouro());
        endereco.setNumero(enderecoAtualizado.getNumero());
        endereco.setComplemento(enderecoAtualizado.getComplemento());
        endereco.setUnidade(enderecoAtualizado.getUnidade());
        endereco.setBairro(enderecoAtualizado.getBairro());
        endereco.setLocalidade(enderecoAtualizado.getLocalidade());
        endereco.setUf(enderecoAtualizado.getUf());
        endereco.setEstado(enderecoAtualizado.getEstado());
        endereco.setRegiao(enderecoAtualizado.getRegiao());
        endereco.setIbge(enderecoAtualizado.getIbge());
        endereco.setGia(enderecoAtualizado.getGia());
        endereco.setDdd(enderecoAtualizado.getDdd());
        endereco.setSiafi(enderecoAtualizado.getSiafi());
    }
}
