package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlmacen;

    private String loteAlmacen;

    @ManyToOne
    @JoinColumn(name = "idContenedor", referencedColumnName = "idContenedor")
    private Contenedor contenedor;

    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;
    
    private String estado;

    public String toString(){
        return loteAlmacen.concat(" : ".concat(contenedor.toString()));
    }
}
