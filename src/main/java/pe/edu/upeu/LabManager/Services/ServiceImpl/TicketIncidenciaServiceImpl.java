package pe.edu.upeu.LabManager.Services.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import pe.edu.upeu.LabManager.Repository.AmbienteRepository;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Repository.TicketIncidenciaRepository;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
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

    @Override
    public TicketIncidencia listById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
    }

    @Override
    public List<TicketIncidencia> listByEstudiante(Integer idEstudiante) {
        return ticketRepository.findByUsuarioReporta_IdUsuario(idEstudiante);
    }

    @Override
    public List<TicketIncidencia> listByAmbiente(Integer idAmbiente) {
        return ticketRepository.findByAmbiente_IdAmbiente(idAmbiente);
    }

    @Override
    public TicketIncidencia create(Integer idUsuario, Integer idAmbiente, Integer idEquipo, TipoIncidencia tipo, String descripcion) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ambiente ambiente = ambienteRepository.findById(idAmbiente)
                .orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));

        Equipo equipo = null;
        if (idEquipo != null) {
            equipo = equipoRepository.findById(idEquipo).orElse(null);
        }

        TicketIncidencia ticket = new TicketIncidencia();
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

        return ticketGuardado;
    }

    @Override
    @Transactional
    public TicketIncidencia updateResolucion(Integer id, EstadoTicket estado, String respuestaEncargado) {
        TicketIncidencia ticket = listById(id);

        Usuario encargado = usuarioService.verificarEncargado(
                ticket.getAmbiente().getIdAmbiente()
        );

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

        return ticketGuardado;
    }
}
