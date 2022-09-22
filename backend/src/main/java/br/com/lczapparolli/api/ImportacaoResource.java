package br.com.lczapparolli.api;

import static br.com.lczapparolli.mapper.ResponseMapper.construirResposta;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.service.ImportacaoService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
    @APIResponse(responseCode = "204", name = "Sucesso", description = "O arquivo foi enviado para processamento com sucesso")
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @RequestBody(name = "DadosNovaImportacao", description = "Conteúdo da requisição para iniciar a importação", content = @Content(schema = @Schema(implementation = ImportacaoNovoDTO.class)))
    public Response iniciarImportacao(@MultipartForm ImportacaoNovoDTO importacaoNovoDTO) {
        var resultado = importacaoService.iniciarImportacao(importacaoNovoDTO);
        return construirResposta(resultado);
    }
}
