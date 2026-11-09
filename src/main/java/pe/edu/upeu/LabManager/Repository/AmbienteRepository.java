package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Ambiente;

import java.util.Optional;

public interface AmbienteRepository extends JpaRepository<Ambiente, Integer> {
    Optional<Ambiente> findByNombre(String nombre);
}
