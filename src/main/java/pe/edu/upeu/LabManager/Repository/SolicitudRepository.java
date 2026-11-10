package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Solicitud;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByAmbiente_IdAmbiente(Integer idAmbiente);
    List<Solicitud> findByEstudiante_IdUsuario(Integer idEstudiante);
}
