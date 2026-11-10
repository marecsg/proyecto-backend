package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Ambiente;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.AmbienteRepository;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Repository.TicketIncidenciaRepository;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Services.TicketIncidenciaService;

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

    @Override
    public TicketIncidencia listById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
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

        return ticketRepository.save(ticket);
    }

    @Override
    public TicketIncidencia updateResolucion(Integer id, EstadoTicket estado, String respuestaEncargado) {
        TicketIncidencia ticket = listById(id);

        ticket.setEstadoTicket(estado);
        ticket.setRespuestaEncargado(respuestaEncargado);

        if (estado == EstadoTicket.RESUELTO) {
            ticket.setFechaResolucion(LocalDateTime.now());
        }

        return ticketRepository.save(ticket);
    }
}
