package pe.edu.upeu.LabManager.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Entity.Ambiente;
import pe.edu.upeu.LabManager.Services.AmbienteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    private final AmbienteService ambienteService;

    public AmbienteController(AmbienteService ambienteService) {
        this.ambienteService = ambienteService;
    }

    @GetMapping
    public ResponseEntity<List<AmbienteDto>> listAllAmbientes() {
        List<AmbienteDto> response = ambienteService.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private AmbienteDto mapToResponse(Ambiente ambiente) {
        return new AmbienteDto(
                ambiente.getIdAmbiente(),
                ambiente.getNombre()
        );
    }
}