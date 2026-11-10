package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;

import java.util.List;

public interface TicketIncidenciaService {

    TicketIncidencia create(Integer idUsuario, Integer idAmbiente, Integer idEquipo, TipoIncidencia tipo, String descripcion);
    TicketIncidencia updateResolucion(Integer id, EstadoTicket estado, String respuestaEncargado);
    TicketIncidencia listById(Integer id);
    List<TicketIncidencia> listByAmbiente(Integer idAmbiente);

}
