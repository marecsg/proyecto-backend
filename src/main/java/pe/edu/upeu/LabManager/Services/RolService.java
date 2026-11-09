package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Rol;

import java.util.Optional;

public interface RolService {
    Optional<Rol> buscarPorId(Integer id);
}
