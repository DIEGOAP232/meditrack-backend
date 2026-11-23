package com.meditrack.repository;

import com.meditrack.model.PacienteVinculado;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TutorRepository {

    @Inject
    DataSource dataSource;

    private static final String SQL_ID_TUTOR = """
            SELECT id_tutor
            FROM dbo.TUTOR
            WHERE id_usuario = ?
            """;

    private static final String SQL_PACIENTES_VINCULADOS = """
            SELECT DISTINCT p.id_paciente,
                   p.codigo_paciente,
                   u.nombres,
                   u.apellidos,
                   tp.estado
            FROM dbo.TUTOR_PACIENTE tp
            INNER JOIN dbo.PACIENTE p
                ON p.id_paciente = tp.id_paciente
            INNER JOIN dbo.USUARIO u
                ON u.id_usuario = p.id_usuario
            WHERE tp.id_tutor = ?
              AND tp.estado = 'ACEPTADA'
              AND u.estado = 'ACTIVO'
            ORDER BY u.apellidos, u.nombres
            """;

    public Integer obtenerIdTutorPorUsuario(Integer idUsuario)
            throws SQLException {

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(SQL_ID_TUTOR)
        ) {
            statement.setInt(1, idUsuario);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt("id_tutor") : null;
            }
        }
    }

    public List<PacienteVinculado> listarPacientesVinculados(
            Integer idTutor) throws SQLException {

        List<PacienteVinculado> pacientes = new ArrayList<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(SQL_PACIENTES_VINCULADOS)
        ) {
            statement.setInt(1, idTutor);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    pacientes.add(new PacienteVinculado(
                            result.getInt("id_paciente"),
                            result.getString("codigo_paciente"),
                            result.getString("nombres"),
                            result.getString("apellidos"),
                            result.getString("estado")
                    ));
                }
            }
        }

        return pacientes;
    }
}