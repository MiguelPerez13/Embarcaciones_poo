package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class embarcacioncontenedor {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idEmbarcacion", referencedColumnName = "idEmbarcacion")
    private embarcacion embarcacion;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;
}