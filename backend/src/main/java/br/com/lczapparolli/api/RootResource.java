package br.com.lczapparolli.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @GET
    @Produces(APPLICATION_JSON)
    public Response validarServico() {
        return Response.ok(Map.of("working", true)).build();
    }
}
