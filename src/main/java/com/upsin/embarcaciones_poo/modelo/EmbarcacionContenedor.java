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
    @JoinColumn(name = "idEmbarcacion")
    private Embarcacion embarcacion;

    @ManyToOne
    @MapsId("idContenedor")
    @JoinColumn(name = "idContenedor")
    private Contenedor contenedor;
}