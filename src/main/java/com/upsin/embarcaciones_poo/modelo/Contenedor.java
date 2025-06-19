package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Contenedor {
    @Id
    @GeneratedValue
    private Integer idContenedor;
    
    @ManyToOne
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    private Empresa idEmpresa;
    
    private Double unidadMedida;
    private Double pesoTotal;
    private String observaciones;
}