package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Producto;
import com.upsin.embarcaciones_poo.repositorio.ProductoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {
    
    @Autowired
    private ProductoRepositorio repositorio;
    
    public List<Producto> listarProducto(){
        List<Producto> productos = repositorio.findAll();
        return productos;
    }
    
    public Producto buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(Producto producto){
        repositorio.save(producto);
    }
    
    public void eliminar(Producto producto){
        repositorio.delete(producto);
    }
}
