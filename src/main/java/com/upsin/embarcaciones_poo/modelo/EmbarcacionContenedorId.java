package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmbarcacionContenedorId implements Serializable{
    private Integer idEmbarcacion;
    private Integer idContenedor;
}
