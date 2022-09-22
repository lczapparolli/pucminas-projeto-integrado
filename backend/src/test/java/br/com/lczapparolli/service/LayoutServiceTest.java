package br.com.lczapparolli.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import br.com.lczapparolli.LayoutTestUtils;
import br.com.lczapparolli.repository.LayoutRepository;
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
    LayoutRepository layoutRepository;

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

    /**
     * Verifica o retorno normal da listagem sem filtros
     */
    @Test
    public void listarLayouts_semFiltro_test() {
        // Garante ao menos 10 layouts no banco de dados
        layoutTestUtils.inicializarListaLayout(10);

        var resultado = layoutService.listarLayouts(null);

        assertFalse(resultado.possuiErros());
        assertNotNull(resultado.getResultado());
        assertEquals(layoutRepository.count(), resultado.getResultado().size());
    }

    /**
     * Verifica se quando informado o filtro, apenas os layouts ativos são retornados
     */
    @Test
    public void listarLayouts_filtroAtivo_test() {
        var layoutInativo = layoutTestUtils.gerarNovoLayout(false);

        var resultadoAtivos = layoutService.listarLayouts(true);
        assertFalse(resultadoAtivos.possuiErros());
        assertFalse(resultadoAtivos.getResultado().contains(layoutInativo));

        var layoutAtivo = layoutTestUtils.gerarNovoLayout(true);

        var resultadoInativos = layoutService.listarLayouts(false);
        assertFalse(resultadoInativos.possuiErros());
        assertFalse(resultadoInativos.getResultado().contains(layoutAtivo));
    }

}
