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
public class EmbarcacionDescarga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;

    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;

    @ManyToOne
    @JoinColumn(name = "loteAlmacen", referencedColumnName = "loteAlmacen")
    private Almacen almacen;
}
