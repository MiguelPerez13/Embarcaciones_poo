package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Barco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarcoRepositorio extends JpaRepository<Barco,Integer>{
    
}
