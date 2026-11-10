package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import java.time.LocalDateTime;
import java.util.List;

public record SolicitudDto() {

    public record Response(
            Integer idSolicitud,
            UsuarioDto.Response estudiante,
            AmbienteDto ambiente,
            String motivoSolicitud,
            LocalDateTime fechaInicioReserva,
            LocalDateTime fechaFinReserva,
            LocalDateTime fechaSolicitud,
            EstadoSolicitud estadoSolicitud,
            List<DetalleResponse> detalles
    ) {
        public record DetalleResponse(
                Integer idDetalleSolicitud,
                EquipoDto.Response equipo
        ) {}
    }

    public record CreateRequest(
            Integer idEstudiante,
            Integer idAmbiente,
            String motivoSolicitud,
            LocalDateTime fechaInicioReserva,
            LocalDateTime fechaFinReserva,
            List<ItemRequest> equipos
    ) {
        public record ItemRequest(
                Integer idEquipo
        ) {}
    }

    public record UpdateRequest(
            EstadoSolicitud estadoSolicitud
    ) {}

}