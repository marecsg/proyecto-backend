package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoSolicitud;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_prestamo")
public class SolicitudPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @Column(name = "motivo_solicitud", nullable = false, columnDefinition = "TEXT")
    private String motivoSolicitud;

    @Column(name = "fecha_solicitud", insertable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_inicio_uso", nullable = false)
    private LocalDateTime fechaInicioUso;

    @Column(name = "fecha_fin_uso", nullable = false)
    private LocalDateTime fechaFinUso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false)
    private EstadoSolicitud estadoSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_encargado_aprobacion")
    private Usuario encargadoAprobacion;

    @Column(name = "comentario_aprobacion", columnDefinition = "TEXT")
    private String comentarioAprobacion;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @ManyToOne
    @JoinColumn(name = "id_encargado_devolucion")
    private Usuario encargadoDevolucion;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fechaDevolucionReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_fisico_al_devolver")
    private EstadoFisico estadoFisicoAlDevolver;

    @Column(name = "comentario_devolucion", columnDefinition = "TEXT")
    private String comentarioDevolucion;

    public SolicitudPrestamo() {
    }

    public SolicitudPrestamo(Integer idSolicitud, Usuario estudiante, Equipo equipo, String motivoSolicitud, LocalDateTime fechaSolicitud, LocalDateTime fechaInicioUso, LocalDateTime fechaFinUso, EstadoSolicitud estadoSolicitud, Usuario encargadoAprobacion, String comentarioAprobacion, LocalDateTime fechaAprobacion, Usuario encargadoDevolucion, LocalDateTime fechaDevolucionReal, EstadoFisico estadoFisicoAlDevolver, String comentarioDevolucion) {
        this.idSolicitud = idSolicitud;
        this.estudiante = estudiante;
        this.equipo = equipo;
        this.motivoSolicitud = motivoSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaInicioUso = fechaInicioUso;
        this.fechaFinUso = fechaFinUso;
        this.estadoSolicitud = estadoSolicitud;
        this.encargadoAprobacion = encargadoAprobacion;
        this.comentarioAprobacion = comentarioAprobacion;
        this.fechaAprobacion = fechaAprobacion;
        this.encargadoDevolucion = encargadoDevolucion;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estadoFisicoAlDevolver = estadoFisicoAlDevolver;
        this.comentarioDevolucion = comentarioDevolucion;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getMotivoSolicitud() {
        return motivoSolicitud;
    }

    public void setMotivoSolicitud(String motivoSolicitud) {
        this.motivoSolicitud = motivoSolicitud;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaInicioUso() {
        return fechaInicioUso;
    }

    public void setFechaInicioUso(LocalDateTime fechaInicioUso) {
        this.fechaInicioUso = fechaInicioUso;
    }

    public LocalDateTime getFechaFinUso() {
        return fechaFinUso;
    }

    public void setFechaFinUso(LocalDateTime fechaFinUso) {
        this.fechaFinUso = fechaFinUso;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public Usuario getEncargadoAprobacion() {
        return encargadoAprobacion;
    }

    public void setEncargadoAprobacion(Usuario encargadoAprobacion) {
        this.encargadoAprobacion = encargadoAprobacion;
    }

    public String getComentarioAprobacion() {
        return comentarioAprobacion;
    }

    public void setComentarioAprobacion(String comentarioAprobacion) {
        this.comentarioAprobacion = comentarioAprobacion;
    }

    public LocalDateTime getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Usuario getEncargadoDevolucion() {
        return encargadoDevolucion;
    }

    public void setEncargadoDevolucion(Usuario encargadoDevolucion) {
        this.encargadoDevolucion = encargadoDevolucion;
    }

    public LocalDateTime getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDateTime fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public EstadoFisico getEstadoFisicoAlDevolver() {
        return estadoFisicoAlDevolver;
    }

    public void setEstadoFisicoAlDevolver(EstadoFisico estadoFisicoAlDevolver) {
        this.estadoFisicoAlDevolver = estadoFisicoAlDevolver;
    }

    public String getComentarioDevolucion() {
        return comentarioDevolucion;
    }

    public void setComentarioDevolucion(String comentarioDevolucion) {
        this.comentarioDevolucion = comentarioDevolucion;
    }
}
