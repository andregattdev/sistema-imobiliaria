package com.asgtech.api_cadastro_clientes_imobiliaria.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import com.asgtech.api_cadastro_clientes_imobiliaria.exception.GlobalExceptionHandler;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RecursoNaoEncontradoException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.RequisicaoInvalidaException;
import com.asgtech.api_cadastro_clientes_imobiliaria.exception.ServicoExternoException;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void converteRequisicaoInvalidaEmBadRequest() {
        var response = handler.handleRequisicaoInvalida(
                new RequisicaoInvalidaException("CEP inválido."),
                request());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CEP inválido.", response.getBody().getDetail());
        assertEquals("/api/enderecos/123", response.getBody().getInstance().toString());
    }

    @Test
    void converteRecursoAusenteEmNotFound() {
        var response = handler.handleRecursoNaoEncontrado(
                new RecursoNaoEncontradoException("CEP não encontrado."),
                request());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("CEP não encontrado.", response.getBody().getDetail());
    }

    @Test
    void converteFalhaDeServicoExternoEmBadGateway() {
        var response = handler.handleServicoExterno(
                new ServicoExternoException("ViaCEP indisponível."),
                request());

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("ViaCEP indisponível.", response.getBody().getDetail());
    }

    private MockHttpServletRequest request() {
        return new MockHttpServletRequest("GET", "/api/enderecos/123");
    }
}
