package com.meditrack.service;

import com.meditrack.dto.PacienteVinculadoResponse;
import com.meditrack.repository.TutorRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TutorService {

    @Inject
    TutorRepository tutorRepository;

    public List<PacienteVinculadoResponse> listarMisPacientes(
            String idUsuarioJwt,
            Set<String> roles) throws SQLException {

        if (idUsuarioJwt == null || idUsuarioJwt.isBlank()) {
            throw new SecurityException(
                    "No se pudo identificar al usuario autenticado."
            );
        }

        if (roles == null || !roles.contains("TUTOR")) {
            throw new SecurityException(
                    "Este usuario no tiene permiso para consultar pacientes vinculados."
            );
        }

        Integer idUsuario;

        try {
            idUsuario = Integer.valueOf(idUsuarioJwt);
        } catch (NumberFormatException e) {
            throw new SecurityException(
                    "El identificador del usuario autenticado no es válido."
            );
        }

        Integer idTutor = tutorRepository.obtenerIdTutorPorUsuario(idUsuario);

        if (idTutor == null) {
            throw new IllegalArgumentException(
                    "No se encontró el perfil del tutor."
            );
        }

        return tutorRepository.listarPacientesVinculados(idTutor)
                .stream()
                .map(PacienteVinculadoResponse::desdeModelo)
                .toList();
    }
}