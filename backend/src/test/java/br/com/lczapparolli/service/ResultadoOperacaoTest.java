package br.com.lczapparolli.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.erro.ErroAplicacao;
import br.com.lczapparolli.erro.ResultadoOperacao;
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
     * Verifica a inclusão de um erro no resultado a partir de um item do enumerador {@link br.com.lczapparolli.erro.ErroAplicacao}
     */
    @Test
    public void builderErro_ErroAplicacao_test() {
        var resultadoOperacao = ResultadoOperacao.<Void>builder()
                .erro(ErroAplicacao.ERRO_DESCONHECIDO)
                .build();

        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getMensagem(), resultadoOperacao.getErros().get(0).getMensagem());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getCodigoErro(), resultadoOperacao.getErros().get(0).getCodigo());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.isValidacao(), resultadoOperacao.getErros().get(0).isValidacao());
        assertNull(resultadoOperacao.getErros().get(0).getCampo());
    }

    /**
     * Verifica a inclusão de um erro no resultado com a associação ao campo
     */
    @Test
    public void builderErro_ErroAplicacaoCampo_test() {
        var campo = "campo1";
        var resultadoOperacao = ResultadoOperacao.<Void>builder()
                .erro(ErroAplicacao.ERRO_DESCONHECIDO, campo)
                .build();

        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getMensagem(), resultadoOperacao.getErros().get(0).getMensagem());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getCodigoErro(), resultadoOperacao.getErros().get(0).getCodigo());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.isValidacao(), resultadoOperacao.getErros().get(0).isValidacao());
        assertEquals(campo, resultadoOperacao.getErros().get(0).getCampo());
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
