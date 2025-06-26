package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import com.upsin.embarcaciones_poo.repositorio.AlmacenRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlmacenServicio {
    
    @Autowired
    private AlmacenRepositorio almacenRepositorio;
    
    public List<Almacen> listarAlmacen(){
        List<Almacen> almacenes = almacenRepositorio.findAll();
        return almacenes;
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
}
