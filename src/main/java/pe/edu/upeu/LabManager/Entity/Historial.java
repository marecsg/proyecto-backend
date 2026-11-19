package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.TipoAccion;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_acciones")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_actor", nullable = false)
    private Usuario usuarioActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_afectado", nullable = false)
    private Usuario usuarioAfectado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false, length = 50)
    private TipoAccion tipoAccion;

    @Column(name = "tipo_entidad", nullable = false, length = 30)
    private String tipoEntidad;

    @Column(name = "id_entidad", nullable = false)
    private Integer idEntidad;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fechaAccion;


    public Historial() {
    }

    public Historial(Integer idHistorial, Usuario usuarioActor, Usuario usuarioAfectado, TipoAccion tipoAccion, String tipoEntidad, Integer idEntidad, String descripcion, LocalDateTime fechaAccion) {
        this.idHistorial = idHistorial;
        this.usuarioActor = usuarioActor;
        this.usuarioAfectado = usuarioAfectado;
        this.tipoAccion = tipoAccion;
        this.tipoEntidad = tipoEntidad;
        this.idEntidad = idEntidad;
        this.descripcion = descripcion;
        this.fechaAccion = fechaAccion;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Usuario getUsuarioActor() {
        return usuarioActor;
    }

    public void setUsuarioActor(Usuario usuarioActor) {
        this.usuarioActor = usuarioActor;
    }

    public Usuario getUsuarioAfectado() {
        return usuarioAfectado;
    }

    public void setUsuarioAfectado(Usuario usuarioAfectado) {
        this.usuarioAfectado = usuarioAfectado;
    }

    public TipoAccion getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(TipoAccion tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public Integer getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }



}
