package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbarcacionRepositorio extends JpaRepository<Embarcacion,Integer>{
    
}
