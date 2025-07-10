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
public class Embarcacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmbarcacion;

    @ManyToOne
    @JoinColumn(name = "idBarco", referencedColumnName = "idBarco")
    private Barco barco;

    private String puertoOrigen;
    private String puertoDestino;

    @Temporal(TemporalType.DATE)
    private Date fechaSalida;

    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;

    @Override
    public String toString(){
        return puertoOrigen.concat(" - ".concat(puertoDestino).concat(" / ").concat(String.valueOf(fechaSalida)));
    }


}