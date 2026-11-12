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

        List<PrestamoDto.Response.DetalleResponse> detallesDto = prestamo.getDetalles().stream()
                .map(det -> new PrestamoDto.Response.DetalleResponse(
                        det.getIdDetalle(),
                        det.getEquipo().getIdEquipo(),
                        det.getEquipo().getCodigoQr(),
                        det.getEquipo().getNombre(),
                        det.getEstadoItem(),
                        det.getEstadoFisicoAlDevolver(),
                        det.getComentarioDevolucion()
                )).toList();

        Integer idSolicitud = prestamo.getSolicitud().getIdSolicitud();
        String nombreEstudiante = prestamo.getSolicitud().getEstudiante().getNombres() + " " + prestamo.getSolicitud().getEstudiante().getApellidos();
        String codigoEstudiante = prestamo.getSolicitud().getEstudiante().getCodigoInstitucional(); // <--- Aquí sacamos el código
        String nombreAmbiente = prestamo.getSolicitud().getAmbiente().getNombre();

        Integer idEncargadoAprobacion = prestamo.getEncargadoAprobacion().getIdUsuario();
        String nombreEncargadoAprobacion = prestamo.getEncargadoAprobacion().getNombres() + " " + prestamo.getEncargadoAprobacion().getApellidos();

        Integer idEncargadoDevolucion = null;
        String nombreEncargadoDevolucion = null;
        if (prestamo.getEncargadoDevolucion() != null) {
            idEncargadoDevolucion = prestamo.getEncargadoDevolucion().getIdUsuario();
            nombreEncargadoDevolucion = prestamo.getEncargadoDevolucion().getNombres() + " " + prestamo.getEncargadoDevolucion().getApellidos();
        }

        return new PrestamoDto.Response(
                prestamo.getIdPrestamo(),
                idSolicitud,
                nombreEstudiante,
                codigoEstudiante,
                nombreAmbiente,
                idEncargadoAprobacion,
                nombreEncargadoAprobacion,
                idEncargadoDevolucion,
                nombreEncargadoDevolucion,
                prestamo.getFechaInicioUso(),
                prestamo.getFechaFinUso(),
                prestamo.getComentarioAprobacion(),
                prestamo.getEstadoPrestamo(),
                detallesDto
        );
    }
}