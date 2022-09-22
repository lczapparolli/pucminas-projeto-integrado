package br.com.lczapparolli.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.dto.ErroDTO;
import br.com.lczapparolli.dto.LayoutDTO;
import br.com.lczapparolli.mapper.LayoutMapper;
import br.com.lczapparolli.mapper.ResponseMapper;
import br.com.lczapparolli.service.LayoutService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Endpoints para manipulação dos layouts
 *
 * @author lczapparolli
 */
@Path("/layout")
@Produces(APPLICATION_JSON)
@Tag(name = "Layout", description = "Endpoints para a manipulação dos layouts")
public class LayoutResource {

    @Inject
    LayoutService layoutService;

    /**
     * Lista os layouts de acordo com o filtro informado
     *
     * @param ativo Filtro da situação dos layouts
     * @return Retorna a resposta HTTP com os layouts encontrados
     */
    @GET
    @Operation(description = "Lista os layouts cadastrados")
    @APIResponse(responseCode = "200", name = "Sucesso", description = "Foi possível obter a lista de layouts", content = @Content(schema = @Schema(type = ARRAY, implementation = LayoutDTO.class)))
    @APIResponse(responseCode = "400", name = "Dados inválidos", description = "Os dados informados na requisição não são válidos", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    @APIResponse(responseCode = "500", name = "Erro interno", description = "Ocorreu um erro internamente que impediu o processamento da solicitação", content = @Content(schema = @Schema(type = ARRAY, implementation = ErroDTO.class)))
    public Response listarLayouts(
            @Parameter(description = "Indica se devem ser retornados apenas os layouts ativos ou inativos. Se não informado, todos os layouts são retornados", example = "true")
            @QueryParam("ativo")
            Boolean ativo) {
        var resultado = layoutService.listarLayouts(ativo);
        return ResponseMapper.construirRespostaLista(resultado, LayoutMapper::toDTO);
    }

}
