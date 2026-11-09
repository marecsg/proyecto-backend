package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ambientes")
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ambiente")
    private Integer idAmbiente;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_encargado", nullable = false)
    private Usuario encargado;

    public Ambiente() {
    }

    public Ambiente(Integer idAmbiente, String nombre, Usuario encargado) {
        this.idAmbiente = idAmbiente;
        this.nombre = nombre;
        this.encargado = encargado;
    }

    public Integer getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(Integer idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getEncargado() {
        return encargado;
    }

    public void setEncargado(Usuario encargado) {
        this.encargado = encargado;
    }
}
