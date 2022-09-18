package br.com.lczapparolli.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import br.com.lczapparolli.dominio.Situacao;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para o serviço de situação
 */
@QuarkusTest
public class SituacaoServiceTest {

    @Inject
    SituacaoService situacaoService;

    /**
     * Verifica o retorno do método em caso de sucesso
     */
    @Test
    public void obterSituacaoEntity_Sucesso_test() {
        var situacaoEntity = situacaoService.obterSituacaoEntity(Situacao.PROCESSANDO);

        assertTrue(situacaoEntity.isPresent());
        assertEquals(Situacao.PROCESSANDO.name(), situacaoEntity.get().getDescricao());
    }
}
