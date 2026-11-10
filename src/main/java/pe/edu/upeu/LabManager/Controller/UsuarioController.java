package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
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


    private UsuarioDto.Response mapToResponse(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDto.Response.RolDto rolDto = usuario.getRol() != null ?
                new UsuarioDto.Response.RolDto(usuario.getRol().getIdRol(), usuario.getRol().getNombre()) : null;

        AmbienteDto ambienteDto = usuario.getAmbiente() != null ?
                new AmbienteDto(usuario.getAmbiente().getIdAmbiente(), usuario.getAmbiente().getNombre()) : null;

        return new UsuarioDto.Response(
                usuario.getIdUsuario(),
                usuario.getCodigoInstitucional(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                rolDto,
                ambienteDto
        );
    }
}
