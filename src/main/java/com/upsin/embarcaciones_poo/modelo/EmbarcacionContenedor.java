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
    private EmbarcacionContenedorId id;
    
    @ManyToOne
    @MapsId("idEmbarcacion")
    @JoinColumn(name = "idEmbarcacion", insertable = false, updatable = false)
    private Embarcacion embarcacion;

    @ManyToOne
    @MapsId("idContenedor")
    @JoinColumn(name = "idEmbarcacion", insertable = false, updatable = false)
    private Contenedor contenedor;
}