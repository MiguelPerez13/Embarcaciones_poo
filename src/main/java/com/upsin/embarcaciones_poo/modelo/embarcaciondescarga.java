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
public class embarcaciondescarga {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idEmbarcacion", referencedColumnName = "idEmbarcacion")
    private embarcacion embarcacion;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
}
