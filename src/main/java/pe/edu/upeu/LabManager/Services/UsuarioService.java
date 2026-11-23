package pe.edu.upeu.LabManager.Services;

import pe.edu.upeu.LabManager.Entity.Usuario;

public interface UsuarioService {
    Usuario listById(Integer id);
    Usuario findByCodigoInstitucional(String codigoInstitucional);
    Usuario verificarEncargado(Integer idAmbiente);
    Usuario guardarFcmToken(String token);
    Usuario verificarEstudiante(Integer idEstudiante);
    Usuario verificarAcceso(Integer idEstudiante, Integer idAmbiente);
}
