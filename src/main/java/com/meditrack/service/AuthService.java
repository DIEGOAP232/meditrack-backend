
package com.meditrack.service;

import com.meditrack.model.Usuario;
import com.meditrack.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    // [MODIFICACIÓN 1] Logger para diagnosticar el inicio de sesión.
    // No registra contraseñas ni hashes.
    private static final Logger LOG =
            Logger.getLogger(AuthService.class);

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    public Optional<Usuario> autenticar(
            String identificador,
            String contrasena) throws SQLException {

        // [MODIFICACIÓN 2] Validación de los datos recibidos.
        if (identificador == null || identificador.isBlank()
                || contrasena == null || contrasena.isBlank()) {

            LOG.warn("LOGIN: identificador o contraseña vacíos.");
            return Optional.empty();
        }

        // [MODIFICACIÓN 3] Buscar por DNI o correo.
        Optional<Usuario> resultado =
                usuarioRepository.buscarPorIdentificador(
                        identificador.trim()
                );

        LOG.infof(
                "LOGIN: usuario encontrado = %s",
                resultado.isPresent()
        );

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = resultado.get();

        // [MODIFICACIÓN 4] Comprobar estado y datos del usuario.
        LOG.infof(
                "LOGIN: estado = %s, rol = %s",
                usuario.estado(),
                usuario.rol()
        );

        LOG.infof(
                "LOGIN: hash presente = %s",
                usuario.passwordHash() != null
                        && !usuario.passwordHash().isBlank()
        );

        if (!"ACTIVO".equalsIgnoreCase(usuario.estado())) {
            LOG.warn("LOGIN: usuario no activo.");
            return Optional.empty();
        }

        // [MODIFICACIÓN 5] Verificar contraseña mediante BCrypt.
        boolean contrasenaValida = passwordService.verificar(
                contrasena,
                usuario.passwordHash()
        );

        LOG.infof(
                "LOGIN: contraseña válida = %s",
                contrasenaValida
        );

        if (!contrasenaValida) {
            return Optional.empty();
        }

        // Autenticación correcta.
        LOG.info("LOGIN: autenticación correcta.");

        return Optional.of(usuario);
    }
}
