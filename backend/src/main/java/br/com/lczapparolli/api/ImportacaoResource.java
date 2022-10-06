package br.com.lczapparolli.api;

import static br.com.lczapparolli.mapper.ResponseMapper.construirResposta;
import static br.com.lczapparolli.mapper.ResponseMapper.construirRespostaLista;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.SERVER_SENT_EVENTS;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.OBJECT;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.dto.ImportacaoDetalhadaDTO;
import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.dto.SituacaoImportacaoDTO;
import br.com.lczapparolli.mapper.ImportacaoMapper;
import br.com.lczapparolli.service.ImportacaoService;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
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

    @Channel("atualizacao-processamento-in")
    Multi<SituacaoImportacaoDTO> atualizacoes;

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
    @APIResponse(responseCode = "204", name = "Sucesso", description = "O arquivo foi enviado para processamento com sucesso")
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @RequestBody(name = "DadosNovaImportacao", description = "Conteúdo da requisição para iniciar a importação", content = @Content(schema = @Schema(implementation = ImportacaoNovoDTO.class)))
    public Response iniciarImportacao(@MultipartForm ImportacaoNovoDTO importacaoNovoDTO) {
        var resultado = importacaoService.iniciarImportacao(importacaoNovoDTO);
        return construirResposta(resultado);
    }

    /**
     * Cancela a execução de uma importação
     *
     * @param importacaoId Identificação da importação a ser cancelada
     * @return Retorna o resultado da operação
     */
    @POST
    @Path("/{importacaoId}/cancelar")
    @Operation(description = "Interrompe a execução de uma importação de forma assíncrona")
    @APIResponse(responseCode = "204", name = "Sucesso", description = "A solicitação de cancelamento foi processada com sucesso")
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    public Response cancelarImportacao(
            @Parameter(name = "importacaoId", description = "Identificação única da importação a ser cancelada", example = "1", required = true)
            @PathParam("importacaoId")
            Integer importacaoId) {
        var resultado = importacaoService.cancelarImportacao(importacaoId);
        return construirResposta(resultado);
    }

    /**
     * Lista as importações conforme os parâmetros fornecidos
     *
     * @param ativo Indica se devem ser retornadas as importações ativos ou finalizadas
     * @return Retorna a lista de importações encontradas
     */
    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @Operation(description = "Lista as importações de acordo com os parâmetros informados")
    @APIResponse(responseCode = "200", name = "Sucesso", description = "O sistema conseguiu realizar a busca das importações", content = @Content(schema = @Schema(type = ARRAY, implementation = ImportacaoDetalhadaDTO.class)))
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    public Response listarImportacoes(
            @Parameter(name = "ativo", description = "Indica se devem ser retornadas as importações ativos ou finalizadas. Caso não seja informado, todas as importações são retornadas", example = "true")
            @QueryParam("ativo")
            Boolean ativo) {
        var resultado = importacaoService.listarImportacoes(ativo);
        return construirRespostaLista(resultado, ImportacaoMapper::toDTO);
    }

    /**
     * Consulta uma importação de acordo com a identificação informada
     *
     * @param importacaoId Identificação da importação a ser consultada
     * @return Retorna a importação encontrada
     */
    @GET
    @Path("/{importacaoId}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @Operation(description = "Consulta uma importação de acordo com a identificação informada")
    @APIResponse(responseCode = "200", name = "Sucesso", description = "O sistema conseguiu encontrar a importação solicitada", content = @Content(schema = @Schema(type = OBJECT, implementation = ImportacaoDetalhadaDTO.class)))
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "404", name = "Registro não encontrado", description = "Não foi encontrado um registro com os dados solicitados", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    public Response obterImportacao(
            @Parameter(name = "importacaoId", description = "Identificação da importação a ser pesquisada", example = "1", required = true)
            @PathParam("importacaoId")
            Integer importacaoId
    ) {
        var resultado = importacaoService.obterImportacao(importacaoId);
        return construirResposta(resultado, ImportacaoMapper::toDTO);
    }

    /**
     * Retorna um stream com as atualizações de situação das importações
     *
     * @return Stream contínuo com os dados das importações
     */
    @GET
    @Path("/situacao")
    @Produces({ SERVER_SENT_EVENTS })
    @Operation(description = "Stream de eventos com as atualizações na situação das importações em processamento")
    @APIResponse(responseCode = "200", name = "Sucesso", description = "O fluxo de atualizações será enviado assim que disponíveis", content = @Content(schema = @Schema(type = OBJECT, implementation = SituacaoImportacaoDTO.class)))
    public Multi<SituacaoImportacaoDTO> obterAtualizacoes() {
        return atualizacoes;
    }

}
