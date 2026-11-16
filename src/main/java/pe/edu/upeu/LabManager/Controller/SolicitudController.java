package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.LabManager.Dto.AmbienteDto;
import pe.edu.upeu.LabManager.Dto.EquipoDto;
import pe.edu.upeu.LabManager.Dto.SolicitudDto;
import pe.edu.upeu.LabManager.Dto.UsuarioDto;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.Solicitud;
import pe.edu.upeu.LabManager.Services.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto.Response> getById(@PathVariable Integer id) {
        Solicitud solicitud = solicitudService.listById(id);
        return ResponseEntity.ok(mapToResponse(solicitud));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<SolicitudDto.Response>> getByEstudiante(@PathVariable Integer idEstudiante) {
        List<SolicitudDto.Response> respuestas = solicitudService.listByEstudiante(idEstudiante)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/ambiente/{idAmbiente}")
    public ResponseEntity<List<SolicitudDto.Response>> getByAmbiente(@PathVariable Integer idAmbiente) {
        List<SolicitudDto.Response> respuestas = solicitudService.listByAmbiente(idAmbiente)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    @PostMapping
    public ResponseEntity<SolicitudDto.Response> create(@RequestBody SolicitudDto.CreateRequest request) {

        List<Integer> idsEquipos = request.equipos().stream()
                .map(SolicitudDto.CreateRequest.ItemRequest::idEquipo)
                .toList();

        Solicitud nuevaSolicitud = solicitudService.create(
                request.idEstudiante(),
                request.idAmbiente(),
                request.motivoSolicitud(),
                request.fechaInicioReserva(),
                request.fechaFinReserva(),
                idsEquipos
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nuevaSolicitud));
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudDto.Response> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody SolicitudDto.UpdateRequest request) {

        if (request.estadoSolicitud() == EstadoSolicitud.APROBADA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Para aprobar una solicitud utiliza /api/prestamos/aprobar"
            );
        }

        Solicitud solicitudActualizada = solicitudService.cambiarEstado(
                id,
                request.estadoSolicitud(),
                request.comentarioRespuesta()
        );

        return ResponseEntity.ok(mapToResponse(solicitudActualizada));
    }

    private SolicitudDto.Response mapToResponse(Solicitud solicitud) {
        if (solicitud == null) return null;

        List<SolicitudDto.Response.DetalleResponse> detallesDto = solicitud.getDetalles().stream()
                .map(det -> new SolicitudDto.Response.DetalleResponse(
                        det.getEquipo().getIdEquipo(),
                        det.getEquipo().getCodigoQr(),
                        det.getEquipo().getNombre()
                )).toList();

        Integer idEstudiante = solicitud.getEstudiante().getIdUsuario();
        String nombreEstudiante = solicitud.getEstudiante().getNombres() + " " + solicitud.getEstudiante().getApellidos();

        Integer idAmbiente = solicitud.getAmbiente().getIdAmbiente();
        String nombreAmbiente = solicitud.getAmbiente().getNombre();

        return new SolicitudDto.Response(
                solicitud.getIdSolicitud(),
                idEstudiante,
                nombreEstudiante,
                solicitud.getEstudiante().getCodigoInstitucional(),
                idAmbiente,
                nombreAmbiente,
                solicitud.getMotivoSolicitud(),
                solicitud.getFechaInicioReserva(),
                solicitud.getFechaFinReserva(),
                solicitud.getFechaSolicitud(),
                solicitud.getEstadoSolicitud(),
                solicitud.getFechaRespuesta(),
                solicitud.getComentarioRespuesta(),
                detallesDto
        );
    }
}