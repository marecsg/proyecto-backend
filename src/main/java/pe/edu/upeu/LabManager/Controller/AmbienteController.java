package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Entity.Ambiente;
import pe.edu.upeu.LabManager.Services.AmbienteService;

import java.util.List;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    @Autowired
    private AmbienteService ambienteService;

    @GetMapping
    public List<Ambiente> listarAmbientes() {
        return ambienteService.listarAmbientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambiente> buscarPorId(@PathVariable Integer id) {
        return ambienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}