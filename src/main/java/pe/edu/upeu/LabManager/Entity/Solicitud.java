package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "id_ambiente", nullable = false)
    private Ambiente ambiente;

    @Column(name = "motivo_solicitud", nullable = false)
    private String motivoSolicitud;

    @Column(name = "fecha_inicio_reserva", nullable = false)
    private LocalDateTime fechaInicioReserva;

    @Column(name = "fecha_fin_reserva", nullable = false)
    private LocalDateTime fechaFinReserva;

    @Column(name = "fecha_solicitud", insertable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false)
    private EstadoSolicitud estadoSolicitud;

    @Column(name = "comentario_respuesta", length = 500)
    private String comentarioRespuesta;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<DetalleSolicitud> detalles;

    public Solicitud() {
    }

    public Solicitud(Integer idSolicitud, Usuario estudiante, Ambiente ambiente, String motivoSolicitud, LocalDateTime fechaInicioReserva, LocalDateTime fechaFinReserva, LocalDateTime fechaSolicitud, EstadoSolicitud estadoSolicitud, String comentarioRespuesta, LocalDateTime fechaRespuesta, List<DetalleSolicitud> detalles) {
        this.idSolicitud = idSolicitud;
        this.estudiante = estudiante;
        this.ambiente = ambiente;
        this.motivoSolicitud = motivoSolicitud;
        this.fechaInicioReserva = fechaInicioReserva;
        this.fechaFinReserva = fechaFinReserva;
        this.fechaSolicitud = fechaSolicitud;
        this.estadoSolicitud = estadoSolicitud;
        this.comentarioRespuesta = comentarioRespuesta;
        this.fechaRespuesta = fechaRespuesta;
        this.detalles = detalles;
    }

    public Integer getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Integer idSolicitud) { this.idSolicitud = idSolicitud; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public Ambiente getAmbiente() { return ambiente; }
    public void setAmbiente(Ambiente ambiente) { this.ambiente = ambiente; }

    public String getMotivoSolicitud() { return motivoSolicitud; }
    public void setMotivoSolicitud(String motivoSolicitud) { this.motivoSolicitud = motivoSolicitud; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public EstadoSolicitud getEstadoSolicitud() { return estadoSolicitud; }
    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) { this.estadoSolicitud = estadoSolicitud; }

    public List<DetalleSolicitud> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleSolicitud> detalles) { this.detalles = detalles; }

    public LocalDateTime getFechaInicioReserva() {
        return fechaInicioReserva;
    }

    public void setFechaInicioReserva(LocalDateTime fechaInicioReserva) {
        this.fechaInicioReserva = fechaInicioReserva;
    }

    public LocalDateTime getFechaFinReserva() {
        return fechaFinReserva;
    }

    public void setFechaFinReserva(LocalDateTime fechaFinReserva) {
        this.fechaFinReserva = fechaFinReserva;
    }

    public String getComentarioRespuesta() {
        return comentarioRespuesta;
    }

    public void setComentarioRespuesta(String comentarioRespuesta) {
        this.comentarioRespuesta = comentarioRespuesta;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}