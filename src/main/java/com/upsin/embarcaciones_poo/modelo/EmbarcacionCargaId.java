package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmbarcacionCargaId implements Serializable {
    private Integer idEmbarcacion;
    private Integer idAlmacen;
}


