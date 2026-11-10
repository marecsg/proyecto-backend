package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.DetallePrestamo;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Integer> {
}
