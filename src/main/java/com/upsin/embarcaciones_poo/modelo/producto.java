package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class producto {
    @Id
    @GeneratedValue
    private Integer idProducto;

    private String nombreProducto;
    private String unidadMedida;
    private Double pesoUnitario;
    private Double pesoLote;
    private String descripcion;
}
