package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;

import java.util.List;
import java.util.Optional;

public interface TicketIncidenciaService {
    List<TicketIncidencia> listarIncidencias();
    Optional<TicketIncidencia> buscarPorId(Integer id);
    List<TicketIncidencia> listarPorEquipo(Integer idEquipo);
    List<TicketIncidencia> listarPorAmbiente(Integer idAmbiente);
    List<TicketIncidencia> listarPorEstado(EstadoTicket estado);
    TicketIncidencia crearTicket(TicketIncidencia ticket);
    TicketIncidencia actualizarEstadoTicket(Integer idTicket, EstadoTicket nuevoEstado, String respuestaEncargado);
}
