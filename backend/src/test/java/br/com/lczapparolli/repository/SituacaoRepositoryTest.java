package br.com.lczapparolli.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import br.com.lczapparolli.dominio.Situacao;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para o repositório de situações
 */
@QuarkusTest
public class SituacaoRepositoryTest {

    @Inject
    SituacaoRepository situacaoRepository;

    /**
     * Verifica o retorno da consulta caso a situação seja encontrada
     */
    @Test
    public void encontrarPorDescricao_Sucesso_test() {
        var situacaoEntity = situacaoRepository.encontrarPorDescricao(Situacao.PROCESSANDO.name());

        assertTrue(situacaoEntity.isPresent());
        assertEquals(Situacao.PROCESSANDO.name(), situacaoEntity.get().getDescricao());
    }

    /**
     * Verifica o retorno da consulta caso a situação não seja encontrada
     */
    @Test
    public void encontrarPorDescricao_NaoEncontrado_test() {
        var situacaoEntity = situacaoRepository.encontrarPorDescricao("SITUACAO INVALIDA");

        assertFalse(situacaoEntity.isPresent());
    }

}
