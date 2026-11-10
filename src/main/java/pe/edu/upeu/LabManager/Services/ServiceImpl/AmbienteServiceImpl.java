package pe.edu.upeu.LabManager.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.Ambiente;
import pe.edu.upeu.LabManager.Repository.AmbienteRepository;
import pe.edu.upeu.LabManager.Services.AmbienteService;

import java.util.List;

@Service
public class AmbienteServiceImpl implements AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Override
    public List<Ambiente> listAll() {
        return ambienteRepository.findAll();
    }
}
