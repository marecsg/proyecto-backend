package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    List<Prestamo> findBySolicitud_Estudiante_IdUsuario(Integer idEstudiante);
    List<Prestamo> findBySolicitud_Ambiente_IdAmbiente(Integer idAmbiente);
}
