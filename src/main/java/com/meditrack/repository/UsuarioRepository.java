package com.meditrack.repository;

import com.meditrack.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository {

    @Inject
    DataSource dataSource;

    private static final String SQL_BUSCAR_POR_IDENTIFICADOR = """
            SELECT
                id_usuario,
                dni,
                nombres,
                apellidos,
                correo,
                password_hash,
                rol,
                estado
            FROM dbo.USUARIO
            WHERE correo = ? OR dni = ?
            """;

    public Optional<Usuario> buscarPorIdentificador(
            String identificador) throws SQLException {

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(
                            SQL_BUSCAR_POR_IDENTIFICADOR
                    )
        ) {

            statement.setString(1, identificador);
            statement.setString(2, identificador);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    Usuario usuario = new Usuario(
                            result.getInt("id_usuario"),
                            result.getString("dni"),
                            result.getString("nombres"),
                            result.getString("apellidos"),
                            result.getString("correo"),
                            result.getString("password_hash"),
                            result.getString("rol"),
                            result.getString("estado")
                    );

                    return Optional.of(usuario);
                }

                return Optional.empty();
            }
        }
    }
}