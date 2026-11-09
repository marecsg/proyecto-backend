package pe.edu.upeu.LabManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.TicketIncidencia;
import pe.edu.upeu.LabManager.Services.TicketIncidenciaService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketIncidenciasController {

    @Autowired
    private TicketIncidenciaService ticketIncidenciaService;

    @GetMapping
    public List<TicketIncidencia> listarIncidencias() {
        return ticketIncidenciaService.listarIncidencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketIncidencia> buscarPorId(@PathVariable Integer id) {
        return ticketIncidenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/equipo/{idEquipo}")
    public List<TicketIncidencia> listarPorEquipo(@PathVariable Integer idEquipo) {
        return ticketIncidenciaService.listarPorEquipo(idEquipo);
    }

    @GetMapping("/ambiente/{idAmbiente}")
    public List<TicketIncidencia> listarPorAmbiente(@PathVariable Integer idAmbiente) {
        return ticketIncidenciaService.listarPorAmbiente(idAmbiente);
    }

    @GetMapping("/estado/{estado}")
    public List<TicketIncidencia> listarPorEstado(@PathVariable EstadoTicket estado) {
        return ticketIncidenciaService.listarPorEstado(estado);
    }

    @PostMapping
    public TicketIncidencia crearTicket(@RequestBody TicketIncidencia ticket) {
        return ticketIncidenciaService.crearTicket(ticket);
    }

    @PutMapping("/{id}/estado")
    public TicketIncidencia actualizarEstadoTicket(
            @PathVariable Integer id,
            @RequestBody TicketIncidencia ticketActualizado) {

        return ticketIncidenciaService.actualizarEstadoTicket(
                id,
                ticketActualizado.getEstadoTicket(),
                ticketActualizado.getRespuestaEncargado()
        );
    }
}
