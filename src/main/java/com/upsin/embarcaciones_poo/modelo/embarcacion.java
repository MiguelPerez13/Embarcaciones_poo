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
public class embarcacion {
    @Id
    @GeneratedValue
    private Integer idEmbarcacion;

    @ManyToOne
    @JoinColumn(name = "idBarco", referencedColumnName = "idBarco")
    private Barco barco;

    private String puertoOrigen;
    private String paisOrigen;

    @Temporal(TemporalType.DATE)
    private Date fechaSalida;

    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;
}
