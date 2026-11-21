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

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<TicketIncidenciaDto.Response>> listByEstudiante(@PathVariable Integer idEstudiante) {
        List<TicketIncidenciaDto.Response> response = ticketService.listByEstudiante(idEstudiante).stream()
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
        if (ticket == null) return null;

        Integer idUsuarioReporta = null;
        String nombreUsuarioReporta = null;
        if (ticket.getUsuarioReporta() != null) {
            idUsuarioReporta = ticket.getUsuarioReporta().getIdUsuario();
            nombreUsuarioReporta = ticket.getUsuarioReporta().getNombres() + " " + ticket.getUsuarioReporta().getApellidos();
        }

        Integer idAmbiente = null;
        String nombreAmbiente = null;
        if (ticket.getAmbiente() != null) {
            idAmbiente = ticket.getAmbiente().getIdAmbiente();
            nombreAmbiente = ticket.getAmbiente().getNombre();
        }

        Integer idEquipo = null;
        String nombreEquipo = null;
        if (ticket.getEquipo() != null) {
            idEquipo = ticket.getEquipo().getIdEquipo();
            nombreEquipo = ticket.getEquipo().getNombre();
        }

        return new TicketIncidenciaDto.Response(
                ticket.getIdTicket(),
                ticket.getCodigoCorrelativo(),
                idUsuarioReporta,
                nombreUsuarioReporta,
                idAmbiente,
                nombreAmbiente,
                idEquipo,
                nombreEquipo,
                ticket.getTipoIncidencia(),
                ticket.getDescripcion(),
                ticket.getEstadoTicket(),
                ticket.getFechaReporte(),
                ticket.getRespuestaEncargado(),
                ticket.getFechaResolucion()
        );
    }
}