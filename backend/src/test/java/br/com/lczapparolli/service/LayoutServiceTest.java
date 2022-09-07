package br.com.lczapparolli.service;

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

        var layoutOptional = layoutService.obterLayout(layoutEntity.getLayoutId());

        assertTrue(layoutOptional.isPresent());
        assertEquals(layoutEntity, layoutOptional.get());
    }

    /**
     * Verifica o retorno caso o layout não tenha sido encontrado na base
     */
    @Test
    public void obterLayout_naoEncontrado_test() {
        var layoutOptional = layoutService.obterLayout(Integer.MIN_VALUE);

        assertFalse(layoutOptional.isPresent());
    }

}
