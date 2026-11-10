package pe.edu.upeu.LabManager.Dto;

public record AuthDto() {

    public record LoginRequest(
            String correo,
            String password
    ) {}

    public record LoginResponse(
            String token,
            UsuarioDto.Response user
    ) {}
}