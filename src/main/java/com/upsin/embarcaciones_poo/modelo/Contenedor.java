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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContenedor;
    
    @ManyToOne
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    private Empresa empresa;
    
    private Float unidadMedida;
    private Float pesoTotal;
    private String observaciones;
}