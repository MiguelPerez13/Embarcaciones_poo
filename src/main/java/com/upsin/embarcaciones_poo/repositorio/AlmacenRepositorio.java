package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface AlmacenRepositorio extends JpaRepository<Almacen, Integer>{

    @Procedure (name = "Almacen.mostrarLotesAlmacen")
    List<Almacen> mostrarLotesAlmacen();
}
