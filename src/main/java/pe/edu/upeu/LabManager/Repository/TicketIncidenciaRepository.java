package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;

import java.util.List;

public interface TicketIncidenciaRepository extends JpaRepository<TicketIncidencia, Integer> {
    List<TicketIncidencia> findByAmbienteIdAmbiente(Integer idAmbiente);
    List<TicketIncidencia> findByEstadoTicket(EstadoTicket estado);
    List<TicketIncidencia> findByEquipoIdEquipo(Integer idEquipo);
}
