package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Services.EquipService;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServiceImpl implements EquipService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public List<Equipo> listarEquipos() {
        return equipoRepository.findAll();
    }

    @Override
    public Optional<Equipo> buscarPorId(Integer id) {
        return equipoRepository.findById(id);
    }

    @Override
    public Optional<Equipo> buscarPorCodigoQr(String codigoQr) {
        return equipoRepository.findByCodigoQr(codigoQr);
    }

    @Override
    public List<Equipo> listarPorAmbiente(Integer idAmbiente) {
        return equipoRepository.findByAmbienteIdAmbiente(idAmbiente);
    }

    @Override
    public List<Equipo> listarPorDisponibilidad(Disponibilidad disponibilidad) {
        return equipoRepository.findByDisponibilidad(disponibilidad);
    }
}
