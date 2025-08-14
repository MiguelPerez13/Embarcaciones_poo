package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "Barco")
@EqualsAndHashCode
public class Barco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBarco;

    @ManyToOne
    @JoinColumn(name = "idTipoBarco", referencedColumnName = "idTipoBarco")
    private TipoBarco tipoBarco;

    private String nombre;

    private Float capacidadCarga;

    private String estado;

    @Override
    public String toString() {
        return this.nombre; // o getNombre();
    }

}
