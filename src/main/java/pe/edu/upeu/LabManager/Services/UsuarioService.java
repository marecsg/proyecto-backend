package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> listarPorRol(Integer idRol);
}
