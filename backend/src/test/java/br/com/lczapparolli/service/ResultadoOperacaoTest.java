package br.com.lczapparolli.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Verifica a inclusão de um erro no resultado a partir de um item do enumerador {@link br.com.lczapparolli.erro.ErroAplicacao}
     */
    @Test
    public void addErro_ErroAplicacao_test() {
        var resultadoOperacao = new ResultadoOperacao<String>()
                .addErro(ErroAplicacao.ERRO_DESCONHECIDO);

        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getMensagem(), resultadoOperacao.getErros().get(0).getMensagem());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.getCodigoErro(), resultadoOperacao.getErros().get(0).getCodigo());
        assertEquals(ErroAplicacao.ERRO_DESCONHECIDO.isValidacao(), resultadoOperacao.getErros().get(0).isValidacao());
        assertNull(resultadoOperacao.getErros().get(0).getCampo());
    }

    /**
     * Verifica a inclusão de um erro no resultado com a associação ao campo
     */
    @Test
    public void addErro_ErroAplicacaoCampo_test() {
        var campo = "campo1";
        var resultadoOperacao = new ResultadoOperacao<String>()
                .addErro(ErroAplicacao.ERRO_DESCONHECIDO, campo);

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
        var resultado = new ResultadoOperacao<>("Resultado teste");
        assertFalse(resultado.possuiErros());

        var erro = ErroDTO.builder()
                .campo("campo")
                .mensagem("mensagem")
                .build();
        resultado = new ResultadoOperacao<String>().addErro(erro);
        assertTrue(resultado.possuiErros());
    }

}
