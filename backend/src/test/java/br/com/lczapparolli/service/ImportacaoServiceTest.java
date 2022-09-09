package br.com.lczapparolli.service;

import static br.com.lczapparolli.ValidacaoTestUtils.validarErro;
import static br.com.lczapparolli.dominio.Situacao.PROCESSANDO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_CAMPO_NAO_INFORMADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_IMPORTACAO_NAO_INFORMADA;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_DESATIVADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;

import br.com.lczapparolli.LayoutTestUtils;
import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.mock.FileUploadMock;
import br.com.lczapparolli.repository.ImportacaoRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Testes unitários para o serviço de importação
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImportacaoServiceTest {

    @Inject
    ImportacaoService importacaoService;

    @Inject
    LayoutTestUtils layoutTestUtils;

    @Inject
    ImportacaoRepository importacaoRepository;

    private LayoutEntity layout;
    private AtomicInteger contador;

    @BeforeAll
    public void inicializar() {
        layout = layoutTestUtils.gerarNovoLayout();
        contador = new AtomicInteger(0);
    }

    /**
     * Verifica o fluxo de sucesso da iniciação de uma importação
     */
    @Test
    public void iniciarImportacao_sucesso_test() {
        var importacoes = importacaoRepository.count();
        var importacaoDTO = gerarImportacaoNovoDTO();

        var resultado = importacaoService.iniciarImportacao(importacaoDTO);

        assertFalse(resultado.possuiErros());
        assertEquals(importacoes + 1, importacaoRepository.count());

        var importacaoEntity = importacaoRepository
                .find("layout = ?1 and nomeArquivo = ?2", layout, importacaoDTO.getNomeArquivo())
                .firstResultOptional();
        assertTrue(importacaoEntity.isPresent());
        assertEquals(importacaoDTO.getNomeArquivo(), importacaoEntity.get().getNomeArquivo());
        assertEquals(importacaoDTO.getLayoutId(), importacaoEntity.get().getLayout().getLayoutId());
        assertEquals(PROCESSANDO.name(), importacaoEntity.get().getSituacao().getDescricao());
        assertNotNull(importacaoEntity.get().getInicioImportacao());
        assertNull(importacaoEntity.get().getFimImportacao());
    }

    /**
     * Verifica o fluxo caso os dados da importação não sejam informados
     */
    @Test
    public void iniciarImportacao_dadosNaoInformados_test() {
        var resultado = importacaoService.iniciarImportacao(null);

        validarErro(resultado, ERRO_IMPORTACAO_NAO_INFORMADA, null);
    }

    /**
     * Verifica o comportamento caso o layout da importação não seja informado
     */
    @Test
    public void iniciarImportacao_layoutNaoInformado_test() {
        var importacaoDTO = gerarImportacaoNovoDTO();

        // Verifica com o valor zerado
        importacaoDTO.setLayoutId(0);
        var resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_CAMPO_NAO_INFORMADO, "layoutId");

        // Verifica com o valor nulo
        importacaoDTO.setLayoutId(null);
        resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_CAMPO_NAO_INFORMADO, "layoutId");
    }

    /**
     * Verifica o comportamento caso o layout informado não corresponda a um dos registros existentes
     */
    @Test
    public void iniciarImportacao_layoutInvalido_test() {
        var importacaoDTO = gerarImportacaoNovoDTO();

        importacaoDTO.setLayoutId(Integer.MIN_VALUE);
        var resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_LAYOUT_NAO_ENCONTRADO, "layoutId");
    }

    /**
     * Verifica o comportamento caso o layout informado não esteja ativo
     */
    @Test
    public void iniciarImportacao_layoutDesativado_test() {
        // Insere um novo layout desativado
        var layoutDesativado = layoutTestUtils.gerarNovoLayout(false);

        var importacaoDTO = gerarImportacaoNovoDTO();

        importacaoDTO.setLayoutId(layoutDesativado.getLayoutId());
        var resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_LAYOUT_DESATIVADO, "layoutId");
    }

    /**
     * Verifica o comportamento caso o arquivo a ser importado não tenha sido informado
     */
    @Test
    public void iniciarImportacao_arquivoNaoInformado_test() {
        var importacaoDTO = gerarImportacaoNovoDTO();

        importacaoDTO.setArquivo(null);
        var resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_CAMPO_NAO_INFORMADO, "arquivo");
    }

    /**
     * Verifica o comportamento caso o nome do arquivo a ser importado não tenha sido informado
     */
    @Test
    public void iniciarImportacao_nomeArquivoNaoInformado_test() {
        var importacaoDTO = gerarImportacaoNovoDTO();

        // Verifica com o valor zerado
        importacaoDTO.setNomeArquivo("");
        var resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_CAMPO_NAO_INFORMADO, "nomeArquivo");

        // Verifica com o valor nulo
        importacaoDTO.setNomeArquivo(null);
        resultado = importacaoService.iniciarImportacao(importacaoDTO);
        validarErro(resultado, ERRO_CAMPO_NAO_INFORMADO, "nomeArquivo");
    }

    /**
     * Gera um novo DTO com os dados necessários para o teste
     *
     * @return Retona a instância criada
     */
    private ImportacaoNovoDTO gerarImportacaoNovoDTO() {
        var indice = contador.incrementAndGet();
        var nomeArquivo = String.format("teste_%05d.txt", indice);
        return ImportacaoNovoDTO.builder()
                .arquivo(new FileUploadMock("arquivo", nomeArquivo))
                .nomeArquivo(nomeArquivo)
                .layoutId(layout.getLayoutId())
                .build();
    }

}
