package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Contenedor;
import java.util.List;

public interface IContenedorServicio {
    
    public List<Contenedor> listarContenedores();
    
    public Contenedor buscarContendorPorId(Integer idContendor);
    
    public void guardarContenedor(Contenedor contenedor);
    
    public void eliminarContenedor(Contenedor contenedor);
    
}
