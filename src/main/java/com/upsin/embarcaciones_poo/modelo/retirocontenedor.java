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
public class retirocontenedor {
    @Id
    @GeneratedValue
    private Integer idRetiro;

    private String nombreReceptor;
    private String tipoTransporte;
    private String empresaTransportista;

    @Temporal(TemporalType.DATE)
    private Date fechaRetiro;
}
