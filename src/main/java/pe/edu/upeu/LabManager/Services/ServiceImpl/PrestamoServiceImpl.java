package pe.edu.upeu.LabManager.Services.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoItem;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoPrestamo;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Repository.*;
import pe.edu.upeu.LabManager.Services.PrestamoService;
import pe.edu.upeu.LabManager.Services.SolicitudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DetallePrestamoRepository detallePrestamoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public Prestamo listById(Integer id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
    }

    @Override
    public List<Prestamo> listByEstudiante(Integer idEstudiante) {
        return prestamoRepository.findBySolicitud_Estudiante_IdUsuario(idEstudiante);
    }

    @Override
    public List<Prestamo> listByAmbiente(Integer idAmbiente) {
        return prestamoRepository.findBySolicitud_Ambiente_IdAmbiente(idAmbiente);
    }

    @Override
    @Transactional
    public Prestamo crearDesdeSolicitud(Integer idSolicitud, Integer idEncargadoAprobacion, String comentarioAprobacion) {
        Solicitud solicitud = solicitudService.listById(idSolicitud);
        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden aprobar solicitudes en estado PENDIENTE.");
        }

        for (DetalleSolicitud detSolicitud : solicitud.getDetalles()) {
            boolean conflicto = solicitudRepository.existeReservaAprobada(
                    detSolicitud.getEquipo().getIdEquipo(),
                    solicitud.getFechaInicioReserva(),
                    solicitud.getFechaFinReserva()
            );

            if (conflicto) {
                throw new RuntimeException("El equipo " + detSolicitud.getEquipo().getNombre() +
                        " presenta un conflicto de horario para la reserva seleccionada.");
            }
        }

        Usuario encargadoAprobacion = usuarioRepository.findById(idEncargadoAprobacion)
                .orElseThrow(() -> new RuntimeException("Encargado de aprobación no encontrado"));

        solicitudService.cambiarEstado(idSolicitud, EstadoSolicitud.APROBADA);

        Prestamo prestamo = new Prestamo();
        prestamo.setSolicitud(solicitud);
        prestamo.setEncargadoAprobacion(encargadoAprobacion);
        prestamo.setComentarioAprobacion(comentarioAprobacion);
        prestamo.setEstadoPrestamo(EstadoPrestamo.PENDIENTE);

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        List<DetallePrestamo> detallesPrestamo = new ArrayList<>();
        for (DetalleSolicitud detSolicitud : solicitud.getDetalles()) {
            DetallePrestamo detPrestamo = new DetallePrestamo();
            detPrestamo.setPrestamo(prestamoGuardado);
            detPrestamo.setEquipo(detSolicitud.getEquipo());
            detPrestamo.setEstadoItem(null);

            detallesPrestamo.add(detallePrestamoRepository.save(detPrestamo));
        }

        prestamoGuardado.setDetalles(detallesPrestamo);
        return prestamoGuardado;
    }

    @Override
    @Transactional
    public Prestamo cambiarEstado(Integer idPrestamo, EstadoPrestamo nuevoEstado) {
        Prestamo prestamo = listById(idPrestamo);

        if (nuevoEstado == EstadoPrestamo.EN_CURSO) {
            if (prestamo.getEstadoPrestamo() != EstadoPrestamo.PENDIENTE) {
                throw new RuntimeException("Solo se puede entregar un préstamo pendiente.");
            }

            prestamo.setFechaInicioUso(LocalDateTime.now());

            for (DetallePrestamo det : prestamo.getDetalles()) {
                Equipo equipo = det.getEquipo();
                equipo.setDisponibilidad(Disponibilidad.PRESTADO);
                equipoRepository.save(equipo);
            }

        } else if (nuevoEstado == EstadoPrestamo.CANCELADO) {
            if (prestamo.getEstadoPrestamo() != EstadoPrestamo.PENDIENTE) {
                throw new RuntimeException("Solo se puede cancelar un préstamo pendiente.");
            }

            if (prestamo.getSolicitud() != null) {
                solicitudService.cambiarEstado(prestamo.getSolicitud().getIdSolicitud(), EstadoSolicitud.CANCELADA);
            }
        }

        prestamo.setEstadoPrestamo(nuevoEstado);
        return prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional
    public Prestamo registrarDevolucion(
            Integer idPrestamo,
            Integer idEncargadoDevolucion,
            EstadoPrestamo nuevoEstado,
            List<DetallePrestamo> detallesDevolucion
    ) {
        Prestamo prestamo = listById(idPrestamo);

        if (prestamo.getEstadoPrestamo() != EstadoPrestamo.EN_CURSO) {
            throw new RuntimeException("Solo se puede registrar la devolución de un préstamo en estado EN_CURSO.");
        }

        Usuario encargadoDevolucion = usuarioRepository.findById(idEncargadoDevolucion)
                .orElseThrow(() -> new RuntimeException("Encargado de devolución no encontrado"));

        prestamo.setEstadoPrestamo(nuevoEstado != null ? nuevoEstado : EstadoPrestamo.FINALIZADO);
        prestamo.setEncargadoDevolucion(encargadoDevolucion);
        prestamo.setFechaFinUso(LocalDateTime.now());

        if (detallesDevolucion != null) {
            for (DetallePrestamo itemDto : detallesDevolucion) {
                DetallePrestamo detalle = detallePrestamoRepository.findById(itemDto.getIdDetalle())
                        .orElseThrow(() -> new RuntimeException("Detalle de préstamo no encontrado con ID: " + itemDto.getIdDetalle()));

                detalle.setEstadoItem(itemDto.getEstadoItem());
                detalle.setEstadoFisicoAlDevolver(itemDto.getEstadoFisicoAlDevolver());
                detalle.setComentarioDevolucion(itemDto.getComentarioDevolucion());
                detallePrestamoRepository.save(detalle);

                Equipo equipo = detalle.getEquipo();

                if (itemDto.getEstadoItem() == EstadoItem.DEVUELTO) {
                    String condicion = itemDto.getEstadoFisicoAlDevolver() != null
                            ? itemDto.getEstadoFisicoAlDevolver().toString().toUpperCase()
                            : "OPERATIVO";

                    if (condicion.contains("OPERATIVO")) {
                        equipo.setDisponibilidad(Disponibilidad.DISPONIBLE);
                    } else {
                        equipo.setDisponibilidad(Disponibilidad.NO_DISPONIBLE);
                    }
                } else if (itemDto.getEstadoItem() == EstadoItem.NO_DEVUELTO) {
                    equipo.setDisponibilidad(Disponibilidad.NO_DISPONIBLE);
                }

                equipoRepository.save(equipo);
            }
        }

        return prestamoRepository.save(prestamo);
    }
}