package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.Solicitud;

import java.time.LocalDateTime;
import java.util.List;

public interface SolicitudService {
    Solicitud create(Integer idEstudiante, Integer idAmbiente, String motivoSolicitud, LocalDateTime fechaInicioReserva, LocalDateTime fechaFinReserva, List<Integer> idsEquipos, Boolean aceptoTerminos);
    Solicitud cambiarEstado(Integer idSolicitud, EstadoSolicitud nuevoEstado, String comentarioRespuesta);
    Solicitud listById(Integer id);
    List<Solicitud> listByEstudiante(Integer idEstudiante);
    List<Solicitud> listByAmbiente(Integer idAmbiente);
    boolean verificarDisponibilidadEquipo(Integer idEquipo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
