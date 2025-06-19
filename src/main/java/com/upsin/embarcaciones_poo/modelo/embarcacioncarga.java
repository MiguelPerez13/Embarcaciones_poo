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
public class embarcacioncarga {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idEmbarcacion", referencedColumnName = "idEmbarcacion")
    private embarcacion embarcacion;

    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;

    @ManyToOne
    @JoinColumn(name = "idAlmacen", referencedColumnName = "idAlmacen")
    private almacen almacen;
}
