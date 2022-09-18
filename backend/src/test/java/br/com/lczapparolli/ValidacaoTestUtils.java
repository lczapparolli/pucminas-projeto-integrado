package br.com.lczapparolli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.lczapparolli.erro.ErroAplicacao;
import br.com.lczapparolli.erro.ResultadoOperacao;

/**
 * Métodos úteis para validação dos critérios de teste
 *
 * @author lczapparolli
 */
public final class ValidacaoTestUtils {

    /**
     * Verifica se o resultado possui o erro esperado
     *
     * @param resultadoOperacao Resultado da operação testada
     * @param erroEsperado Erro que é esperado na lista dos possíveis erros
     * @param campoEsperado Identificação do campo vinculado ao erro
     */
    public static void validarErro(ResultadoOperacao<?> resultadoOperacao, ErroAplicacao erroEsperado, String campoEsperado) {
        assertTrue(resultadoOperacao.possuiErros());
        var erroOpcional = resultadoOperacao.getErros().stream()
                .filter(item -> item.getCodigo().equals(erroEsperado.getCodigoErro()))
                .findAny();

        assertTrue(erroOpcional.isPresent(), String.format("O erro esperado '%s' não foi encontrado", erroEsperado.getCodigoErro()));
        assertEquals(campoEsperado, erroOpcional.get().getCampo());
    }

}
