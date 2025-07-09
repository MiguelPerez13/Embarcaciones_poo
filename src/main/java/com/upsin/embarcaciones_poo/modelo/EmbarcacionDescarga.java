package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbarcacionDescarga {

    @EmbeddedId
    private EmbarcacionDescargaId id;

    @ManyToOne
    @MapsId("idEmbarcacion")
    @JoinColumn(name = "idEmbarcacion")
    private Embarcacion embarcacion;

    @ManyToOne
    @MapsId("idContenedor")
    @JoinColumn(name = "idContenedor")
    private Contenedor contenedor;

    @Temporal(TemporalType.DATE)
    private Date fechaDescarga;

    @Column(name = "loteAlmacen")
    private String loteAlmacen;
}

