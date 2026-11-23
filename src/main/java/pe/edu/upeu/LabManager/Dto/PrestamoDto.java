package pe.edu.upeu.LabManager.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoPrestamo;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoItem;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import java.time.LocalDateTime;
import java.util.List;

public record PrestamoDto() {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Response(
            Integer idPrestamo,
            String codigoCorrelativo,
            Integer idSolicitud,
            String nombreEstudiante,
            String codigoEstudiante,
            String nombreAmbiente,
            Integer idEncargadoAprobacion,
            String nombreEncargadoAprobacion,
            String nombreEncargadoInicio,
            Integer idEncargadoDevolucion,
            String nombreEncargadoDevolucion,
            LocalDateTime fechaInicioUso,
            LocalDateTime fechaFinUso,
            EstadoPrestamo estadoPrestamo,
            List<DetalleResponse> detalles
    ) {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record DetalleResponse(
                Integer idDetalle,
                Integer idEquipo,
                String codigoQrEquipo,
                String nombreEquipo,
                EstadoItem estadoItem,
                EstadoFisico estadoFisicoAlDevolver,
                String comentarioDevolucion
        ) {}
    }

    public record CreateRequest(
            Integer idSolicitud,
            String comentarioRespuesta
    ) {}

    public record UpdateEstadoRequest(
            EstadoPrestamo estadoPrestamo,
            List<DetalleDevolucionDto> detallesDevolucion
    ) {
        public record DetalleDevolucionDto(
                Integer idDetalle,
                EstadoItem estadoItem,
                EstadoFisico estadoFisicoAlDevolver,
                String comentarioDevolucion
        ) {}
    }
}