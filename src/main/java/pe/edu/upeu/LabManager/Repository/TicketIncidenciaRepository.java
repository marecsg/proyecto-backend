package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Prestamo;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;

import java.util.List;

public interface TicketIncidenciaRepository extends JpaRepository<TicketIncidencia, Integer> {
    List<TicketIncidencia> findByAmbiente_IdAmbiente(Integer idAmbiente);

    List<TicketIncidencia> findByUsuarioReporta_IdUsuario(Integer idEstudiante);

}
