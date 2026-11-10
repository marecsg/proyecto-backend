package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Repository.EquipoRepository;
import pe.edu.upeu.LabManager.Services.EquipoService;

import java.util.List;


@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Override
    public List<Equipo> listAll() {
        return equipoRepository.findAll();
    }

    @Override
    public Equipo listById(Integer idEquipo) {
        return equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
    }

    @Override
    public Equipo listByCodigoQr(String codigoQr) {
        return equipoRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
    }

    @Override
    public List<Equipo> listByDisponibilidad(Disponibilidad disponibilidad) {
        return equipoRepository.findByDisponibilidad(disponibilidad);
    }
}