package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.EmbarcacionCarga;
import com.upsin.embarcaciones_poo.repositorio.EmbarcacionCargaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacionCargaServicio {
    
    @Autowired
    private EmbarcacionCargaRepositorio repositorio;
    
    public List<EmbarcacionCarga> listarEmbarcacionCarga(){
        List<EmbarcacionCarga> embarcacionCargas = repositorio.findAll();
        return embarcacionCargas;
    }
    
    public EmbarcacionCarga buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(EmbarcacionCarga embarcacionCarga){
        repositorio.save(embarcacionCarga);
    }
    
    public void eliminar(EmbarcacionCarga embarcacionCarga){
        repositorio.delete(embarcacionCarga);
    }
}
