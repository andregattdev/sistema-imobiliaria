package com.asgtech.api_cadastro_clientes_imobiliaria.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.asgtech.api_cadastro_clientes_imobiliaria.config.RestClientConfiguration;

class RestClientConfigurationTests {

    @Test
    void createsEnderecoServiceWithRestClientBuilder() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(RestClientConfiguration.class, EnderecoService.class);
            context.refresh();

            assertNotNull(context.getBean(EnderecoService.class));
        }
    }
}
