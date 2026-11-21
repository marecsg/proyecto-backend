package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.*;
import pe.edu.upeu.LabManager.Repository.*;
import pe.edu.upeu.LabManager.Services.*;

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

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private CorrelativoService correlativoService;

    @Override
    public Prestamo listById(Integer id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
    }

    @Override
    public List<Prestamo> listByEstudiante(Integer idEstudiante) {
        usuarioService.verificarEstudiante(idEstudiante);
        return prestamoRepository.findBySolicitud_Estudiante_IdUsuario(idEstudiante);
    }

    @Override
    public List<Prestamo> listByAmbiente(Integer idAmbiente) {
        usuarioService.verificarEncargado(idAmbiente);
        return prestamoRepository.findBySolicitud_Ambiente_IdAmbiente(idAmbiente);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Prestamo crearDesdeSolicitud(Integer idSolicitud, String comentarioRespuesta) {

        Solicitud solicitud = solicitudRepository.bloquearPorId(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        ;

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya fue procesada");
        }

        List<Integer> idsEquipos = solicitud.getDetalles().stream()
                .map(det -> det.getEquipo().getIdEquipo())
                .distinct()
                .sorted()
                .toList();

        for (Integer idEquipo : idsEquipos) {
            equipoRepository.bloquearPorId(idEquipo)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Equipo no encontrado"
                    ));
        }

        for (DetalleSolicitud detSolicitud : solicitud.getDetalles()) {
            boolean conflicto = solicitudRepository.existeReservaAprobada(
                    detSolicitud.getEquipo().getIdEquipo(),
                    solicitud.getFechaInicioReserva(),
                    solicitud.getFechaFinReserva()
            );

            if (conflicto) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "El equipo " + detSolicitud.getEquipo().getNombre() + " ya está reservado en ese horario"
                );
            }
        }

        Usuario encargadoAprobacion = usuarioService.verificarEncargado(
                solicitud.getAmbiente().getIdAmbiente()
        );

        solicitudService.cambiarEstado(idSolicitud, EstadoSolicitud.APROBADA, comentarioRespuesta);

        Prestamo prestamo = new Prestamo();
        prestamo.setCodigoCorrelativo(correlativoService.generar("PRE"));
        prestamo.setSolicitud(solicitud);
        prestamo.setEncargadoAprobacion(encargadoAprobacion);
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
    public Prestamo cambiarEstado(
            Integer idPrestamo,
            EstadoPrestamo nuevoEstado
    ) {
        Prestamo prestamo = listById(idPrestamo);

        // Solo se permite iniciar o cancelar préstamos
        if (nuevoEstado != EstadoPrestamo.EN_CURSO &&
                nuevoEstado != EstadoPrestamo.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se permite iniciar o cancelar un préstamo"
            );
        }

        // Solo se pueden modificar préstamos pendientes
        if (prestamo.getEstadoPrestamo() != EstadoPrestamo.PENDIENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se puede iniciar o cancelar un préstamo pendiente"
            );
        }

        Usuario usuarioActor;
        TipoAccion tipoAccion;

        // INICIAR PRÉSTAMO
        if (nuevoEstado == EstadoPrestamo.EN_CURSO) {


            // Verificar al encargado del ambiente
            usuarioActor = usuarioService.verificarEncargado(
                    prestamo.getSolicitud().getAmbiente().getIdAmbiente()
            );

            tipoAccion = TipoAccion.PRESTAMO_INICIADO;
            // Registrar fecha real de inicio
            prestamo.setFechaInicioUso(LocalDateTime.now());

            // Marcar todos los equipos como prestados
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                Equipo equipo = detalle.getEquipo();
                equipo.setDisponibilidad(Disponibilidad.PRESTADO);
                equipoRepository.save(equipo);
            }

            // CANCELAR PRÉSTAMO
        } else{

            usuarioActor = usuarioService.verificarEstudiante(
                    prestamo.getSolicitud().getEstudiante().getIdUsuario()
            );

            tipoAccion = TipoAccion.PRESTAMO_CANCELADO;
        }

        prestamo.setEstadoPrestamo(nuevoEstado);


        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        Historial historial = new Historial();

        historial.setUsuarioActor(usuarioActor);
        historial.setUsuarioAfectado(
                prestamo.getSolicitud().getEstudiante()
        );

        historial.setTipoAccion(tipoAccion);
        historial.setTipoEntidad("PRESTAMO");
        historial.setIdEntidad(prestamoGuardado.getIdPrestamo());

        historial.setDescripcion(
                nuevoEstado == EstadoPrestamo.EN_CURSO
                        ? "Préstamo iniciado"
                        : "Préstamo cancelado"
        );

        historialService.registrarAccion(historial);

        return prestamoGuardado;

    }


    @Override
    @Transactional
    public Prestamo registrarDevolucion(
            Integer idPrestamo,
            EstadoPrestamo nuevoEstado,
            List<DetallePrestamo> detallesDevolucion
    ) {
        Prestamo prestamo = listById(idPrestamo);


        if (prestamo.getEstadoPrestamo() != EstadoPrestamo.EN_CURSO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se puede devolver un préstamo en curso"
            );
        }

        if (nuevoEstado != null &&
                nuevoEstado != EstadoPrestamo.FINALIZADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La devolución solo puede finalizar el préstamo"
            );
        }

        Usuario encargadoDevolucion = usuarioService.verificarEncargado(
                prestamo.getSolicitud().getAmbiente().getIdAmbiente()
        );

        if (detallesDevolucion == null ||
                prestamo.getDetalles() == null ||
                detallesDevolucion.size() != prestamo.getDetalles().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes registrar todos los equipos del préstamo"
            );
        }

        java.util.Set<Integer> idsRecibidos = new java.util.HashSet<>();

        for (DetallePrestamo itemDto : detallesDevolucion) {

            if (itemDto == null || itemDto.getIdDetalle() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El ID del detalle es obligatorio"
                );
            }

            if (!idsRecibidos.add(itemDto.getIdDetalle())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No puedes registrar dos veces el mismo equipo"
                );
            }

            DetallePrestamo detalle = detallePrestamoRepository
                    .findById(itemDto.getIdDetalle())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Detalle de préstamo no encontrado"
                    ));

            if (!detalle.getPrestamo().getIdPrestamo()
                    .equals(prestamo.getIdPrestamo())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El equipo no pertenece a este préstamo"
                );
            }

            EstadoItem estadoItem = itemDto.getEstadoItem();

            if (estadoItem != EstadoItem.DEVUELTO &&
                    estadoItem != EstadoItem.NO_DEVUELTO) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cada equipo debe estar DEVUELTO o NO_DEVUELTO"
                );
            }

            // Un equipo devuelto debe tener condición física
            if (estadoItem == EstadoItem.DEVUELTO &&
                    itemDto.getEstadoFisicoAlDevolver() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Indica el estado físico del equipo devuelto"
                );
            }

            // Guardar resultado individual
            detalle.setEstadoItem(estadoItem);

            detalle.setEstadoFisicoAlDevolver(
                    estadoItem == EstadoItem.DEVUELTO
                            ? itemDto.getEstadoFisicoAlDevolver()
                            : null
            );

            detalle.setComentarioDevolucion(
                    itemDto.getComentarioDevolucion() == null ||
                            itemDto.getComentarioDevolucion().isBlank()
                            ? null
                            : itemDto.getComentarioDevolucion().trim()
            );

            detallePrestamoRepository.save(detalle);

            Equipo equipo = detalle.getEquipo();

            if (estadoItem == EstadoItem.NO_DEVUELTO) {

                equipo.setDisponibilidad(
                        Disponibilidad.NO_DISPONIBLE
                );

            } else if (
                    itemDto.getEstadoFisicoAlDevolver()
                            == pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico.OPERATIVO
            ) {

                equipo.setDisponibilidad(
                        Disponibilidad.DISPONIBLE
                );

            } else {

                equipo.setDisponibilidad(
                        Disponibilidad.NO_DISPONIBLE
                );
            }

            equipoRepository.save(equipo);
        }


        prestamo.setEstadoPrestamo(EstadoPrestamo.FINALIZADO);
        prestamo.setEncargadoDevolucion(encargadoDevolucion);
        prestamo.setFechaFinUso(LocalDateTime.now());

        Prestamo prestamoGuardado =
                prestamoRepository.save(prestamo);

        // Registrar devolución en el historial
        Historial historial = new Historial();

        historial.setUsuarioActor(encargadoDevolucion);

        historial.setUsuarioAfectado(
                prestamo.getSolicitud().getEstudiante()
        );

        historial.setTipoAccion(
                TipoAccion.DEVOLUCION_REGISTRADA
        );

        historial.setTipoEntidad("PRESTAMO");

        historial.setIdEntidad(
                prestamoGuardado.getIdPrestamo()
        );

        historial.setDescripcion("Devolución registrada");

        historialService.registrarAccion(historial);

        return prestamoGuardado;

    }

}