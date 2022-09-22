package br.com.lczapparolli.mapper;

import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_DESCONHECIDO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_DESATIVADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import br.com.lczapparolli.erro.ResultadoOperacao;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Testes para a validação da conversão da resposta
 *
 * @author lczapparolli
 */
public class ResponseMapperTest {

    /**
     * Verifica o retorno para uma resposta de sucesso
     */
    @Test
    public void construirResposta_sucesso_test() {
        var resultadoOperacao = new ResultadoOperacao<String>().setResultado("Teste");

        var response = ResponseMapper.construirResposta(resultadoOperacao);

        assertNotNull(response);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getResultado(), response.getEntity());
    }

    /**
     * Verifica o retorno para uma resposta de sucesso sem conteúdo
     */
    @Test
    public void construirResposta_vazio_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>();

        var response = ResponseMapper.construirResposta(resultadoOperacao);

        assertNotNull(response);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    /**
     * Verifica o retorno quando há erros no resultado
     */
    @Test
    public void construirResposta_erro_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>().addErro(ERRO_DESCONHECIDO);

        var response = ResponseMapper.construirRespostaErro(resultadoOperacao);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getErros(), response.getEntity());
    }

    /**
     * Verifica se o método de conversão é chamado passando o item presente
     */
    @Test
    public void construirResposta_conversao_test() {
        var spied = spyLambda(Function.class, item -> "Convertido");
        var resultadoOperacao = new ResultadoOperacao<String>().setResultado("Teste");

        var response = ResponseMapper.construirResposta(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals("Convertido", response.getEntity());
        verify(spied, times(1)).apply(eq("Teste"));
    }

    /**
     * Verifica se o método de conversão não é chamado quando o resultado está vazio
     */
    @Test
    public void construirResposta_conversaoVazio_test() {
        var spied = spyLambda(Function.class, item -> "Convertido");
        var resultadoOperacao = new ResultadoOperacao<String>();

        var response = ResponseMapper.construirResposta(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
        verify(spied, never()).apply(any());
    }

    /**
     * Verifica se o método de conversão não é chamado quando há um erro no resultado
     */
    @Test
    public void construirResposta_conversaoErro_test() {
        var spied = spyLambda(Function.class, item -> "Convertido");
        var resultadoOperacao = new ResultadoOperacao<String>().addErro(ERRO_DESCONHECIDO)
                .setResultado("Teste");

        var response = ResponseMapper.construirResposta(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getErros(), response.getEntity());
        verify(spied, never()).apply(any());
    }

    /**
     * Verifica se o método de conversão é chamado para cada um dos itens da lista
     */
    @Test
    public void construirRespostaLista_sucesso_test() {
        var spied = spyLambda(Function.class, item -> item.toString().toUpperCase());
        var captor = ArgumentCaptor.forClass(String.class);

        var valores = List.of("Teste1", "Teste2");
        var resultadoOperacao = new ResultadoOperacao<List<String>>().setResultado(valores);

        var response = ResponseMapper.construirRespostaLista(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(List.of("TESTE1", "TESTE2"), response.getEntity());

        verify(spied, times(valores.size())).apply(captor.capture());
        assertEquals(valores, captor.getAllValues());
    }

    /**
     * Verifica se o método de conversão não é chamado quando a lista está vazia
     */
    @Test
    public void construirRespostaLista_vazio_test() {
        var spied = spyLambda(Function.class, item -> item);

        var resultadoOperacao = new ResultadoOperacao<List<String>>().setResultado(new ArrayList<>());

        var response = ResponseMapper.construirRespostaLista(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(), response.getEntity());

        verify(spied, never()).apply(any());
    }

    /**
     * Verifica se o método de conversão não é chamado quando a lista está nula
     */
    @Test
    public void construirRespostaLista_nulo_test() {
        var spied = spyLambda(Function.class, item -> item);

        var resultadoOperacao = new ResultadoOperacao<List<String>>();

        var response = ResponseMapper.construirRespostaLista(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());

        verify(spied, never()).apply(any());
    }

    /**
     * Verifica se o método de conversão não é chamado quando há um erro no resultado da operação
     */
    @Test
    public void construirRespostaLista_erro_test() {
        var spied = spyLambda(Function.class, item -> item);

        var resultadoOperacao = new ResultadoOperacao<List<String>>().addErro(ERRO_DESCONHECIDO)
                .setResultado(List.of("Item1", "Item2"));

        var response = ResponseMapper.construirRespostaLista(resultadoOperacao, spied);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getErros(), response.getEntity());

        verify(spied, never()).apply(any());
    }

    /**
     * Verifica o retorno para quando houver pelo menos um erro de validação
     */
    @Test
    public void construirRespostaErro_validacao_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>().addErro(ERRO_LAYOUT_DESATIVADO)
                .addErro(ERRO_DESCONHECIDO);

        var response = ResponseMapper.construirRespostaErro(resultadoOperacao);
        assertNotNull(response);
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getErros(), response.getEntity());
    }

    /**
     * Verifica o retorno para quando houver apenas erros internos
     */
    @Test
    public void construirRespostaErro_interno_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>().addErro(ERRO_DESCONHECIDO)
                .addErro(ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO);

        var response = ResponseMapper.construirRespostaErro(resultadoOperacao);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(resultadoOperacao.getErros(), response.getEntity());
    }

    /**
     * Gera um método spy a partir de uma função Lambda
     *
     * @param lambdaType O tipo do lambda a ser gerado
     * @param lambda O método a ser espionado
     * @return Retorna um método espionado que permite fazer asserções
     * @param <T> O tipo do lambda a ser gerado
     */
    private <T> T spyLambda(final Class<T> lambdaType, final T lambda) {
        return mock(lambdaType, delegatesTo(lambda));
    }

}
