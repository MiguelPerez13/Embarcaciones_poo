package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmacenRepositorio extends JpaRepository<Almacen, Integer>{
    
}
