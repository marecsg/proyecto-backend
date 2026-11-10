package pe.edu.upeu.LabManager.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ambientes")
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ambiente")
    private Integer idAmbiente;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    public Ambiente() {
    }

    public Ambiente(Integer idAmbiente, String nombre) {
        this.idAmbiente = idAmbiente;
        this.nombre = nombre;
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
}