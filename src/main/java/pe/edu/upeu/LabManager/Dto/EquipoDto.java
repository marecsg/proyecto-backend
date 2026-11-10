package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.Disponibilidad;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;

public record EquipoDto() {

    public record Response(
            Integer idEquipo,
            String codigoQr,
            String nombre,
            String descripcion,
            AmbienteDto ambiente,
            EstadoFisico estadoFisico,
            Disponibilidad disponibilidad
    ) {
    }

    public record UpdateRequest(
            EstadoFisico estadoFisico,
            Disponibilidad disponibilidad
    ) {}
}
