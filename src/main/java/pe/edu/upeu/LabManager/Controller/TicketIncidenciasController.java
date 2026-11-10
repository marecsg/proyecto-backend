package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Dto.EquipoDto;
import pe.edu.upeu.LabManager.Dto.TicketIncidenciaDto;
import pe.edu.upeu.LabManager.Dto.UsuarioDto;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;
import pe.edu.upeu.LabManager.Services.TicketIncidenciaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidencias")
public class TicketIncidenciasController {

    @Autowired
    private TicketIncidenciaService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketIncidenciaDto.Response> listById(@PathVariable Integer id) {
        return ResponseEntity.ok(mapToResponse(ticketService.listById(id)));
    }

    @GetMapping("/ambiente/{idAmbiente}")
    public ResponseEntity<List<TicketIncidenciaDto.Response>> listByAmbiente(@PathVariable Integer idAmbiente) {
        List<TicketIncidenciaDto.Response> response = ticketService.listByAmbiente(idAmbiente).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<TicketIncidenciaDto.Response> create(@RequestBody TicketIncidenciaDto.CreateRequest request) {
        TicketIncidencia nuevo = ticketService.create(
                request.idUsuarioReporta(), request.idAmbiente(), request.idEquipo(),
                request.tipoIncidencia(), request.descripcion()
        );
        return ResponseEntity.ok(mapToResponse(nuevo));
    }

    @PutMapping("/{id}/resolucion")
    public ResponseEntity<TicketIncidenciaDto.Response> updateResolucion(
            @PathVariable Integer id,
            @RequestBody TicketIncidenciaDto.ResolucionRequest request) {

        TicketIncidencia actualizado = ticketService.updateResolucion(
                id, request.estadoTicket(), request.respuestaEncargado()
        );
        return ResponseEntity.ok(mapToResponse(actualizado));
    }


    private TicketIncidenciaDto.Response mapToResponse(TicketIncidencia ticket) {

        UsuarioDto.Response usuarioDto = null;
        if (ticket.getUsuarioReporta() != null) {
            UsuarioDto.Response.RolDto rolDto = ticket.getUsuarioReporta().getRol() != null ?
                    new UsuarioDto.Response.RolDto(ticket.getUsuarioReporta().getRol().getIdRol(), ticket.getUsuarioReporta().getRol().getNombre()) : null;
            AmbienteDto ambUser = ticket.getUsuarioReporta().getAmbiente() != null ?
                    new AmbienteDto(ticket.getUsuarioReporta().getAmbiente().getIdAmbiente(), ticket.getUsuarioReporta().getAmbiente().getNombre()) : null;

            usuarioDto = new UsuarioDto.Response(
                    ticket.getUsuarioReporta().getIdUsuario(), ticket.getUsuarioReporta().getCodigoInstitucional(),
                    ticket.getUsuarioReporta().getNombres(), ticket.getUsuarioReporta().getApellidos(),
                    ticket.getUsuarioReporta().getCorreo(), rolDto, ambUser
            );
        }

        AmbienteDto ambienteDto = null;
        if (ticket.getAmbiente() != null) {
            ambienteDto = new AmbienteDto(ticket.getAmbiente().getIdAmbiente(), ticket.getAmbiente().getNombre());
        }

        EquipoDto.Response equipoDto = null;
        if (ticket.getEquipo() != null) {
            AmbienteDto ambEquipo = ticket.getEquipo().getAmbiente() != null ?
                    new AmbienteDto(ticket.getEquipo().getAmbiente().getIdAmbiente(), ticket.getEquipo().getAmbiente().getNombre()) : null;

            equipoDto = new EquipoDto.Response(
                    ticket.getEquipo().getIdEquipo(), ticket.getEquipo().getCodigoQr(),
                    ticket.getEquipo().getNombre(), ticket.getEquipo().getDescripcion(),
                    ambEquipo, ticket.getEquipo().getEstadoFisico(), ticket.getEquipo().getDisponibilidad()
            );
        }

        return new TicketIncidenciaDto.Response(
                ticket.getIdTicket(), usuarioDto, ambienteDto, equipoDto,
                ticket.getTipoIncidencia(), ticket.getDescripcion(), ticket.getEstadoTicket(),
                ticket.getFechaReporte(), ticket.getRespuestaEncargado(), ticket.getFechaResolucion()
        );
    }
}