package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Barco;
import com.upsin.embarcaciones_poo.repositorio.BarcoRepositorio;
import java.util.List;

import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BarcoServicio {
    
    @Autowired
    private BarcoRepositorio repositorio;
    
    public List<Barco> listarBarco(){
        return repositorio.findAll();
    }
    
    public Barco buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(Barco barco){
        repositorio.save(barco);
    }
    
    public void eliminar(Barco barco){
        repositorio.delete(barco);
    }

    @Transactional
    public List<Barco> mostarBarcosActivos(){
        return repositorio.mostarbarcosActivos();
    }
}
