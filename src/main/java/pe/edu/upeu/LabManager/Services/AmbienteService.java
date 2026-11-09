package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Ambiente;

import java.util.List;
import java.util.Optional;

public interface AmbienteService {
    List<Ambiente> listarAmbientes();
    Optional<Ambiente> buscarPorId(Integer id);
    Optional<Ambiente> buscarPorNombre(String nombre);
}
