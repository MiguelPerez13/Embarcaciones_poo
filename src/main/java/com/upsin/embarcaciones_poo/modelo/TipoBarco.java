package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TipoBarco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoBarco;

    private String nombreTipo;

    private String descripcion;

    @Override
    public String toString() {
        return nombreTipo;
    }
}
