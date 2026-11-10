package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;

import java.util.List;

public interface EquipoService {
    List<Equipo> listAll();
    Equipo listById(Integer idEquipo);
    Equipo listByCodigoQr(String codigoQr);
    List<Equipo> listByDisponibilidad(Disponibilidad disponibilidad);
}
