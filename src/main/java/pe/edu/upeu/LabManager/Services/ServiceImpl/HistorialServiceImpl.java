package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Entity.Historial;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.HistorialRepository;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Services.HistorialService;
import pe.edu.upeu.LabManager.Services.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialServiceImpl implements HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Historial registrarAccion(Historial historial) {
        historial.setFechaAccion(LocalDateTime.now());
        return historialRepository.save(historial);
    }

    @Override
    public List<Historial> getByUsuarioActor(Integer idUsuario) {

        String correo = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Usuario actor = usuarioRepository.findByCorreo(correo)
                .orElseThrow();

        if (!actor.getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este historial");
        }

        return historialRepository.findByUsuarioActor_IdUsuarioOrderByFechaAccionDesc(idUsuario);
    }

    @Override
    public List<Historial> getByUsuarioAfectado(Integer idUsuario) {
        usuarioService.verificarEstudiante(idUsuario);
        return historialRepository.findByUsuarioAfectado_IdUsuarioOrderByFechaAccionDesc(idUsuario);
    }
}
