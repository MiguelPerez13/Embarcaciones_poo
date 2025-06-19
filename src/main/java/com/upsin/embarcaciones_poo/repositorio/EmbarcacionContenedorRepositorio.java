package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedor;
import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbarcacionContenedorRepositorio extends JpaRepository<EmbarcacionContenedor,EmbarcacionContenedorId>{
    
}
