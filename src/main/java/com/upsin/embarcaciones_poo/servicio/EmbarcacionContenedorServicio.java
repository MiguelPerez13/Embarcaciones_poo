package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedor;
import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedorId;
import com.upsin.embarcaciones_poo.repositorio.EmbarcacionContenedorRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacionContenedorServicio {
    
    @Autowired
    private EmbarcacionContenedorRepositorio repositorio;
    
    public List<EmbarcacionContenedor> listarEmbarcacionContenedor(){
        List<EmbarcacionContenedor> embarcacionContenedors = repositorio.findAll();
        return embarcacionContenedors;
    }
    
    public EmbarcacionContenedor buscarPorId(EmbarcacionContenedorId id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(EmbarcacionContenedor embarcacionContenedor){
        repositorio.save(embarcacionContenedor);
    }
    
    public void eliminar(EmbarcacionContenedor embarcacionContenedor){
        repositorio.delete(embarcacionContenedor);
    }
}
