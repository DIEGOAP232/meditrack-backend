package com.meditrack.model;

public record PacienteVinculado(
        Integer idPaciente,
        String codigoPaciente,
        String nombres,
        String apellidos,
        String estadoVinculo
) {
}