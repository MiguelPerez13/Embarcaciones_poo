package com.upsin.embarcaciones_poo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Usuario {

    @EmbeddedId
    private UsuarioId id;

    private Integer permiso;
}
