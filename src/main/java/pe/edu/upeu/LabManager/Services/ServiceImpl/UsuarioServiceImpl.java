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

    @Override
    public Usuario listById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public Usuario findByCodigoInstitucional(String codigoInstitucional) {
        return usuarioRepository.findByCodigoInstitucional(codigoInstitucional)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el código: " + codigoInstitucional));
    }

    @Override
    public Usuario verificarEncargado(Integer idAmbiente) {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no autenticado"
            );
        }

        Usuario usuario = usuarioRepository
                .findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ));

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
}
