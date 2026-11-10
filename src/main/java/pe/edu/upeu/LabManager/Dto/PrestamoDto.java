package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoPrestamo;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoItem;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import java.time.LocalDateTime;
import java.util.List;

public record PrestamoDto() {

    public record Response(
            Integer idPrestamo,
            SolicitudDto.Response solicitud,
            UsuarioDto.Response encargadoAprobacion,
            UsuarioDto.Response encargadoDevolucion,
            LocalDateTime fechaInicioUso,
            LocalDateTime fechaFinUso,
            String comentarioAprobacion,
            EstadoPrestamo estadoPrestamo,
            List<DetalleResponse> detalles
    ) {
        public record DetalleResponse(
                Integer idDetalle,
                EquipoDto.Response equipo,
                EstadoItem estadoItem,
                EstadoFisico estadoFisicoAlDevolver,
                String comentarioDevolucion
        ) {}
    }

    public record CreateRequest(
            Integer idSolicitud,
            Integer idEncargadoAprobacion,
            String comentarioAprobacion
    ) {}


    public record UpdateEstadoRequest(
            EstadoPrestamo estadoPrestamo,
            Integer idEncargadoDevolucion,
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