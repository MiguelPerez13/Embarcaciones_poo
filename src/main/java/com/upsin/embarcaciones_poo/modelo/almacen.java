package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class almacen {
    @Id
    @GeneratedValue
    private Integer idAlmacen;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;

    private String estatus;
}
