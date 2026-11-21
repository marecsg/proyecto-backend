package pe.edu.upeu.LabManager.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Entity.Solicitud;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    Optional<Equipo> findByCodigoQr(String codigoQr);
    List<Equipo> findByDisponibilidad(Disponibilidad disponibilidad);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Solicitud s WHERE s.idSolicitud = :id")
    Optional<Solicitud> bloquearPorId(@Param("id") Integer id);
}
