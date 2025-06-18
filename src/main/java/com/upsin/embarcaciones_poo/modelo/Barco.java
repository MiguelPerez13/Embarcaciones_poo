package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Barco {

    @Id
    @GeneratedValue
    private Integer idBarco;

    @ManyToOne
    @JoinColumn(name = "idTipoBarco", referencedColumnName = "idTipoBarco")
    private TipoBarco tipoBarco;

    private String nombre;

    private Double capacidadCarga;

    private String estado;
}
