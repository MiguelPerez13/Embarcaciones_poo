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
public class RetiroContenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRetiro;

    @ManyToOne
    @JoinColumn(name = "loteAlmacen", referencedColumnName = "loteAlmacen")
    private Almacen almacen;
    
    private String nombreReceptor;
    private String tipoVehiculo;
    private String matricula;
    private String empresaTransporte;
    
    @Temporal(TemporalType.DATE)
    private Date fechaRetiro;
    
    private String observaciones;
}
