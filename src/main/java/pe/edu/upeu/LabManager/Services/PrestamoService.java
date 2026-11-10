package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.DetallePrestamo;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoPrestamo;
import pe.edu.upeu.LabManager.Entity.Prestamo;

import java.util.List;

public interface PrestamoService {

    Prestamo listById(Integer id);
    List<Prestamo> listByEstudiante(Integer idEstudiante);
    List<Prestamo> listByAmbiente(Integer idAmbiente);
    Prestamo crearDesdeSolicitud(Integer idSolicitud, Integer idEncargadoAprobacion, String comentarioAprobacion);
    Prestamo cambiarEstado(Integer idPrestamo, EstadoPrestamo nuevoEstado);
    Prestamo registrarDevolucion(Integer idPrestamo, Integer idEncargadoDevolucion, EstadoPrestamo nuevoEstado, List<DetallePrestamo> detallesDevolucion);

}