package pe.edu.upeu.LabManager.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UsuarioDto() {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Response(
            Integer idUsuario,
            String codigoInstitucional,
            String nombres,
            String apellidos,
            String correo,
            RolDto rol,
            AmbienteDto ambiente
    ) {
        public record RolDto(
                Integer idRol,
                String nombre
        ) {}
    }

    public record Request(
            String codigoInstitucional,
            String nombres,
            String apellidos,
            String correo,
            String password,
            Integer idRol,
            Integer idAmbiente
    ) {}
}