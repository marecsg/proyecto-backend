package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Dto.AuthDto;
import pe.edu.upeu.LabManager.Dto.UsuarioDto;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Security.JwtService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request) {

        String correo = request.correo();
        String password = request.password();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();

        boolean passwordMatch = passwordEncoder.matches(password, usuario.getPasswordHash());

        if (passwordMatch) {
            String token = jwtService.generarToken(usuario);

            UsuarioDto.Response.RolDto rolDto = null;
            if (usuario.getRol() != null) {
                rolDto = new UsuarioDto.Response.RolDto(
                        usuario.getRol().getIdRol(),
                        usuario.getRol().getNombre()
                );
            }

            AmbienteDto ambienteDto = null;
            if (usuario.getAmbiente() != null) {
                ambienteDto = new AmbienteDto(
                        usuario.getAmbiente().getIdAmbiente(),
                        usuario.getAmbiente().getNombre()
                );
            }

            UsuarioDto.Response usuarioResponse = new UsuarioDto.Response(
                    usuario.getIdUsuario(),
                    usuario.getCodigoInstitucional(),
                    usuario.getNombres(),
                    usuario.getApellidos(),
                    usuario.getCorreo(),
                    rolDto,
                    ambienteDto
            );

            AuthDto.LoginResponse response = new AuthDto.LoginResponse(token, usuarioResponse);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
}