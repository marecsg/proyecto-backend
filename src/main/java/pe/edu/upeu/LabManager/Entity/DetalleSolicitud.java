package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_solicitudes")
public class DetalleSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_solicitud")
    private Integer idDetalleSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    public DetalleSolicitud() {
    }

    public DetalleSolicitud(Integer idDetalleSolicitud, Solicitud solicitud, Equipo equipo) {
        this.idDetalleSolicitud = idDetalleSolicitud;
        this.solicitud = solicitud;
        this.equipo = equipo;
    }

    public Integer getIdDetalleSolicitud() {
        return idDetalleSolicitud;
    }

    public void setIdDetalleSolicitud(Integer idDetalleSolicitud) {
        this.idDetalleSolicitud = idDetalleSolicitud;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}