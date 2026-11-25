package pe.edu.upeu.LabManager.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    Optional<Equipo> findByCodigoQr(String codigoQr);
    List<Equipo> findByDisponibilidad(Disponibilidad disponibilidad);

    List<Equipo> findByAmbiente_IdAmbiente(Integer idAmbiente);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipo e WHERE e.idEquipo = :id")
    Optional<Equipo> bloquearPorId(@Param("id") Integer id);
}
