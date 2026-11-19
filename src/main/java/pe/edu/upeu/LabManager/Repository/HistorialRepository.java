package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Historial;

import java.util.List;

public interface HistorialRepository extends JpaRepository<Historial, Integer> {


    List<Historial> findByUsuarioActor_IdUsuarioOrderByFechaAccionDesc(
            Integer idUsuario
    );

    List<Historial> findByUsuarioAfectado_IdUsuarioOrderByFechaAccionDesc(
            Integer idUsuario
    );
}


