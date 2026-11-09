package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Entity.SolicitudPrestamo;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Repository.SolicitudPrestamoRepository;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Services.SolicitudPrestamoService;
import pe.edu.upeu.LabManager.Services.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudPrestamoServiceImpl implements SolicitudPrestamoService {

    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<SolicitudPrestamo> listarSolicitudes() {
        return solicitudPrestamoRepository.findAll();
    }

    @Override
    public Optional<SolicitudPrestamo> buscarPorId(Integer id) {
        return solicitudPrestamoRepository.findById(id);
    }

    @Override
    public List<SolicitudPrestamo> listarPorEstudiante(Integer idUsuario) {
        return solicitudPrestamoRepository.findByEstudiante_IdUsuario(idUsuario);
    }

    @Override
    public List<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estado) {
        return solicitudPrestamoRepository.findByEstadoSolicitud(estado);
    }

    @Override
    public List<SolicitudPrestamo> listarPorEncargadoAmbiente(Integer idEncargado) {
        return solicitudPrestamoRepository.findByEncargadoAmbiente(idEncargado);
    }

    @Override
    public SolicitudPrestamo crearSolicitud(SolicitudPrestamo solicitud) {

        Equipo equipo = equipoRepository.findById(solicitud.getEquipo().getIdEquipo())
                .orElseThrow(() -> new RuntimeException("El equipo especificado no existe."));

        if (equipo.getEstadoFisico() == EstadoFisico.MANTENIMIENTO ||
                equipo.getEstadoFisico() == EstadoFisico.DEFECTUOSO ||
                equipo.getDisponibilidad() == Disponibilidad.NO_DISPONIBLE) {

            throw new RuntimeException("No se puede crear la solicitud porque el equipo se encuentra en Mantenimiento, Defectuoso o No Disponible.");
        }

        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        return solicitudPrestamoRepository.save(solicitud);
    }

    @Override
    public SolicitudPrestamo responderSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado, Integer idEncargado, String comentario) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + idSolicitud));

        Integer idEncargadoDelAmbiente = solicitud.getEquipo().getAmbiente().getEncargado().getIdUsuario();

        if (!idEncargadoDelAmbiente.equals(idEncargado)) {
            throw new RuntimeException("Acceso denegado: Solo el encargado asignado a este ambiente puede aprobar o rechazar sus equipos.");
        }

        solicitud.setEstadoSolicitud(nuevoEstado);

        if (nuevoEstado == EstadoSolicitud.ACEPTADA || nuevoEstado == EstadoSolicitud.RECHAZADA) {
            Usuario encargado = new Usuario();
            encargado.setIdUsuario(idEncargado);

            solicitud.setEncargadoAprobacion(encargado);
            solicitud.setComentarioAprobacion(comentario);
            solicitud.setFechaAprobacion(LocalDateTime.now());
        }

        return solicitudPrestamoRepository.save(solicitud);
    }



    @Override
    public SolicitudPrestamo registrarDevolucion(Integer idSolicitud, Integer idEncargado, EstadoFisico estadoFisicoDevolucion, String comentario) {

        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.EN_CURSO) {
            throw new RuntimeException("La solicitud no está en curso, no se puede registrar la devolución.");
        }

        Integer encargadoAsignadoId = solicitud.getEquipo().getAmbiente().getEncargado().getIdUsuario();
        if (!encargadoAsignadoId.equals(idEncargado)) {
            throw new RuntimeException("Acceso denegado: Solo el encargado titular de este ambiente puede registrar la devolución.");
        }

        Usuario encargadoDevolucion = usuarioRepository.findById(idEncargado)
                .orElseThrow(() -> new RuntimeException("Usuario encargado de devolución no encontrado"));

        solicitud.setEstadoSolicitud(EstadoSolicitud.FINALIZADO);
        solicitud.setEncargadoDevolucion(encargadoDevolucion);
        solicitud.setFechaDevolucionReal(LocalDateTime.now());
        solicitud.setEstadoFisicoAlDevolver(estadoFisicoDevolucion);
        solicitud.setComentarioDevolucion(comentario);

        Equipo equipo = solicitud.getEquipo();

        if (estadoFisicoDevolucion == EstadoFisico.OPERATIVO) {

            equipo.setEstadoFisico(EstadoFisico.OPERATIVO);
            equipo.setDisponibilidad(Disponibilidad.DISPONIBLE);
        } else {

            equipo.setEstadoFisico(EstadoFisico.DEFECTUOSO);
            equipo.setDisponibilidad(Disponibilidad.NO_DISPONIBLE);
        }

        equipoRepository.save(equipo);

        return solicitudPrestamoRepository.save(solicitud);
    }
}
