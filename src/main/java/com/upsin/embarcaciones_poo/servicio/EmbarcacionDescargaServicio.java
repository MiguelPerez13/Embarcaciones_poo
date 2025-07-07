package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.EmbarcacionDescarga;
import com.upsin.embarcaciones_poo.repositorio.EmbarcacionDescargaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacionDescargaServicio {

    @Autowired
    private EmbarcacionDescargaRepositorio repositorio;

    public List<EmbarcacionDescarga> listarEmbarcacionDescarga(){
        List<EmbarcacionDescarga> embarcacionDescargas = repositorio.findAll();
        return embarcacionDescargas;
    }

    public EmbarcacionDescarga buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }

    public void guardar(EmbarcacionDescarga embarcacionDescarga){
        repositorio.save(embarcacionDescarga);
    }

    public void eliminar(EmbarcacionDescarga embarcacionDescarga){
        repositorio.delete(embarcacionDescarga);
    }
}