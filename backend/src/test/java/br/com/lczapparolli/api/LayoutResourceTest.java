package br.com.lczapparolli.api;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import javax.inject.Inject;

import br.com.lczapparolli.LayoutTestUtils;
import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.erro.ResultadoOperacao;
import br.com.lczapparolli.service.LayoutService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para os endpoinAtualizando azurets de layout
 *
 * @author lczapparolli
 */
@QuarkusTest
public class LayoutResourceTest {

    @InjectMock
    LayoutService layoutService;

    @Inject
    LayoutTestUtils layoutTestUtils;

    private static final ResultadoOperacao<List<LayoutEntity>> resultadoPadrao = new ResultadoOperacao<List<LayoutEntity>>().setResultado(List.of());

    /**
     * Verifica o retorno da listagem de layouts
     */
    @Test
    public void listarLayouts_semFiltro_test() {
        doReturn(resultadoPadrao).when(layoutService).listarLayouts(any());

        given()
                .accept(APPLICATION_JSON)
            .when()
                .get("/layout")
            .then()
                .statusCode(OK.getStatusCode())
                .body("", isA(List.class))
                .body("", empty());

        verify(layoutService, times(1)).listarLayouts(isNull());
    }

    /**
     * Verifica o comportamento da aplicação quando um filtro é passado
     */
    @Test
    public void listarLayouts_comFiltro_test() {
        doReturn(resultadoPadrao).when(layoutService).listarLayouts(any());

        given()
                .accept(APPLICATION_JSON)
                .queryParam("ativo", true)
            .when()
                .get("/layout")
            .then()
                .statusCode(OK.getStatusCode());

        verify(layoutService, times(1)).listarLayouts(eq(true));
    }

    /**
     * Verifica se o retorno possui os dados transformados para um formato externo
     */
    @Test
    public void listarLayouts_transformado_test() {
        var layouts = List.of(layoutTestUtils.gerarNovoLayout(true), layoutTestUtils.gerarNovoLayout(false));
        var resultado = new ResultadoOperacao<List<LayoutEntity>>()
                .setResultado(layouts);
        doReturn(resultado).when(layoutService).listarLayouts(any());

        given()
                .accept(APPLICATION_JSON)
            .when()
                .get("/layout")
            .then()
                .statusCode(OK.getStatusCode())
                .body("", hasSize(layouts.size()))
                .body("[0].id", equalTo(layouts.get(0).getLayoutId()))
                .body("[0].descricao", equalTo(layouts.get(0).getDescricao()))
                .body("[0].ativo", equalTo(layouts.get(0).getAtivo()))
                .body("[1].id", equalTo(layouts.get(1).getLayoutId()))
                .body("[1].descricao", equalTo(layouts.get(1).getDescricao()))
                .body("[1].ativo", equalTo(layouts.get(1).getAtivo()));
    }

}
