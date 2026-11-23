package com.meditrack.repository;

import com.meditrack.dto.RegistroPacienteRequest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ApplicationScoped
public class RegistroRepository {

    @Inject
    DataSource dataSource;

    private static final String SQL_EXISTE = """
            SELECT
              (SELECT COUNT(*) FROM dbo.USUARIO WHERE dni = ?) AS dni_existe,
              (SELECT COUNT(*) FROM dbo.USUARIO WHERE correo = ?) AS correo_existe
            """;

    private static final String SQL_INSERT_USUARIO = """
            INSERT INTO dbo.USUARIO
                (dni, nombres, apellidos, telefono, correo,
                 password_hash, rol, estado, sexo)
            VALUES (?, ?, ?, ?, ?, ?, 'PACIENTE', 'ACTIVO', ?)
            """;

    private static final String SQL_SIGUIENTE_CODIGO = """
            SELECT ISNULL(MAX(CAST(SUBSTRING(codigo_paciente, 5, 10) AS INT)), 0) + 1
            FROM dbo.PACIENTE WITH (UPDLOCK, HOLDLOCK)
            WHERE codigo_paciente LIKE 'PAC-%'
            """;

    private static final String SQL_INSERT_PACIENTE = """
            INSERT INTO dbo.PACIENTE
                (id_usuario, codigo_paciente, fecha_nacimiento)
            VALUES (?, ?, ?)
            """;

    /** Devuelve "DNI", "CORREO" o null si ninguno existe. */
    public String duplicado(String dni, String correo) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_EXISTE)) {
            ps.setString(1, dni);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt("dni_existe") > 0) return "DNI";
                if (rs.getInt("correo_existe") > 0) return "CORREO";
                return null;
            }
        }
    }

    /** Inserta USUARIO + PACIENTE en una transacción. Devuelve id_usuario. */
    public int registrarPaciente(
            RegistroPacienteRequest r,
            String nombres, String apellidos, String dni,
            String correo, String telefono, String sexo,
            String passwordHash) throws SQLException {

        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try {
                int idUsuario;

                try (PreparedStatement ps = c.prepareStatement(
                        SQL_INSERT_USUARIO,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, dni);
                    ps.setString(2, nombres);
                    ps.setString(3, apellidos);
                    ps.setString(4, telefono);
                    ps.setString(5, correo);
                    ps.setString(6, passwordHash);
                    ps.setString(7, sexo);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Sin id_usuario generado.");
                        }
                        idUsuario = keys.getInt(1);
                    }
                }

                int siguiente;
                try (PreparedStatement ps =
                             c.prepareStatement(SQL_SIGUIENTE_CODIGO);
                     ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    siguiente = rs.getInt(1);
                }
                String codigo = String.format("PAC-%06d", siguiente);

                try (PreparedStatement ps =
                             c.prepareStatement(SQL_INSERT_PACIENTE)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, codigo);
                    ps.setDate(3, Date.valueOf(r.fechaNacimiento()));
                    ps.executeUpdate();
                }

                c.commit();
                return idUsuario;

            } catch (SQLException | RuntimeException e) {
                try { c.rollback(); } catch (SQLException ignored) { }
                throw e;
            } finally {
                try { c.setAutoCommit(true); } catch (SQLException ignored) { }
            }
        }
    }
}