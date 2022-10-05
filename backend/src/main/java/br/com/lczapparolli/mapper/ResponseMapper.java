package br.com.lczapparolli.mapper;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.List;
import java.util.function.Function;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.erro.ResultadoOperacao;

/**
 * Métodos úteis para gerar respostas a partir dos resultados
 *
 * @author lczapparolli
 */
public final class ResponseMapper {

    /**
     * Gera uma resposta HTTP a partir de uma operação, verificando se há erros ou sucesso
     *
     * @param resultadoOperacao Resultado da operação que foi executada
     * @return Retorna a resposta HTTP com os dados presentes no resultado da operação
     */
    public static Response construirResposta(ResultadoOperacao<?> resultadoOperacao) {
        if (resultadoOperacao.possuiErros()) {
            return construirRespostaErro(resultadoOperacao);
        }

        var resultado = resultadoOperacao.getResultado();

        if (isNull(resultado)) {
            return Response.noContent().build();
        }

        return Response.ok(resultado).build();
    }

    /**
     * Gera uma resposta HTTP a partir de uma operação, verificando se há erros ou sucesso e convertendo o objeto de resultado
     *
     * @param resultadoOperacao Resultado da operação que foi executada
     * @param mapper Método para conversão do objeto de resultado
     * @return Retorna a resposta HTTP com os dados presentes no resultado da operação
     * @param <T> O tipo do objeto de resultado
     */
    public static <T> Response construirResposta(ResultadoOperacao<T> resultadoOperacao, Function<T, ?> mapper) {
        if (resultadoOperacao.possuiErros()) {
            return construirRespostaErro(resultadoOperacao);
        }

        var resultado = resultadoOperacao.getResultado();

        if (isNull(resultado)) {
            return Response.noContent().build();
        }

        return Response.ok(mapper.apply(resultado)).build();
    }

    /**
     * Gera uma resposta HTTP a partir de uma operação, verificando se há erros ou sucesso e convertendo os itens da lista
     *
     * @param resultadoOperacao Resultado da operação que foi executada
     * @param mapper Método para conversão dos objetos da lista
     * @return Retorna a resposta HTTP com os dados presentes no resultado da operação
     * @param <T> O tipo do objeto presente na lista
     */
    public static <T> Response construirRespostaLista(ResultadoOperacao<List<T>> resultadoOperacao, Function<T, ?> mapper) {
        if (resultadoOperacao.possuiErros()) {
            return construirRespostaErro(resultadoOperacao);
        }

        var resultado = resultadoOperacao.getResultado();

        if (isNull(resultado)) {
            return Response.noContent().build();
        }

        var convertido = resultado.stream().map(mapper).toList();

        return Response.ok(convertido).build();
    }

    /**
     * Constroi uma resposta de erro, identificando o código HTTP que deve ser retornado
     *
     * @param resultadoOperacao Objeto contendo os erros retornados
     * @return Retorna o objeto da resposta construído
     */
    public static Response construirRespostaErro(ResultadoOperacao<?> resultadoOperacao) {
        var status = BAD_REQUEST;

        var codigosHttp = resultadoOperacao.getErros().stream().map(ErroDTO::getCodigoHttp).distinct().toList();
        if (codigosHttp.size() == 1) {
            status = Response.Status.fromStatusCode(codigosHttp.get(0));
        }

        return Response
                .status(status)
                .entity(resultadoOperacao.getErros())
                .build();
    }

}
