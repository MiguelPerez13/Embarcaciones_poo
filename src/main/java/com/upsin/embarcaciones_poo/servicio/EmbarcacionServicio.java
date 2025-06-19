package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import com.upsin.embarcaciones_poo.repositorio.EmbarcacionRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacionServicio {
    
    @Autowired
    private EmbarcacionRepositorio repositorio;
    
    public List<Embarcacion> listarEmbarcacion(){
        List<Embarcacion> embarcacions = repositorio.findAll();
        return embarcacions;
    }
    
    public Embarcacion buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(Embarcacion embarcacion){
        repositorio.save(embarcacion);
    }
    
    public void eliminar(Embarcacion embarcacion){
        repositorio.delete(embarcacion);
    }
}
