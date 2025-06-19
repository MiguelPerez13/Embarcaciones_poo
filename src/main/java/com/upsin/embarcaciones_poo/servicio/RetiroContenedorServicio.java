package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.RetiroContenedor;
import com.upsin.embarcaciones_poo.repositorio.RetiroContenedorRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetiroContenedorServicio {
    
    @Autowired
    private RetiroContenedorRepositorio repositorio;
    
    public List<RetiroContenedor> listarRetiroContenedor(){
        List<RetiroContenedor> retiroContenedors = repositorio.findAll();
        return retiroContenedors;
    }
    
    public RetiroContenedor buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(RetiroContenedor retiroContenedor){
        repositorio.save(retiroContenedor);
    }
    
    public void eliminar(RetiroContenedor retiroContenedor){
        repositorio.delete(retiroContenedor);
    }
}
