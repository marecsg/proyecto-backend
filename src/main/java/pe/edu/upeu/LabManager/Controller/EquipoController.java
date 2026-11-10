package pe.edu.upeu.LabManager.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Dto.EquipoDto;
import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Equipo;
import pe.edu.upeu.LabManager.Services.EquipoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }


    @GetMapping
    public ResponseEntity<List<EquipoDto.Response>> listAll() {
        List<EquipoDto.Response> response = equipoService.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto.Response> listById(@PathVariable Integer id) {
        Equipo equipo = equipoService.listById(id);
        return ResponseEntity.ok(mapToResponse(equipo));
    }

    @GetMapping("/qr/{codigoQr}")
    public ResponseEntity<EquipoDto.Response> listByCodigoQr(@PathVariable String codigoQr) {
        Equipo equipo = equipoService.listByCodigoQr(codigoQr);
        return ResponseEntity.ok(mapToResponse(equipo));
    }

    @GetMapping("/disponibilidad/{disponibilidad}")
    public ResponseEntity<List<EquipoDto.Response>> listByDisponibilidad(@PathVariable Disponibilidad disponibilidad) {
        List<EquipoDto.Response> response = equipoService.listByDisponibilidad(disponibilidad).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    private EquipoDto.Response mapToResponse(Equipo equipo) {
        return new EquipoDto.Response(
                equipo.getIdEquipo(),
                equipo.getCodigoQr(),
                equipo.getNombre(),
                equipo.getDescripcion(),
                new AmbienteDto(
                        equipo.getAmbiente().getIdAmbiente(),
                        equipo.getAmbiente().getNombre()
                ),
                equipo.getEstadoFisico(),
                equipo.getDisponibilidad()
        );
    }

}