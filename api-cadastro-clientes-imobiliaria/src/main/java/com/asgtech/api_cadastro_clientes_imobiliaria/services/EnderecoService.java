package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.EnderecoResponseDTO;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RequisicaoInvalidaException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.ServicoExternoException;

@Service
public class EnderecoService {

    private final RestClient restClient;

    public EnderecoService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }

    public EnderecoResponseDTO buscarPorCep(String cep) {
        String cepNormalizado = normalizarCep(cep);
        ViaCepResponse resposta;

        try {
            resposta = restClient.get()
                    .uri("/{cep}/json/", cepNormalizado)
                    .retrieve()
                    .body(ViaCepResponse.class);
        } catch (RestClientException exception) {
            throw new ServicoExternoException(
                    "Não foi possível consultar o serviço ViaCEP.",
                    exception);
        }

        if (resposta == null) {
            throw new ServicoExternoException(
                    "O serviço ViaCEP retornou uma resposta vazia.");
        }

        if (Boolean.TRUE.equals(resposta.getErro())) {
            throw new RecursoNaoEncontradoException("CEP não encontrado.");
        }

        return resposta.toEnderecoDTO();
    }

    private String normalizarCep(String cep) {
        if (cep == null || !cep.matches("\\d{5}-?\\d{3}")) {
            throw new RequisicaoInvalidaException(
                    "O CEP deve conter 8 dígitos, com ou sem hífen.");
        }
        return cep.replace("-", "");
    }

    private static class ViaCepResponse {

        public String cep;
        public String logradouro;
        public String complemento;
        public String unidade;
        public String bairro;
        public String localidade;
        public String uf;
        public Boolean erro;

        public ViaCepResponse() {
        }

        public Boolean getErro() {
            return erro;
        }

        public EnderecoResponseDTO toEnderecoDTO() {
            return new EnderecoResponseDTO(
                    cep,
                    logradouro,
                    null,
                    complemento,
                    unidade,
                    bairro,
                    localidade,
                    uf);
        }
    }
}
