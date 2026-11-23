package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import java.time.LocalDateTime;
import java.util.List;

public record SolicitudDto() {

    public record Response(
            Integer idSolicitud,
            String codigoCorrelativo,
            Integer idEstudiante,
            String nombreEstudiante,
            String codigoEstudiante,
            Integer idAmbiente,
            String nombreAmbiente,
            String motivoSolicitud,
            LocalDateTime fechaInicioReserva,
            LocalDateTime fechaFinReserva,
            LocalDateTime fechaSolicitud,
            EstadoSolicitud estadoSolicitud,
            LocalDateTime fechaRespuesta,
            String comentarioRespuesta,
            String nombreEncargadoRespuesta,
            List<DetalleResponse> detalles

    ) {
        public record DetalleResponse(
                Integer idEquipo,        // Aplanado
                String codigoQrEquipo,   // Aplanado
                String nombreEquipo      // Aplanado
        ) {}
    }

    public record CreateRequest(
            Integer idEstudiante,
            Integer idAmbiente,
            String motivoSolicitud,
            LocalDateTime fechaInicioReserva,
            LocalDateTime fechaFinReserva,
            List<ItemRequest> equipos,
            Boolean aceptoTerminos
    ) {
        public record ItemRequest(
                Integer idEquipo
        ) {}
    }

    public record UpdateRequest(
            EstadoSolicitud estadoSolicitud,
            String comentarioRespuesta

    ) {}

}