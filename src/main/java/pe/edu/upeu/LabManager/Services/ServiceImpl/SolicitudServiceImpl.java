package pe.edu.upeu.LabManager.Services.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;
import pe.edu.upeu.LabManager.Repository.*;
import pe.edu.upeu.LabManager.Services.HistorialService;
import pe.edu.upeu.LabManager.Services.SolicitudService;
import pe.edu.upeu.LabManager.Services.UsuarioService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private DetalleSolicitudRepository detalleSolicitudRepository;

    @Override
    public Solicitud listById(Integer id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    @Override
    public List<Solicitud> listByEstudiante(Integer idEstudiante) {
        return solicitudRepository.findByEstudiante_IdUsuario(idEstudiante);
    }

    @Override
    public List<Solicitud> listByAmbiente(Integer idAmbiente) {
        return solicitudRepository.findByAmbiente_IdAmbiente(idAmbiente);
    }

    @Override
    @Transactional
    public Solicitud create(Integer idEstudiante, Integer idAmbiente, String motivoSolicitud,
                            LocalDateTime fechaInicioReserva, LocalDateTime fechaFinReserva,
                            List<Integer> idsEquipos) {

        Usuario estudiante = usuarioRepository.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Ambiente ambiente = ambienteRepository.findById(idAmbiente)
                .orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));

        for (Integer idEquipo : idsEquipos) {
            Equipo equipo = equipoRepository.findById(idEquipo)
                    .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + idEquipo));

            if (!equipo.getAmbiente().getIdAmbiente().equals(idAmbiente)) {
                throw new RuntimeException("El equipo " + equipo.getNombre() + " no pertenece al laboratorio seleccionado.");
            }
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setEstudiante(estudiante);
        solicitud.setAmbiente(ambiente);
        solicitud.setMotivoSolicitud(motivoSolicitud);
        solicitud.setFechaInicioReserva(fechaInicioReserva);
        solicitud.setFechaFinReserva(fechaFinReserva);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        List<DetalleSolicitud> detalles = new ArrayList<>();
        for (Integer idEquipo : idsEquipos) {
            Equipo equipo = equipoRepository.findById(idEquipo).get();
            DetalleSolicitud detalle = new DetalleSolicitud();
            detalle.setSolicitud(solicitudGuardada);
            detalle.setEquipo(equipo);

            detalles.add(detalleSolicitudRepository.save(detalle));
        }

        solicitudGuardada.setDetalles(detalles);

        Historial historial = new Historial();

        historial.setUsuarioActor(estudiante);
        historial.setUsuarioAfectado(estudiante);
        historial.setTipoAccion(TipoAccion.SOLICITUD_CREADA);
        historial.setTipoEntidad("SOLICITUD");
        historial.setIdEntidad(solicitudGuardada.getIdSolicitud());
        historial.setDescripcion("Solicitud enviada");

        historialService.registrarAccion(historial);

        return solicitudGuardada;
    }



    @Override
    @Transactional
    public Solicitud cambiarEstado(
            Integer idSolicitud,
            EstadoSolicitud nuevoEstado,
            String comentarioRespuesta
    ) {
        Solicitud solicitud = listById(idSolicitud);

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("Estado no válido");
        }

        EstadoSolicitud estadoActual = solicitud.getEstadoSolicitud();

        // Solo se pueden responder solicitudes pendientes
        boolean transicionValida =
                estadoActual == EstadoSolicitud.PENDIENTE &&
                        (nuevoEstado == EstadoSolicitud.APROBADA ||
                                nuevoEstado == EstadoSolicitud.RECHAZADA ||
                                nuevoEstado == EstadoSolicitud.CANCELADA);

        if (!transicionValida) {
            throw new IllegalStateException(
                    "No se puede cambiar de " +
                            estadoActual + " a " + nuevoEstado
            );
        }

        Usuario usuarioActor;
        TipoAccion tipoAccion;

        // Aprobar o rechazar: encargado
        if (nuevoEstado == EstadoSolicitud.APROBADA ||
                nuevoEstado == EstadoSolicitud.RECHAZADA) {

            usuarioActor = usuarioService.verificarEncargado(
                    solicitud.getAmbiente().getIdAmbiente()
            );

            tipoAccion = nuevoEstado == EstadoSolicitud.APROBADA
                    ? TipoAccion.SOLICITUD_APROBADA
                    : TipoAccion.SOLICITUD_RECHAZADA;

             // Cancelar: estudiante propietario
        } else {
            usuarioActor = usuarioService.verificarEstudiante(
                    solicitud.getEstudiante().getIdUsuario()
            );

            tipoAccion = TipoAccion.SOLICITUD_CANCELADA;
        }

        // Guardar la respuesta
        solicitud.setEstadoSolicitud(nuevoEstado);
        solicitud.setFechaRespuesta(LocalDateTime.now());

        // El comentario es opcional
        solicitud.setComentarioRespuesta(
                comentarioRespuesta == null ||
                        comentarioRespuesta.isBlank()
                        ? null
                        : comentarioRespuesta.trim()
        );

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        Historial historial = new Historial();

        historial.setUsuarioActor(usuarioActor);
        historial.setUsuarioAfectado(
                solicitud.getEstudiante()
        );

        historial.setTipoAccion(tipoAccion);
        historial.setTipoEntidad("SOLICITUD");
        historial.setIdEntidad(
                solicitudGuardada.getIdSolicitud()
        );

        historial.setDescripcion(
                "Solicitud " + nuevoEstado.name().toLowerCase()
        );

        historialService.registrarAccion(historial);

        return solicitudGuardada;

    }




    @Override
    public boolean verificarDisponibilidadEquipo(Integer idEquipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return !solicitudRepository.existeReservaAprobada(idEquipo, fechaInicio, fechaFin);
    }
}
