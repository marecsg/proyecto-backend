package pe.edu.upeu.LabManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.upeu.LabManager.Security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // Login libre

                                .requestMatchers(HttpMethod.POST, "/api/prestamos/aprobar").hasRole("ENCARGADO")

                                // Registrar devoluciones
                                .requestMatchers(HttpMethod.PATCH, "/api/prestamos/*/devolucion").hasRole("ENCARGADO")

                                // Responder incidencias
                                .requestMatchers(HttpMethod.PUT, "/api/incidencias/*/resolucion").hasRole("ENCARGADO")

                                // Crear solicitudes
                                .requestMatchers(HttpMethod.POST, "/api/solicitudes")
                                .hasRole("ESTUDIANTE")

                                 //crear incidencias
                                .requestMatchers(HttpMethod.POST, "/api/incidencias")
                                .hasAnyRole("ESTUDIANTE", "ENCARGADO")

                                 // Catálogo del laboratorio asignado
                                 .requestMatchers(HttpMethod.GET, "/api/equipos/ambiente/**")
                                 .hasRole("ENCARGADO")

                        .anyRequest().authenticated() // El resto protegido
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}