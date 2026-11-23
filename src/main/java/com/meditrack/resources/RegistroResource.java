package com.meditrack.resources;

import com.meditrack.dto.RegistroPacienteRequest;
import com.meditrack.service.RegistroService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.Map;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistroResource {

    private static final Logger LOG =
            Logger.getLogger(RegistroResource.class);

    @Inject
    RegistroService registroService;

    @POST
    @Path("/registro-paciente")
    public Response registrarPaciente(RegistroPacienteRequest request) {
        try {
            registroService.registrarPaciente(request);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("mensaje",
                            "Cuenta de paciente creada correctamente."))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", e.getMessage()))
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("mensaje", e.getMessage()))
                    .build();

        } catch (SQLException e) {
            LOG.error("Error SQL al registrar paciente:", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("mensaje",
                            "Error al registrar la cuenta."))
                    .build();
        }
    }
}