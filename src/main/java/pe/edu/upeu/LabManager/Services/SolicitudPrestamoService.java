package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.SolicitudPrestamo;

import java.util.List;
import java.util.Optional;

public interface SolicitudPrestamoService {
    List<SolicitudPrestamo> listarSolicitudes();
    Optional<SolicitudPrestamo> buscarPorId(Integer id);
    List<SolicitudPrestamo> listarPorEstudiante(Integer idUsuario);
    List<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estado);
    List<SolicitudPrestamo> listarPorEncargadoAmbiente(Integer idEncargado);
    SolicitudPrestamo crearSolicitud(SolicitudPrestamo solicitud);
    SolicitudPrestamo responderSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado, Integer idEncargado, String comentario);
    SolicitudPrestamo registrarDevolucion(Integer idSolicitud, Integer idEncargado, EstadoFisico estadoFisicoDevolucion, String comentario);
}
