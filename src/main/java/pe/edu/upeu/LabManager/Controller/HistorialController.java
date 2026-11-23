package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.LabManager.Dto.HistorialDto;
import pe.edu.upeu.LabManager.Dto.SolicitudDto;
import pe.edu.upeu.LabManager.Entity.Historial;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Services.HistorialService;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    @Autowired
    HistorialService historialService;
    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<HistorialDto>> getByEstudiante(@PathVariable Integer idEstudiante) {
        List<HistorialDto> respuestas = historialService.getByUsuarioAfectado(idEstudiante)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/encargado/{idEncargado}")
    public ResponseEntity<List<HistorialDto>> getByEncargado(@PathVariable Integer idEncargado) {
        List<HistorialDto> respuestas = historialService.getByUsuarioActor(idEncargado)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(respuestas);
    }


    private HistorialDto mapToResponse(Historial historial) {
        if (historial == null) return null;

        Usuario actor = historial.getUsuarioActor();
        Usuario afectado = historial.getUsuarioAfectado();

        return new HistorialDto(
                historial.getIdHistorial(),
                actor.getIdUsuario(),
                actor.getNombres() + " " + actor.getApellidos(),
                afectado.getIdUsuario(),
                afectado.getNombres() + " " + afectado.getApellidos(),
                historial.getTipoAccion(),
                historial.getTipoEntidad(),
                historialService.obtenerCodigoEntidad(historial),
                historial.getIdEntidad(),
                historial.getDescripcion(),
                historial.getFechaAccion()
        );
    }

}
