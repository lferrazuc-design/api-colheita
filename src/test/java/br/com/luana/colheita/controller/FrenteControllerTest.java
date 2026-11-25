package br.com.luana.colheita.controller;

import br.com.luana.colheita.repository.FrenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de integração: sobe a aplicação inteira com banco H2 em memória
 * e faz requisições HTTP de verdade nos endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FrenteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FrenteRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveCriarEListarFrente() throws Exception {
        String json = """
                {"codigo": "F-11", "fornecedor": "Fornecedor A", "toneladasDia": 850.0}
                """;

        mvc.perform(post("/frentes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.codigo").value("F-11"))
                .andExpect(jsonPath("$.status").value("ATIVA"));

        mvc.perform(get("/frentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        String json = """
                {"codigo": "72", "fornecedor": "", "toneladasDia": -5}
                """;

        mvc.perform(post("/frentes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.codigo").exists())
                .andExpect(jsonPath("$.campos.fornecedor").exists())
                .andExpect(jsonPath("$.campos.toneladasDia").exists());
    }

    @Test
    void deveRetornar409QuandoCodigoDuplicado() throws Exception {
        String json = """
                {"codigo": "F-12", "fornecedor": "Fornecedor A", "toneladasDia": 500}
                """;
        mvc.perform(post("/frentes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        mvc.perform(post("/frentes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar404QuandoFrenteNaoExiste() throws Exception {
        mvc.perform(get("/frentes/999"))
                .andExpect(status().isNotFound());
    }
}
