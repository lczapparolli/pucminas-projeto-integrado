package br.com.lczapparolli.api;

import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_CAMPO_NAO_INFORMADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_DESCONHECIDO;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;

import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.erro.ResultadoOperacao;
import br.com.lczapparolli.service.ImportacaoService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Testes unitários para o endpoint de importações
 */
@QuarkusTest
public class ImportacaoResourceTest {

    @InjectMock
    ImportacaoService importacaoService;

    /**
     * Verifica o retorno do endpoint de importação em casos de sucesso
     */
    @Test
    public void iniciarImportacao_Sucesso_test() {
        doReturn(new ResultadoOperacao<Void>()).when(importacaoService).iniciarImportacao(any());
        var layoutId = 1;
        var nomeArquivo = "teste.txt";
        var conteudoArquivo = "teste";

        given()
            .multiPart("layoutId", layoutId)
            .multiPart("nomeArquivo", nomeArquivo)
            .multiPart("arquivo", nomeArquivo, new ByteArrayInputStream(conteudoArquivo.getBytes()))
            .contentType(MULTIPART_FORM_DATA)
            .when()
                .post("/importacao")
            .then()
                .statusCode(NO_CONTENT.getStatusCode());

        var argumentCaptor = ArgumentCaptor.forClass(ImportacaoNovoDTO.class);
        verify(importacaoService, times(1)).iniciarImportacao(argumentCaptor.capture());

        assertEquals(nomeArquivo, argumentCaptor.getValue().getNomeArquivo());
        assertEquals(layoutId, argumentCaptor.getValue().getLayoutId());
        assertEquals(nomeArquivo, argumentCaptor.getValue().getArquivo().fileName());
    }

    /**
     * Verifica o retorno do endpoint no caso de um erro de validação dos dados
     */
    @Test
    public void iniciarImportacao_ErroValidacao_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>()
                .addErro(ERRO_CAMPO_NAO_INFORMADO, "layoutId")
                .addErro(ERRO_CAMPO_NAO_INFORMADO, "layoutId");
        doReturn(resultadoOperacao).when(importacaoService).iniciarImportacao(any());

        given()
                .contentType(MULTIPART_FORM_DATA)
            .when()
                .post("/importacao")
            .then()
                .statusCode(BAD_REQUEST.getStatusCode())
                .body("size()", is(resultadoOperacao.getErros().size()))
                .body("[0].campo", is(resultadoOperacao.getErros().get(0).getCampo()))
                .body("[0].mensagem", is(resultadoOperacao.getErros().get(0).getMensagem()))
                .body("[0].codigo", is(resultadoOperacao.getErros().get(0).getCodigo()))
                .body("[0].codigoHttp", is(resultadoOperacao.getErros().get(0).getCodigoHttp()));
    }

    /**
     * Verifica o retorno do endpoint no caso de um erro de processamento interno
     */
    @Test
    public void iniciarImportacao_ErroProcessamento_test() {
        var resultadoOperacao = new ResultadoOperacao<Void>()
                .addErro(ERRO_DESCONHECIDO, null);
        doReturn(resultadoOperacao).when(importacaoService).iniciarImportacao(any());

        given()
                .contentType(MULTIPART_FORM_DATA)
            .when()
                .post("/importacao")
            .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body("size()", is(resultadoOperacao.getErros().size()))
                .body("[0].campo", is(resultadoOperacao.getErros().get(0).getCampo()))
                .body("[0].mensagem", is(resultadoOperacao.getErros().get(0).getMensagem()))
                .body("[0].codigo", is(resultadoOperacao.getErros().get(0).getCodigo()))
                .body("[0].codigoHttp", is(resultadoOperacao.getErros().get(0).getCodigoHttp()));
    }
}
