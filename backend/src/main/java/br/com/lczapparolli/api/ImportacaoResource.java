package br.com.lczapparolli.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.erro.ResultadoOperacao;
import br.com.lczapparolli.service.ImportacaoService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.MultipartForm;

/**
 * Endpoints para a manipulação das importações
 *
 * @author lczapparolli
 */
@Path("/importacao")
@Tag(name = "Importação", description = "Endpoints para a manipulação das importações")
public class ImportacaoResource {

    @Inject
    ImportacaoService importacaoService;

    /**
     * Cria uma importação com os dados fornecidos
     *
     * @param importacaoNovoDTO Dados da importação
     * @return Retorna o resultado do processo
     */
    @POST
    @Consumes({MULTIPART_FORM_DATA, APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @Operation(description = "Realiza o upload de um arquivo e inicia o processo de importação")
    @RequestBody(name = "DadosNovaImportacao", description = "Conteúdo da requisição para iniciar a importação")
    public Response iniciarImportacao(@MultipartForm ImportacaoNovoDTO importacaoNovoDTO) {
        var resultado = importacaoService.iniciarImportacao(importacaoNovoDTO);
        if (resultado.possuiErros()) {
            return construirRespostaErro(resultado);
        }

        return Response.noContent().build();
    }

    /**
     * Constroi uma resposta de erro, identificando o código HTTP que deve ser retornado
     *
     * @param resultadoOperacao Objeto contendo os erros retornados
     * @return Retorna o objeto da resposta construído
     */
    private Response construirRespostaErro(ResultadoOperacao<?> resultadoOperacao) {
        var possuiErroValidacao = resultadoOperacao.getErros().stream().anyMatch(ErroDTO::isValidacao);
        return Response
                .status(possuiErroValidacao ? BAD_REQUEST : INTERNAL_SERVER_ERROR)
                .entity(resultadoOperacao.getErros())
                .build();
    }
}
