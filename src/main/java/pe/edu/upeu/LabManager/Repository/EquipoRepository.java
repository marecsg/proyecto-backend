package pe.edu.upeu.LabManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    List<Equipo> findByAmbienteIdAmbiente(Integer idAmbiente);
    List<Equipo> findByDisponibilidad(Disponibilidad disponibilidad);
    Optional<Equipo> findByCodigoQr(String codigoQr);
}
