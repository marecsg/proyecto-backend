package pe.edu.upeu.LabManager.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;
import java.time.LocalDateTime;

public record TicketIncidenciaDto() {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Response(
            Integer idTicket,
            Integer idUsuarioReporta,
            String nombreUsuarioReporta,
            Integer idAmbiente,
            String nombreAmbiente,
            Integer idEquipo,
            String nombreEquipo,
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