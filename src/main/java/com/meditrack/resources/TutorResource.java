package com.meditrack.resources;

import com.meditrack.service.TutorService;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;

@Path("/api/tutores")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
public class TutorResource {

    private static final Logger LOG = Logger.getLogger(TutorResource.class);

    @Inject
    TutorService tutorService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/mis-pacientes")
    public Response listarMisPacientes() {

        try {
            return Response.ok(
                    tutorService.listarMisPacientes(
                            jwt.getSubject(),
                            jwt.getGroups()
                    )
            ).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", e.getMessage()))
                    .build();

        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("mensaje", e.getMessage()))
                    .build();

        } catch (SQLException e) {
            LOG.error("Error SQL al consultar pacientes vinculados:", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("mensaje",
                            "Error al consultar los pacientes vinculados."))
                    .build();
        }
    }
}