package pe.edu.upeu.LabManager.Dto;

import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;

import java.time.LocalDateTime;

public record HistorialDto(Integer idHistorial,
                           Integer idUsuarioActor,
                           String nombreActor,
                           Integer idUsuarioAfectado,
                           String nombreAfectado,
                           TipoAccion tipoAccion,
                           String tipoEntidad,
                           String codigoEntidad,
                           Integer idEntidad,
                           String descripcion,
                           LocalDateTime fechaAccion) {

}
