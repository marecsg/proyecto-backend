package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Dto.*;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Services.PrestamoService;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDto.Response> getById(@PathVariable Integer id) {
        Prestamo prestamo = prestamoService.listById(id);
        return ResponseEntity.ok(mapToResponse(prestamo));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<PrestamoDto.Response>> getByEstudiante(@PathVariable Integer idEstudiante) {
        List<Prestamo> prestamos = prestamoService.listByEstudiante(idEstudiante);
        List<PrestamoDto.Response> responseList = prestamos.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/ambiente/{idAmbiente}")
    public ResponseEntity<List<PrestamoDto.Response>> getByAmbiente(@PathVariable Integer idAmbiente) {
        List<Prestamo> prestamos = prestamoService.listByAmbiente(idAmbiente);
        List<PrestamoDto.Response> responseList = prestamos.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/aprobar")
    public ResponseEntity<PrestamoDto.Response> crearDesdeSolicitud(@RequestBody PrestamoDto.CreateRequest request) {
        Prestamo prestamo = prestamoService.crearDesdeSolicitud(
                request.idSolicitud(),
                request.idEncargadoAprobacion(),
                request.comentarioAprobacion()
        );
        return ResponseEntity.ok(mapToResponse(prestamo));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PrestamoDto.Response> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody PrestamoDto.UpdateEstadoRequest request) {
        Prestamo prestamo = prestamoService.cambiarEstado(id, request.estadoPrestamo());
        return ResponseEntity.ok(mapToResponse(prestamo));
    }

    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoDto.Response> registrarDevolucion(
            @PathVariable Integer id,
            @RequestBody PrestamoDto.UpdateEstadoRequest request) {

        List<DetallePrestamo> detallesEntidades = request.detallesDevolucion().stream().map(dto -> {
            DetallePrestamo det = new DetallePrestamo();
            det.setIdDetalle(dto.idDetalle());
            det.setEstadoItem(dto.estadoItem());
            det.setEstadoFisicoAlDevolver(dto.estadoFisicoAlDevolver());
            det.setComentarioDevolucion(dto.comentarioDevolucion());
            return det;
        }).toList();

        Prestamo prestamoActualizado = prestamoService.registrarDevolucion(
                id,
                request.idEncargadoDevolucion(),
                request.estadoPrestamo(),
                detallesEntidades
        );

        return ResponseEntity.ok(mapToResponse(prestamoActualizado));
    }

    private PrestamoDto.Response mapToResponse(Prestamo prestamo) {
        if (prestamo == null) return null;

        List<PrestamoDto.Response.DetalleResponse> detallesDto = null;
        if (prestamo.getDetalles() != null) {
            detallesDto = prestamo.getDetalles().stream().map(det ->
                    new PrestamoDto.Response.DetalleResponse(
                            det.getIdDetalle(),
                            mapEquipoToDto(det.getEquipo()),
                            det.getEstadoItem(),
                            det.getEstadoFisicoAlDevolver(),
                            det.getComentarioDevolucion()
                    )
            ).toList();
        }

        return new PrestamoDto.Response(
                prestamo.getIdPrestamo(),
                mapSolicitudToDto(prestamo.getSolicitud()),
                mapUsuarioToDto(prestamo.getEncargadoAprobacion()),
                mapUsuarioToDto(prestamo.getEncargadoDevolucion()),
                prestamo.getFechaInicioUso(),
                prestamo.getFechaFinUso(),
                prestamo.getComentarioAprobacion(),
                prestamo.getEstadoPrestamo(),
                detallesDto
        );
    }

    private UsuarioDto.Response mapUsuarioToDto(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDto.Response.RolDto rolDto = null;
        if (usuario.getRol() != null) {
            rolDto = new UsuarioDto.Response.RolDto(
                    usuario.getRol().getIdRol(),
                    usuario.getRol().getNombre()
            );
        }

        return new UsuarioDto.Response(
                usuario.getIdUsuario(),
                usuario.getCodigoInstitucional(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                rolDto,
                mapAmbienteToDto(usuario.getAmbiente())
        );
    }

    private EquipoDto.Response mapEquipoToDto(Equipo equipo) {
        if (equipo == null) return null;

        return new EquipoDto.Response(
                equipo.getIdEquipo(),
                equipo.getCodigoQr(),
                equipo.getNombre(),
                equipo.getDescripcion(),
                mapAmbienteToDto(equipo.getAmbiente()),
                equipo.getEstadoFisico(),
                equipo.getDisponibilidad()
        );
    }

    private AmbienteDto mapAmbienteToDto(Ambiente ambiente) {
        if (ambiente == null) return null;

        return new AmbienteDto(
                ambiente.getIdAmbiente(),
                ambiente.getNombre()
        );
    }

    private SolicitudDto.Response mapSolicitudToDto(Solicitud solicitud) {
        if (solicitud == null) return null;

        List<SolicitudDto.Response.DetalleResponse> detallesSolicitudDto = null;
        if (solicitud.getDetalles() != null) {
            detallesSolicitudDto = solicitud.getDetalles().stream().map(det ->
                    new SolicitudDto.Response.DetalleResponse(
                            det.getIdDetalleSolicitud(),
                            mapEquipoToDto(det.getEquipo())
                    )
            ).toList();
        }

        return new SolicitudDto.Response(
                solicitud.getIdSolicitud(),
                mapUsuarioToDto(solicitud.getEstudiante()),
                mapAmbienteToDto(solicitud.getAmbiente()),
                solicitud.getMotivoSolicitud(),
                solicitud.getFechaInicioReserva(),
                solicitud.getFechaFinReserva(),
                solicitud.getFechaSolicitud(),
                solicitud.getEstadoSolicitud(),
                detallesSolicitudDto
        );
    }
}