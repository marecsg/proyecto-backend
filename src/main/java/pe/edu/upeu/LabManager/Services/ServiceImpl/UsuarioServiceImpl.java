package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Entity.Enums.Roles;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario obtenerUsuarioAutenticado() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no autenticado"
            );
        }

        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ));
    }

    @Override
    public Usuario listById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id
                ));

        Usuario autenticado = obtenerUsuarioAutenticado();

        if (!autenticado.getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes consultar los datos de otro usuario"
            );
        }

        return usuario;
    }

    @Override
    public Usuario findByCodigoInstitucional(String codigoInstitucional) {
        Usuario usuario = usuarioRepository
                .findByCodigoInstitucional(codigoInstitucional)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con el código: " + codigoInstitucional
                ));

        Usuario autenticado = obtenerUsuarioAutenticado();

        if (!autenticado.getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes consultar los datos de otro usuario"
            );
        }

        return usuario;
    }

    @Override
    public Usuario verificarEncargado(Integer idAmbiente) {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (usuario.getRol() == null ||
                usuario.getRol().getNombre() != Roles.ENCARGADO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un encargado puede realizar esta acción"
            );
        }

        if (usuario.getAmbiente() == null ||
                !usuario.getAmbiente().getIdAmbiente().equals(idAmbiente)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No eres encargado de este laboratorio"
            );
        }

        return usuario;
    }

    @Override
    public Usuario guardarFcmToken(String token) {
        if (token == null || token.isBlank() || token.length() > 2048) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token FCM inválido"
            );
        }

        Usuario usuario = obtenerUsuarioAutenticado();

        usuario.setFcmToken(token.trim());

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario verificarEstudiante(Integer idEstudiante) {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (usuario.getRol() == null ||
                usuario.getRol().getNombre() != Roles.ESTUDIANTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un estudiante puede realizar esta acción"
            );
        }

        if (!usuario.getIdUsuario().equals(idEstudiante)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes modificar solicitudes de otro estudiante"
            );
        }

        return usuario;
    }

    @Override
    public Usuario verificarAcceso(Integer idEstudiante, Integer idAmbiente) {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no autenticado"
            );
        }

        boolean esEstudiante = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ESTUDIANTE".equals(a.getAuthority()));

        boolean esEncargado = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ENCARGADO".equals(a.getAuthority()));

        if (esEstudiante) {
            return verificarEstudiante(idEstudiante);
        }

        if (esEncargado) {
            return verificarEncargado(idAmbiente);
        }

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No tienes permisos para acceder a este registro"
        );
    }
}
