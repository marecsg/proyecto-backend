package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;
import pe.edu.upeu.LabManager.Entity.Enums.EstadoTicket;
import pe.edu.upeu.LabManager.Entity.Enums.TipoIncidencia;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets_incidencias")
public class TicketIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;

    @ManyToOne
    @JoinColumn(name = "id_usuario_reporta", nullable = false)
    private Usuario usuarioReporta;

    @ManyToOne
    @JoinColumn(name = "id_ambiente", nullable = false)
    private Ambiente ambiente;

    // Puede ser null si es un reporte global del aula
    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidencia", nullable = false)
    private TipoIncidencia tipoIncidencia;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ticket", nullable = false)
    private EstadoTicket estadoTicket;

    @Column(name = "fecha_reporte", insertable = false, updatable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "respuesta_encargado", columnDefinition = "TEXT")
    private String respuestaEncargado;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public TicketIncidencia() {
    }

    public TicketIncidencia(Integer idTicket, Usuario usuarioReporta, Ambiente ambiente, Equipo equipo, TipoIncidencia tipoIncidencia, String descripcion, EstadoTicket estadoTicket, LocalDateTime fechaReporte, String respuestaEncargado, LocalDateTime fechaResolucion) {
        this.idTicket = idTicket;
        this.usuarioReporta = usuarioReporta;
        this.ambiente = ambiente;
        this.equipo = equipo;
        this.tipoIncidencia = tipoIncidencia;
        this.descripcion = descripcion;
        this.estadoTicket = estadoTicket;
        this.fechaReporte = fechaReporte;
        this.respuestaEncargado = respuestaEncargado;
        this.fechaResolucion = fechaResolucion;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Usuario getUsuarioReporta() {
        return usuarioReporta;
    }

    public void setUsuarioReporta(Usuario usuarioReporta) {
        this.usuarioReporta = usuarioReporta;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(TipoIncidencia tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTicket getEstadoTicket() {
        return estadoTicket;
    }

    public void setEstadoTicket(EstadoTicket estadoTicket) {
        this.estadoTicket = estadoTicket;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getRespuestaEncargado() {
        return respuestaEncargado;
    }

    public void setRespuestaEncargado(String respuestaEncargado) {
        this.respuestaEncargado = respuestaEncargado;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
}
