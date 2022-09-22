package br.com.lczapparolli.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.lczapparolli.mapper.LayoutMapper;
import br.com.lczapparolli.mapper.ResponseMapper;
import br.com.lczapparolli.service.LayoutService;

/**
 * Endpoints para manipulação dos layouts
 *
 * @author lczapparolli
 */
@Path("/layout")
@Produces(APPLICATION_JSON)
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
    public Response listarLayouts(@QueryParam("ativo") Boolean ativo) {
        var resultado = layoutService.listarLayouts(ativo);
        return ResponseMapper.construirRespostaLista(resultado, LayoutMapper::toDTO);
    }

}
