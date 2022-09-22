package br.com.lczapparolli.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import br.com.lczapparolli.LayoutTestUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * Validação do mapeamento de layout
 *
 * @author lczapparolli
 */
@QuarkusTest
public class LayoutMapperTest {

    @Inject
    LayoutTestUtils layoutTestUtils;

    /**
     * Verifica o retorno de sucesso do mapeamento
     */
    @Test
    public void toDTO_sucesso_test() {
        var layoutEntity = layoutTestUtils.gerarNovoLayout();
        var resultado = LayoutMapper.toDTO(layoutEntity);

        assertNotNull(resultado);
        assertEquals(layoutEntity.getLayoutId(), resultado.getId());
        assertEquals(layoutEntity.getDescricao(), resultado.getDescricao());
        assertEquals(layoutEntity.getAtivo(), resultado.getAtivo());
    }

    /**
     * Verifica o retorno do método caso o objeto esteja nulo
     */
    @Test
    public void toDTO_nulo_test() {
        var resultado = LayoutMapper.toDTO(null);
        assertNull(resultado);
    }
}
