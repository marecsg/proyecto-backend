package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;
import pe.edu.upeu.LabManager.Entity.Historial;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.*;
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

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private TicketIncidenciaRepository ticketIncidenciaRepository;

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

    @Override
    public String obtenerCodigoEntidad(Historial historial) {
        if (historial == null ||
                historial.getIdEntidad() == null ||
                historial.getTipoEntidad() == null) {
            return null;
        }

        Integer id = historial.getIdEntidad();

        return switch (historial.getTipoEntidad()) {

            case "SOLICITUD" ->
                    solicitudRepository.findById(id)
                            .map(solicitud ->
                                    solicitud.getCodigoCorrelativo())
                            .orElse(null);

            case "PRESTAMO" ->
                    prestamoRepository.findById(id)
                            .map(prestamo ->
                                    prestamo.getCodigoCorrelativo())
                            .orElse(null);

            case "INCIDENCIA" ->
                    ticketIncidenciaRepository.findById(id)
                            .map(incidencia ->
                                    incidencia.getCodigoCorrelativo())
                            .orElse(null);

            default -> null;
        };
    }

    @Override
    public String obtenerNombreResponsable(String tipoEntidad, Integer idEntidad, TipoAccion tipoAccion) {
        if (tipoEntidad == null || idEntidad == null || tipoAccion == null) {
            return null;
        }

        return historialRepository
                .findFirstByTipoEntidadAndIdEntidadAndTipoAccionOrderByFechaAccionDesc(
                        tipoEntidad,
                        idEntidad,
                        tipoAccion
                )
                .map(historial -> {
                    Usuario actor = historial.getUsuarioActor();

                    if (actor == null) {
                        return null;
                    }

                    return (actor.getNombres() + " " + actor.getApellidos()).trim();
                })
                .orElse(null);
    }
}
