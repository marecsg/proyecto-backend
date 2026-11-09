package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;
import pe.edu.upeu.LabManager.Security.JwtService;

import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String password = request.get("password");

        System.out.println("-> Intentando loguear correo: " + correo);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (!usuarioOpt.isPresent()) {
            System.out.println("-> ERROR: El usuario no existe en la BD.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();
        System.out.println("-> Usuario encontrado: " + usuario.getNombres());
        System.out.println("-> Hash en BD: " + usuario.getPasswordHash());

        boolean passwordMatch = passwordEncoder.matches(password, usuario.getPasswordHash());
        System.out.println("-> ¿Coincide la contraseña?: " + passwordMatch);

        if (passwordMatch) {
            String token = jwtService.generarToken(usuario);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("nombres", usuario.getNombres());
            response.put("correo", usuario.getCorreo());
            response.put("rol", usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN_ROL");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @GetMapping("/generar-hash")
    public String generarHash() {
        String hash = passwordEncoder.encode("123456");
        System.out.println("-> Hash generado para 123456: " + hash);
        return hash;
    }
}