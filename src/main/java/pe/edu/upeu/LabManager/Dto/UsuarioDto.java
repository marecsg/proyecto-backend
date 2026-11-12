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
            String rol,
            Integer idAmbiente,
            String nombreAmbiente
    ) {}

}