package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import com.upsin.embarcaciones_poo.repositorio.AlmacenRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlmacenServicio {
    
    @Autowired
    private AlmacenRepositorio almacenRepositorio;
    
    public List<Almacen> listarAlmacen(){
        return almacenRepositorio.findAll();
    }
    
    public Almacen buscarPorId(Integer id){
        return almacenRepositorio.findById(id).orElse(null);
    }
    
    public void guardar(Almacen almacen){
        almacenRepositorio.save(almacen);
    }
    
    public void eliminar(Almacen almacen){
        almacenRepositorio.delete(almacen);
    }

    @Transactional
    public List<Almacen> listarActivos(){
        return almacenRepositorio.mostrarLotesAlmacen();
    }

}
