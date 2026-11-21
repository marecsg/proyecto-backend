package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Dto.FcmTokenDto;
import pe.edu.upeu.LabManager.Dto.UsuarioDto;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto.Response> listById(@PathVariable Integer id) {
        return ResponseEntity.ok(mapToResponse(usuarioService.listById(id)));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<UsuarioDto.Response> findByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(mapToResponse(usuarioService.findByCodigoInstitucional(codigo)));
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<Void> guardarFcmToken(
            @RequestBody FcmTokenDto request
    ) {
        usuarioService.guardarFcmToken(request.token());

        return ResponseEntity.noContent().build();
    }

    private UsuarioDto.Response mapToResponse(Usuario usuario) {
        if (usuario == null) return null;

        String nombreRol = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
                ? usuario.getRol().getNombre().toString() : null;

        Integer idAmbiente = (usuario.getAmbiente() != null) ? usuario.getAmbiente().getIdAmbiente() : null;
        String nombreAmbiente = (usuario.getAmbiente() != null) ? usuario.getAmbiente().getNombre() : null;

        return new UsuarioDto.Response(
                usuario.getIdUsuario(),
                usuario.getCodigoInstitucional(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                nombreRol,
                idAmbiente,
                nombreAmbiente
        );
    }
}
