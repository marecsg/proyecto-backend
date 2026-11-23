package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Enums.Roles;
import pe.edu.upeu.LabManager.Entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCodigoInstitucional(String codigoInstitucional);
    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByAmbiente_IdAmbienteAndRol_Nombre(Integer idAmbiente, Roles nombre);

    List<Usuario> findAllByFcmToken(String fcmToken);
}
