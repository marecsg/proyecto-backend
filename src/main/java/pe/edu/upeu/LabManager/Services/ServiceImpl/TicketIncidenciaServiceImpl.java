package pe.edu.upeu.LabManager.Services.ServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import pe.edu.upeu.LabManager.Fcm.NotificacionService;
import pe.edu.upeu.LabManager.Repository.AmbienteRepository;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Repository.TicketIncidenciaRepository;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Services.CorrelativoService;
import pe.edu.upeu.LabManager.Services.HistorialService;
import pe.edu.upeu.LabManager.Services.TicketIncidenciaService;
import pe.edu.upeu.LabManager.Services.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketIncidenciaServiceImpl implements TicketIncidenciaService {

    @Autowired
    private TicketIncidenciaRepository ticketRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CorrelativoService correlativoService;

    @Autowired
    private NotificacionService notificacionService;

    @Override
    public TicketIncidencia listById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
    }

    @Override
    public List<TicketIncidencia> listByEstudiante(Integer idEstudiante) {
        usuarioService.verificarEstudiante(idEstudiante);
        return ticketRepository.findByUsuarioReporta_IdUsuario(idEstudiante);
    }

    @Override
    public List<TicketIncidencia> listByAmbiente(Integer idAmbiente) {
        usuarioService.verificarEncargado(idAmbiente);
        return ticketRepository.findByAmbiente_IdAmbiente(idAmbiente);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TicketIncidencia create(Integer idUsuario, Integer idAmbiente, Integer idEquipo, TipoIncidencia tipo, String descripcion) {

        if (tipo == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes indicar el tipo de incidencia"
            );
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La descripción es obligatoria"
            );
        }

        Usuario usuario = usuarioService.verificarEstudiante(idUsuario);

        Ambiente ambiente = ambienteRepository.findById(idAmbiente)
                .orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));

        Equipo equipo = null;

        if (idEquipo != null) {

            equipo = equipoRepository.findById(idEquipo)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "El equipo seleccionado no existe"
                    ));

            if (equipo.getAmbiente() == null ||
                    !equipo.getAmbiente().getIdAmbiente().equals(idAmbiente)) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El equipo no pertenece al laboratorio seleccionado"
                );
            }
        }

        TicketIncidencia ticket = new TicketIncidencia();
        ticket.setCodigoCorrelativo(correlativoService.generar("INC"));
        ticket.setUsuarioReporta(usuario);
        ticket.setAmbiente(ambiente);
        ticket.setEquipo(equipo);
        ticket.setTipoIncidencia(tipo);
        ticket.setDescripcion(descripcion);
        ticket.setEstadoTicket(EstadoTicket.PENDIENTE);
        ticket.setFechaReporte(LocalDateTime.now());

        TicketIncidencia ticketGuardado =
                ticketRepository.save(ticket);

        Historial historial = new Historial();

        historial.setUsuarioActor(usuario);
        historial.setUsuarioAfectado(usuario);

        historial.setTipoAccion(
                TipoAccion.INCIDENCIA_REPORTADA
        );

        historial.setTipoEntidad("INCIDENCIA");
        historial.setIdEntidad(ticketGuardado.getIdTicket());
        historial.setDescripcion("Incidencia reportada");

        historialService.registrarAccion(historial);

        notificacionService.notificarEncargados(
                ticketGuardado.getAmbiente().getIdAmbiente(),
                "Nueva incidencia",
                "Se reportó la incidencia "
                        + ticketGuardado.getCodigoCorrelativo()
        );

        return ticketGuardado;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TicketIncidencia updateResolucion(Integer id, EstadoTicket estado, String respuestaEncargado) {
        TicketIncidencia ticket = listById(id);

        Usuario encargado = usuarioService.verificarEncargado(
                ticket.getAmbiente().getIdAmbiente()
        );

        EstadoTicket estadoAnterior = ticket.getEstadoTicket();

        boolean transicionValida =
                (estadoAnterior == EstadoTicket.PENDIENTE &&
                        estado == EstadoTicket.EN_PROCESO)
                        ||
                        (estadoAnterior == EstadoTicket.EN_PROCESO &&
                                estado == EstadoTicket.RESUELTO);

        if (!transicionValida) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede cambiar la incidencia de "
                            + estadoAnterior + " a " + estado
            );
        }

        ticket.setEstadoTicket(estado);

        ticket.setRespuestaEncargado(respuestaEncargado);

        if (estado == EstadoTicket.RESUELTO) {
            ticket.setFechaResolucion(LocalDateTime.now());
        }

        TicketIncidencia ticketGuardado = ticketRepository.save(ticket);

        Historial historial = new Historial();

        historial.setUsuarioActor(encargado);
        historial.setUsuarioAfectado(ticket.getUsuarioReporta());
        historial.setTipoAccion(
                estado == EstadoTicket.EN_PROCESO
                        ? TipoAccion.INCIDENCIA_EN_PROCESO
                        : TipoAccion.INCIDENCIA_RESUELTA
        );
        historial.setTipoEntidad("INCIDENCIA");
        historial.setIdEntidad(ticketGuardado.getIdTicket());
        historial.setDescripcion("Incidencia " + estado.name().toLowerCase());

        historialService.registrarAccion(historial);

        if (estadoAnterior != estado) {

            if (estado == EstadoTicket.EN_PROCESO) {

                notificacionService.notificarUsuario(
                        ticketGuardado.getUsuarioReporta(),
                        "Incidencia en proceso",
                        "Tu incidencia "
                                + ticketGuardado.getCodigoCorrelativo()
                                + " está siendo atendida."
                );

            } else if (estado == EstadoTicket.RESUELTO) {

                notificacionService.notificarUsuario(
                        ticketGuardado.getUsuarioReporta(),
                        "Incidencia resuelta",
                        "Tu incidencia "
                                + ticketGuardado.getCodigoCorrelativo()
                                + " fue resuelta."
                );
            }
        }

        return ticketGuardado;
    }
}
