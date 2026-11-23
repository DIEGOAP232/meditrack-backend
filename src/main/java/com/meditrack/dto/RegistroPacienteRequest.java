package com.meditrack.dto;

import java.time.LocalDate;

public record RegistroPacienteRequest(
        String nombres,
        String apellidos,
        String dni,
        String correo,
        String telefono,
        String sexo,
        LocalDate fechaNacimiento,
        String contrasena
) {
}