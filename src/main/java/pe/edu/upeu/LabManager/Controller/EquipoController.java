package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Services.ServiceImpl.EquipoServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoServiceImpl equipoService;

    @GetMapping
    public List<Equipo> listarEquipos() {
        return equipoService.listarEquipos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> buscarPorId(@PathVariable Integer id) {
        return equipoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/qr/{codigo}")
    public ResponseEntity<Equipo> buscarPorCodigoQr(@PathVariable String codigo) {
        return equipoService.buscarPorCodigoQr(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ambiente/{idAmbiente}")
    public List<Equipo> listarPorAmbiente(@PathVariable Integer idAmbiente) {
        return equipoService.listarPorAmbiente(idAmbiente);
    }

    @GetMapping("/disponibilidad/{disponibilidad}")
    public List<Equipo> listarPorDisponibilidad(@PathVariable Disponibilidad disponibilidad) {
        return equipoService.listarPorDisponibilidad(disponibilidad);
    }

}