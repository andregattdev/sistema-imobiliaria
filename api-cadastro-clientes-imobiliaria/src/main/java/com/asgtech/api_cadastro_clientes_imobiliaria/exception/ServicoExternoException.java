package com.asgtech.api_cadastro_clientes_imobiliaria.exception;

public class ServicoExternoException extends RuntimeException {

    public ServicoExternoException(String message) {
        super(message);
    }

    public ServicoExternoException(String message, Throwable cause) {
        super(message, cause);
    }
}
