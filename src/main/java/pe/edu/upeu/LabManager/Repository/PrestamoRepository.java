package pe.edu.upeu.LabManager.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.LabManager.Entity.Prestamo;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    List<Prestamo> findBySolicitud_Estudiante_IdUsuario(Integer idEstudiante);
    List<Prestamo> findBySolicitud_Ambiente_IdAmbiente(Integer idAmbiente);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Prestamo p WHERE p.idPrestamo = :id")
    Optional<Prestamo> bloquearPorId(@Param("id") Integer id);

}
