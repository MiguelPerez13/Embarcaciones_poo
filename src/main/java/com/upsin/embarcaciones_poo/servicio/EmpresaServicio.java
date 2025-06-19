package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Empresa;
import com.upsin.embarcaciones_poo.repositorio.EmpresaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServicio {
    
    @Autowired
    private EmpresaRepositorio repositorio;
    
    public List<Empresa> listarEmpresa(){
        List<Empresa> empresas = repositorio.findAll();
        return empresas;
    }
    
    public Empresa buscarPorId(Integer id){
        return repositorio.findById(id).orElse(null);
    }
    
    public void guardar(Empresa empresa){
        repositorio.save(empresa);
    }
    
    public void eliminar(Empresa empresa){
        repositorio.delete(empresa);
    }
}
