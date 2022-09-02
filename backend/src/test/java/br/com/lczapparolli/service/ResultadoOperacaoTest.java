package br.com.lczapparolli.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import br.com.lczapparolli.dto.ErroDTO;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para a classe de resultado da operação
 *
 * @author lczapparolli
 */
public class ResultadoOperacaoTest {

    /**
     * Verifica se a classe do construtor repassa os dados para a instância de resultado
     */
    @Test
    public void builderBuild_test() {
        var resultado = "Resultado teste";
        var erro1 = ErroDTO.builder()
                .campo("campo1")
                .mensagem("mensagem1")
                .build();
        var erro2 = ErroDTO.builder()
                .campo("campo2")
                .mensagem("mensagem2")
                .build();

        var resultadoOperacao = ResultadoOperacao.<String>builder()
                .erro(erro1)
                .erro(erro2)
                .resultado(resultado)
                .build();

        assertEquals(resultado, resultadoOperacao.getResultado());
        assertTrue(resultadoOperacao.getErros().contains(erro1));
        assertTrue(resultadoOperacao.getErros().contains(erro2));
    }

    /**
     * Verifica se o método retorna {@code true} quando há erros no resultado
     */
    @Test
    public void possuiErros_test() {
        var resultado = new ResultadoOperacao<>("Resultado teste", List.of());
        assertFalse(resultado.possuiErros());

        var erro = ErroDTO.builder()
                .campo("campo")
                .mensagem("mensagem")
                .build();
        resultado = new ResultadoOperacao<>(null, List.of(erro));
        assertTrue(resultado.possuiErros());
    }

}
