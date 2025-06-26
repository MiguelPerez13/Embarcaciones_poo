package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Contenedor;
import com.upsin.embarcaciones_poo.repositorio.ContenedorRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContenedorServicio implements IContenedorServicio{

    @Autowired
    private ContenedorRepositorio contenedorRepositorio;
    
    @Override
    public List<Contenedor> listarContenedores() {
        List<Contenedor> contenedores = contenedorRepositorio.findAll();
        return contenedores;
    }

    @Override
    public Contenedor buscarContendorPorId(Integer idContendor) {
        return contenedorRepositorio.findById(idContendor).orElse(null);
    }

    @Override
    public void guardarContenedor(Contenedor contenedor) {
        contenedorRepositorio.save(contenedor);
    }

    @Override
    public void eliminarContenedor(Contenedor contenedor) {
        contenedorRepositorio.delete(contenedor);
    }
    
    
}
