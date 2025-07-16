package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmbarcacionCarga {
    @EmbeddedId
    private EmbarcacionCargaId id;

    @ManyToOne
    @JoinColumn(name = "idEmbarcacion", insertable = false, updatable = false)
    private Embarcacion embarcacion;

    @ManyToOne
    @JoinColumn(name = "idAlmacen", insertable = false, updatable = false)
    private Almacen almacen;

    @Temporal(TemporalType.DATE)
    private Date fechaCarga;
}

