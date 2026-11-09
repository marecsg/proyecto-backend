package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipService {
    List<Equipo> listarEquipos();
    Optional<Equipo> buscarPorId(Integer id);
    Optional<Equipo> buscarPorCodigoQr(String codigoQr);
    List<Equipo> listarPorAmbiente(Integer idAmbiente);
    List<Equipo> listarPorDisponibilidad(Disponibilidad disponibilidad);
}
