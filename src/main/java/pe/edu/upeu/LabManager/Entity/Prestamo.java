package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoPrestamo;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @ManyToOne
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @ManyToOne
    @JoinColumn(name = "id_encargado_aprobacion", nullable = false)
    private Usuario encargadoAprobacion;

    @Column(name = "fecha_inicio_uso")
    private LocalDateTime fechaInicioUso;

    @Column(name = "fecha_fin_uso")
    private LocalDateTime fechaFinUso;

    @Column(name = "comentario_aprobacion", columnDefinition = "TEXT")
    private String comentarioAprobacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo", nullable = false)
    private EstadoPrestamo estadoPrestamo;

    @ManyToOne
    @JoinColumn(name = "id_encargado_devolucion")
    private Usuario encargadoDevolucion;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePrestamo> detalles;

    public Prestamo() {
    }

    public Prestamo(Integer idPrestamo, Solicitud solicitud, Usuario encargadoAprobacion, LocalDateTime fechaInicioUso, LocalDateTime fechaFinUso, String comentarioAprobacion, EstadoPrestamo estadoPrestamo, Usuario encargadoDevolucion, List<DetallePrestamo> detalles) {
        this.idPrestamo = idPrestamo;
        this.solicitud = solicitud;
        this.encargadoAprobacion = encargadoAprobacion;
        this.fechaInicioUso = fechaInicioUso;
        this.fechaFinUso = fechaFinUso;
        this.comentarioAprobacion = comentarioAprobacion;
        this.estadoPrestamo = estadoPrestamo;
        this.encargadoDevolucion = encargadoDevolucion;
        this.detalles = detalles;
    }

    public Integer getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(Integer idPrestamo) { this.idPrestamo = idPrestamo; }

    public Solicitud getSolicitud() { return solicitud; }
    public void setSolicitud(Solicitud solicitud) { this.solicitud = solicitud; }

    public Usuario getEncargadoAprobacion() { return encargadoAprobacion; }
    public void setEncargadoAprobacion(Usuario encargadoAprobacion) { this.encargadoAprobacion = encargadoAprobacion; }

    public LocalDateTime getFechaInicioUso() { return fechaInicioUso; }
    public void setFechaInicioUso(LocalDateTime fechaInicioUso) { this.fechaInicioUso = fechaInicioUso; }

    public LocalDateTime getFechaFinUso() { return fechaFinUso; }
    public void setFechaFinUso(LocalDateTime fechaFinUso) { this.fechaFinUso = fechaFinUso; }

    public String getComentarioAprobacion() { return comentarioAprobacion; }
    public void setComentarioAprobacion(String comentarioAprobacion) { this.comentarioAprobacion = comentarioAprobacion; }

    public EstadoPrestamo getEstadoPrestamo() { return estadoPrestamo; }
    public void setEstadoPrestamo(EstadoPrestamo estadoPrestamo) { this.estadoPrestamo = estadoPrestamo; }

    public Usuario getEncargadoDevolucion() {
        return encargadoDevolucion;
    }

    public void setEncargadoDevolucion(Usuario encargadoDevolucion) {
        this.encargadoDevolucion = encargadoDevolucion;
    }

    public List<DetallePrestamo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePrestamo> detalles) {
        this.detalles = detalles;
    }
}