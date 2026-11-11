package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoFisico;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoItem;

@Entity
@Table(name = "detalle_prestamos")
public class DetallePrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_prestamo", nullable = false)
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_item", nullable = true)
    private EstadoItem estadoItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_fisico_al_devolver")
    private EstadoFisico estadoFisicoAlDevolver;

    @Column(name = "comentario_devolucion", columnDefinition = "TEXT")
    private String comentarioDevolucion;

    public DetallePrestamo() {
    }

    public DetallePrestamo(Integer idDetalle, Prestamo prestamo, Equipo equipo, EstadoItem estadoItem, EstadoFisico estadoFisicoAlDevolver, String comentarioDevolucion) {
        this.idDetalle = idDetalle;
        this.prestamo = prestamo;
        this.equipo = equipo;
        this.estadoItem = estadoItem;
        this.estadoFisicoAlDevolver = estadoFisicoAlDevolver;
        this.comentarioDevolucion = comentarioDevolucion;
    }

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public EstadoItem getEstadoItem() { return estadoItem; }
    public void setEstadoItem(EstadoItem estadoItem) { this.estadoItem = estadoItem; }

    public EstadoFisico getEstadoFisicoAlDevolver() { return estadoFisicoAlDevolver; }
    public void setEstadoFisicoAlDevolver(EstadoFisico estadoFisicoAlDevolver) { this.estadoFisicoAlDevolver = estadoFisicoAlDevolver; }

    public String getComentarioDevolucion() { return comentarioDevolucion; }
    public void setComentarioDevolucion(String comentarioDevolucion) { this.comentarioDevolucion = comentarioDevolucion; }
}