package com.meditrack.service;

import com.meditrack.dto.RegistroPacienteRequest;
import com.meditrack.repository.RegistroRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;

@ApplicationScoped
public class RegistroService {

    @Inject
    RegistroRepository registroRepository;

    @Inject
    PasswordService passwordService;

    public int registrarPaciente(RegistroPacienteRequest request)
            throws SQLException {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Los datos de registro son obligatorios.");
        }

        String nombres = ValidacionRegistro.texto(
                request.nombres(), "nombres", 100);
        String apellidos = ValidacionRegistro.texto(
                request.apellidos(), "apellidos", 100);
        String dni = ValidacionRegistro.dni(request.dni());
        String correo = ValidacionRegistro.correo(request.correo());
        String telefono = ValidacionRegistro.telefono(request.telefono());
        String sexo = ValidacionRegistro.sexo(request.sexo());
        ValidacionRegistro.fechaNacimiento(request.fechaNacimiento());
        String contrasena = ValidacionRegistro.contrasena(
                request.contrasena());

        String duplicado = registroRepository.duplicado(dni, correo);
        if (duplicado != null) {
            throw new IllegalStateException(
                    "DNI".equals(duplicado)
                            ? "Este DNI ya está registrado."
                            : "Este correo ya está registrado.");
        }

        String hash = passwordService.generarHash(contrasena);

        try {
            return registroRepository.registrarPaciente(
                    request, nombres, apellidos, dni, correo,
                    telefono, sexo, hash);
        } catch (SQLException e) {
            // 2627 y 2601: violación de restricción única (carrera)
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new IllegalStateException(
                        "El DNI o el correo ya están registrados.");
            }
            throw e;
        }
    }
}