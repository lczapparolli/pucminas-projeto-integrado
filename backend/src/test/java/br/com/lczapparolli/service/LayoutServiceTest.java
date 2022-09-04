package br.com.lczapparolli.service;

import static br.com.lczapparolli.ValidacaoTestUtils.validarErro;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import br.com.lczapparolli.LayoutTestUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para o serviço de layout
 *
 * @author lczapparolli
 */
@QuarkusTest
public class LayoutServiceTest {

    @Inject
    LayoutTestUtils layoutTestUtils;

    @Inject
    LayoutService layoutService;

    /**
     * Verifica o fluxo normal para a consulta de layouts
     */
    @Test
    public void obterLayout_sucesso_test() {
        var layoutEntity = layoutTestUtils.gerarNovoLayout();

        var resultadoOperacao = layoutService.obterLayout(layoutEntity.getLayoutId());

        assertFalse(resultadoOperacao.possuiErros());
        assertEquals(layoutEntity, resultadoOperacao.getResultado());
    }

    /**
     * Verifica o retorno caso o layout não tenha sido encontrado na base
     */
    @Test
    public void obterLayout_naoEncontrado_test() {
        var resultadoOperacao = layoutService.obterLayout(Integer.MIN_VALUE);

        assertTrue(resultadoOperacao.possuiErros());
        validarErro(resultadoOperacao, ERRO_LAYOUT_NAO_ENCONTRADO, null);
    }

}
