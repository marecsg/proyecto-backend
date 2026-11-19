package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Historial;
import pe.edu.upeu.LabManager.Repository.HistorialRepository;
import pe.edu.upeu.LabManager.Services.HistorialService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialServiceImpl implements HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    @Override
    public Historial registrarAccion(Historial historial) {
        historial.setFechaAccion(LocalDateTime.now());
        return historialRepository.save(historial);
    }

    @Override
    public List<Historial> getByUsuarioActor(Integer idUsuario) {
        return historialRepository.findByUsuarioActor_IdUsuarioOrderByFechaAccionDesc(idUsuario);
    }

    @Override
    public List<Historial> getByUsuarioAfectado(Integer idUsuario) {
        return historialRepository.findByUsuarioAfectado_IdUsuarioOrderByFechaAccionDesc(idUsuario);
    }
}
