package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmbarcacionContenedor {

    @EmbeddedId
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "idEmbarcacion", referencedColumnName = "idEmbarcacion")
    private Embarcacion embarcacion;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;
}