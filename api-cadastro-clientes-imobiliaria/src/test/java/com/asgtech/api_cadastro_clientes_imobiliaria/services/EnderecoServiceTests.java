package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RequisicaoInvalidaException;

class EnderecoServiceTests {

    private MockRestServiceServer server;
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        server = MockRestServiceServer.bindTo(restClientBuilder).build();
        enderecoService = new EnderecoService(restClientBuilder);
    }

    @Test
    void buscaEnderecoPorCep() {
        server.expect(requestTo("https://viacep.com.br/ws/01001000/json/"))
                .andRespond(withSuccess("""
                        {
                          "cep": "01001-000",
                          "logradouro": "Praça da Sé",
                          "complemento": "lado ímpar",
                          "bairro": "Sé",
                          "localidade": "São Paulo",
                          "uf": "SP",
                          "estado": "São Paulo",
                          "regiao": "Sudeste",
                          "ibge": "3550308",
                          "ddd": "11"
                        }
                        """, MediaType.APPLICATION_JSON));

        var endereco = enderecoService.buscarPorCep("01001-000");

        assertEquals("01001-000", endereco.cep());
        assertEquals("Praça da Sé", endereco.logradouro());
        assertEquals("São Paulo", endereco.cidade());
        assertEquals("SP", endereco.uf());
        server.verify();
    }

    @Test
    void rejeitaCepComFormatoInvalidoSemConsultarApi() {
        assertThrows(
                RequisicaoInvalidaException.class,
                () -> enderecoService.buscarPorCep("1234"));

        server.verify();
    }

    @Test
    void retornaNaoEncontradoQuandoViaCepNaoLocalizaCep() {
        server.expect(requestTo("https://viacep.com.br/ws/99999999/json/"))
                .andRespond(withSuccess("{\"erro\": true}", MediaType.APPLICATION_JSON));

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> enderecoService.buscarPorCep("99999999"));

        server.verify();
    }
}
