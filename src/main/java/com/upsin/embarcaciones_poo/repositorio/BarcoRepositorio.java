package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Barco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface BarcoRepositorio extends JpaRepository<Barco,Integer>{

    @Procedure (name = "Barco.mostarBarcosActivos")
    List<Barco> mostarbarcosActivos();
}
