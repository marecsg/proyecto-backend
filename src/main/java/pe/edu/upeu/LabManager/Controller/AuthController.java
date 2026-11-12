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

            String nombreRol = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
                    ? usuario.getRol().getNombre().toString() : null;

            Integer idAmbiente = (usuario.getAmbiente() != null) ? usuario.getAmbiente().getIdAmbiente() : null;
            String nombreAmbiente = (usuario.getAmbiente() != null) ? usuario.getAmbiente().getNombre() : null;

            UsuarioDto.Response usuarioResponse = new UsuarioDto.Response(
                    usuario.getIdUsuario(),
                    usuario.getCodigoInstitucional(),
                    usuario.getNombres(),
                    usuario.getApellidos(),
                    usuario.getCorreo(),
                    nombreRol,
                    idAmbiente,
                    nombreAmbiente
            );

            AuthDto.LoginResponse response = new AuthDto.LoginResponse(token, usuarioResponse);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
}