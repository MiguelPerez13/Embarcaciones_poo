package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Producto {
    @Id
    @GeneratedValue
    private Integer idProducto;
    
    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;

    private String nombreProducto;
    private String tipoProducto;
    private Integer cantidad;
    private Double pesoUnitario;
    private String descripcion;
}
