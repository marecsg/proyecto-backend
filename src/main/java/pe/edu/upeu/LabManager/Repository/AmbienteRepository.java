package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Integer> {
}
