package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Historial;

import java.util.List;

public interface HistorialService {

    Historial registrarAccion(Historial historial);

    List<Historial> getByUsuarioActor(Integer idUsuario);

    List<Historial> getByUsuarioAfectado(Integer idUsuario);

}
