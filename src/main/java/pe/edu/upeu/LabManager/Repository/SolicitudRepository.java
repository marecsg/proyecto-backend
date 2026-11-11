package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upeu.LabManager.Entity.Solicitud;
import java.time.LocalDateTime;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByAmbiente_IdAmbiente(Integer idAmbiente);
    List<Solicitud> findByEstudiante_IdUsuario(Integer idEstudiante);
    @Query("""
    SELECT COUNT(ds) > 0
    FROM DetalleSolicitud ds
    JOIN ds.solicitud s
    LEFT JOIN Prestamo p ON p.solicitud = s
    WHERE ds.equipo.idEquipo = :idEquipo
      AND s.estadoSolicitud = EstadoSolicitud.APROBADA
      AND (p.estadoPrestamo IS NULL OR p.estadoPrestamo IN (
          EstadoPrestamo.PENDIENTE,
          EstadoPrestamo.EN_CURSO
      ))
      AND (s.fechaInicioReserva < :fechaFin AND s.fechaFinReserva > :fechaInicio)
""")
    boolean existeReservaAprobada(
            @Param("idEquipo") Integer idEquipo,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
