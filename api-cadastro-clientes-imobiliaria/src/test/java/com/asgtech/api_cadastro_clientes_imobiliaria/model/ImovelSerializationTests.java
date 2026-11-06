package com.asgtech.api_cadastro_clientes_imobiliaria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.asgtech.api_cadastro_clientes_imobiliaria.dto.ImovelResponseDTO;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

class ImovelSerializationTests {

    @Test
    void serializaImovelComEnderecoEProprietarioSemRecursao() {
        Endereco endereco = new Endereco();
        endereco.setCep("01310-200");
        endereco.setLogradouro("Avenida Paulista");
        endereco.setNumero("1000");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");

        Cliente proprietario = new Cliente();
        proprietario.setId(789L);
        proprietario.setNome("João Silva");

        Imovel imovel = new Imovel();
        imovel.setTipo(TipoImovel.APARTAMENTO);
        imovel.setEndereco(endereco);
        imovel.setProprietario(proprietario);

        JsonNode json = JsonMapper.builder().build().valueToTree(imovel);

        assertEquals("APARTAMENTO", json.get("tipo").asString());
        assertEquals("01310-200", json.get("endereco").get("cep").asString());
        assertEquals("São Paulo", json.get("endereco").get("cidade").asString());
        assertFalse(json.get("endereco").has("imovel"));
        assertEquals("João Silva", json.get("proprietario").get("nome").asString());
        assertFalse(json.get("proprietario").has("imoveis"));
    }

    @Test
    void dtoDoImovelNaoExpoeDadosDeContatoNemMetadadosInternos() {
        Endereco endereco = new Endereco();
        endereco.setId(12L);
        endereco.setCep("01310-200");
        endereco.setLocalidade("São Paulo");
        endereco.setIbge("3550308");

        Cliente proprietario = new Cliente();
        proprietario.setId(789L);
        proprietario.setNome("João Silva");
        proprietario.setEmail("joao@example.com");
        proprietario.setTelefone("11999998888");

        Imovel imovel = new Imovel();
        imovel.setEndereco(endereco);
        imovel.setProprietario(proprietario);

        JsonNode json = JsonMapper.builder().build()
                .valueToTree(ImovelResponseDTO.from(imovel));

        assertEquals("João Silva", json.get("proprietario").get("nome").asString());
        assertFalse(json.get("proprietario").has("email"));
        assertFalse(json.get("proprietario").has("telefone"));
        assertFalse(json.get("endereco").has("id"));
        assertFalse(json.get("endereco").has("ibge"));
    }
}
