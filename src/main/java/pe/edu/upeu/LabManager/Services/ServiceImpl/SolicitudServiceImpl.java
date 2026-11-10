package pe.edu.upeu.LabManager.Services.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.LabManager.Entity.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import pe.edu.upeu.LabManager.Repository.*;
import pe.edu.upeu.LabManager.Services.SolicitudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private DetalleSolicitudRepository detalleSolicitudRepository;

    @Override
    public Solicitud listById(Integer id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    @Override
    public List<Solicitud> listByEstudiante(Integer idEstudiante) {
        return solicitudRepository.findByEstudiante_IdUsuario(idEstudiante);
    }

    @Override
    public List<Solicitud> listByAmbiente(Integer idAmbiente) {
        return solicitudRepository.findByAmbiente_IdAmbiente(idAmbiente);
    }

    @Override
    @Transactional
    public Solicitud create(Integer idEstudiante, Integer idAmbiente, String motivoSolicitud,
                            LocalDateTime fechaInicioReserva, LocalDateTime fechaFinReserva,
                            List<Integer> idsEquipos) {

        Usuario estudiante = usuarioRepository.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Ambiente ambiente = ambienteRepository.findById(idAmbiente)
                .orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));

        for (Integer idEquipo : idsEquipos) {
            Equipo equipo = equipoRepository.findById(idEquipo)
                    .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + idEquipo));

            if (!equipo.getAmbiente().getIdAmbiente().equals(idAmbiente)) {
                throw new RuntimeException("El equipo " + equipo.getNombre() + " no pertenece al laboratorio seleccionado.");
            }
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setEstudiante(estudiante);
        solicitud.setAmbiente(ambiente);
        solicitud.setMotivoSolicitud(motivoSolicitud);
        solicitud.setFechaInicioReserva(fechaInicioReserva);
        solicitud.setFechaFinReserva(fechaFinReserva);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        List<DetalleSolicitud> detalles = new ArrayList<>();
        for (Integer idEquipo : idsEquipos) {
            Equipo equipo = equipoRepository.findById(idEquipo).get();
            DetalleSolicitud detalle = new DetalleSolicitud();
            detalle.setSolicitud(solicitudGuardada);
            detalle.setEquipo(equipo);

            detalles.add(detalleSolicitudRepository.save(detalle));
        }

        solicitudGuardada.setDetalles(detalles);
        return solicitudGuardada;
    }

    @Override
    public Solicitud cambiarEstado(Integer idSolicitud, EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = listById(idSolicitud);
        solicitud.setEstadoSolicitud(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }
}
