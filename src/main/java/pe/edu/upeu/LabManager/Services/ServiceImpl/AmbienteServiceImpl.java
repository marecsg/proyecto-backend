package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Ambiente;
import pe.edu.upeu.LabManager.Repository.AmbienteRepository;
import pe.edu.upeu.LabManager.Services.AmbienteService;

import java.util.List;
import java.util.Optional;

@Service
public class AmbienteServiceImpl implements AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Override
    public List<Ambiente> listarAmbientes() {
        return ambienteRepository.findAll();
    }

    @Override
    public Optional<Ambiente> buscarPorId(Integer id) {
        return ambienteRepository.findById(id);
    }

    @Override
    public Optional<Ambiente> buscarPorNombre(String nombre) {
        return ambienteRepository.findByNombre(nombre);
    }
}
