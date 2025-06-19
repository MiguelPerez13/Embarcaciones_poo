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
public class empresa {
    @Id
    @GeneratedValue
    private Integer idEmpresa;

    private String nombre;
    private String rfc;
    private String telefono;
    private String email;
    private String direccion;
    private String tipoEmpresa;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
}
