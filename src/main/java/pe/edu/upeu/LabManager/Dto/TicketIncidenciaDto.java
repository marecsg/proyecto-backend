package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import java.time.LocalDateTime;

public record TicketIncidenciaDto() {

    public record Response(
            Integer idTicket,
            UsuarioDto.Response usuarioReporta,
            AmbienteDto ambiente,
            EquipoDto.Response equipo,
            TipoIncidencia tipoIncidencia,
            String descripcion,
            EstadoTicket estadoTicket,
            LocalDateTime fechaReporte,
            String respuestaEncargado,
            LocalDateTime fechaResolucion
    ) {}

    public record CreateRequest(
            Integer idUsuarioReporta,
            Integer idAmbiente,
            Integer idEquipo,
            TipoIncidencia tipoIncidencia,
            String descripcion
    ) {}

    public record ResolucionRequest(
            String respuestaEncargado,
            EstadoTicket estadoTicket
    ) {}
}