package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Usuario;

public interface UsuarioService {
    Usuario listById(Integer id);
    Usuario findByCodigoInstitucional(String codigoInstitucional);
}
