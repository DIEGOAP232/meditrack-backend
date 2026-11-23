package com.meditrack.dto;

import com.meditrack.model.PacienteVinculado;

public record PacienteVinculadoResponse(
        Integer idPaciente,
        String codigoPaciente,
        String nombres,
        String apellidos,
        String estadoVinculo
) {

    public static PacienteVinculadoResponse desdeModelo(
            PacienteVinculado p) {
        return new PacienteVinculadoResponse(
                p.idPaciente(),
                p.codigoPaciente(),
                p.nombres(),
                p.apellidos(),
                p.estadoVinculo()
        );
    }
}