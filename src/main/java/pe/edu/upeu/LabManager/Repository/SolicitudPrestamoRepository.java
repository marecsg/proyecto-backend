package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.SolicitudPrestamo;

import java.util.List;

public interface SolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamo, Integer> {
    List<SolicitudPrestamo> findByEstudiante_IdUsuario(Integer idUsuario);
    List<SolicitudPrestamo> findByEstadoSolicitud(EstadoSolicitud estado);
    @Query("SELECT s FROM SolicitudPrestamo s WHERE s.equipo.ambiente.encargado.idUsuario = :idEncargado")
    List<SolicitudPrestamo> findByEncargadoAmbiente(@Param("idEncargado") Integer idEncargado);
}
