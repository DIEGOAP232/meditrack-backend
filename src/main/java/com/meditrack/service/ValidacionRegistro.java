package com.meditrack.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;

public final class ValidacionRegistro {

    private static final Pattern DNI = Pattern.compile("\\d{8}");
    private static final Pattern TELEFONO = Pattern.compile("\\d{9}");
    private static final Pattern CORREO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Set<String> SEXOS =
            Set.of("MASCULINO", "FEMENINO");

    private ValidacionRegistro() {
    }

    public static String texto(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " supera los "
                            + max + " caracteres.");
        }
        return limpio;
    }

    public static String dni(String valor) {
        String v = texto(valor, "DNI", 8);
        if (!DNI.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "El DNI debe tener 8 dígitos.");
        }
        return v;
    }

    public static String correo(String valor) {
        String v = texto(valor, "correo", 150).toLowerCase();
        if (!CORREO.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "El correo no es válido.");
        }
        return v;
    }

    public static String telefono(String valor) {
        String v = texto(valor, "teléfono", 15);
        if (!TELEFONO.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "El teléfono debe tener 9 dígitos.");
        }
        return v;
    }

    public static String sexo(String valor) {
        String v = texto(valor, "sexo", 20).toUpperCase();
        if (!SEXOS.contains(v)) {
            throw new IllegalArgumentException(
                    "El sexo debe ser MASCULINO o FEMENINO.");
        }
        return v;
    }

    public static LocalDate fechaNacimiento(LocalDate valor) {
        if (valor == null) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento es obligatoria.");
        }
        if (!valor.isBefore(LocalDate.now())
                || valor.isBefore(LocalDate.now().minusYears(120))) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento no es válida.");
        }
        return valor;
    }

    // Regla única: 8+ caracteres, mayúscula, minúscula y número.
    public static String contrasena(String valor) {
        if (valor == null
                || valor.length() < 8
                || valor.length() > 72
                || !valor.matches(".*[A-Z].*")
                || !valor.matches(".*[a-z].*")
                || !valor.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener entre 8 y 72 caracteres, "
                            + "con mayúscula, minúscula y número.");
        }
        return valor;
    }
}

