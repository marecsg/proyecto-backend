package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Entity.SolicitudPrestamo;
import pe.edu.upeu.LabManager.Services.SolicitudPrestamoService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamoService solicitudPrestamoService;

    @GetMapping
    public List<SolicitudPrestamo> listarSolicitudes() {
        return solicitudPrestamoService.listarSolicitudes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudPrestamo> buscarPorId(@PathVariable Integer id) {
        return solicitudPrestamoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<SolicitudPrestamo> listarPorEstudiante(@PathVariable Integer idEstudiante) {
        return solicitudPrestamoService.listarPorEstudiante(idEstudiante);
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudPrestamo> listarPorEstado(@PathVariable EstadoSolicitud estado) {
        return solicitudPrestamoService.listarPorEstado(estado);
    }

    @PostMapping
    public SolicitudPrestamo crearSolicitud(@RequestBody SolicitudPrestamo solicitud) {
        return solicitudPrestamoService.crearSolicitud(solicitud);
    }

    @PutMapping("/{id}/responder")
    public SolicitudPrestamo responderSolicitud(@PathVariable Integer id, @RequestBody SolicitudPrestamo datos) {
        return solicitudPrestamoService.responderSolicitud(
                id,
                datos.getEstadoSolicitud(),
                datos.getEncargadoAprobacion().getIdUsuario(),
                datos.getComentarioAprobacion()
        );
    }

    @PutMapping("/{id}/devolucion")
    public SolicitudPrestamo registrarDevolucion(
            @PathVariable Integer id,
            @RequestBody SolicitudPrestamo solicitudRecibida) {

        return solicitudPrestamoService.registrarDevolucion(
                id,
                solicitudRecibida.getEncargadoDevolucion().getIdUsuario(),
                solicitudRecibida.getEstadoFisicoAlDevolver(),
                solicitudRecibida.getComentarioDevolucion()
        );
    }

    @GetMapping("/encargado/{idEncargado}")
    public List<SolicitudPrestamo> listarPorEncargadoAmbiente(@PathVariable Integer idEncargado) {
        return solicitudPrestamoService.listarPorEncargadoAmbiente(idEncargado);
    }
}