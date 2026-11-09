package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;
import pe.edu.upeu.LabManager.Repository.TicketIncidenciaRepository;
import pe.edu.upeu.LabManager.Services.TicketIncidenciaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketIncidenciaServiceImpl implements TicketIncidenciaService {
    @Autowired
    private TicketIncidenciaRepository ticketIncidenciaRepository;

    @Override
    public List<TicketIncidencia> listarIncidencias() {
        return ticketIncidenciaRepository.findAll();
    }

    @Override
    public Optional<TicketIncidencia> buscarPorId(Integer id) {
        return ticketIncidenciaRepository.findById(id);
    }

    @Override
    public List<TicketIncidencia> listarPorEquipo(Integer idEquipo) {
        return ticketIncidenciaRepository.findByEquipoIdEquipo(idEquipo);
    }

    @Override
    public List<TicketIncidencia> listarPorAmbiente(Integer idAmbiente) {
        return ticketIncidenciaRepository.findByAmbienteIdAmbiente(idAmbiente);
    }

    @Override
    public List<TicketIncidencia> listarPorEstado(EstadoTicket estado) {
        return ticketIncidenciaRepository.findByEstadoTicket(estado);
    }

    @Override
    public TicketIncidencia crearTicket(TicketIncidencia ticket) {
        ticket.setEstadoTicket(EstadoTicket.ABIERTO);
        ticket.setFechaReporte(LocalDateTime.now());
        return ticketIncidenciaRepository.save(ticket);
    }

    @Override
    public TicketIncidencia actualizarEstadoTicket(Integer idTicket, EstadoTicket nuevoEstado, String respuestaEncargado) {
        TicketIncidencia ticket = ticketIncidenciaRepository.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + idTicket));

        if (nuevoEstado != null) {
            ticket.setEstadoTicket(nuevoEstado);

            if (nuevoEstado == EstadoTicket.RESUELTO) {
                ticket.setFechaResolucion(LocalDateTime.now());
            }
        }

        if (respuestaEncargado != null) {
            ticket.setRespuestaEncargado(respuestaEncargado);
        }

        return ticketIncidenciaRepository.save(ticket);
    }
}
