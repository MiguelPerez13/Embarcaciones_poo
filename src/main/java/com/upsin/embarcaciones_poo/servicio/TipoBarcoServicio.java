package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.TipoBarco;
import com.upsin.embarcaciones_poo.repositorio.TipoBarcoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoBarcoServicio implements ITipoBarcoServicio{

    @Autowired
    private TipoBarcoRepositorio tipoBarcoRepositorio;
    
    @Override
    public List<TipoBarco> listarBarcos() {
        List<TipoBarco> tipoBarcos = tipoBarcoRepositorio.findAll();
        return tipoBarcos;
    }

    @Override
    public TipoBarco bucarTipobarcoId(Integer id) {
        return tipoBarcoRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardarTipoBarco(TipoBarco tipoBarco) {
        tipoBarcoRepositorio.save(tipoBarco);
    }

    @Override
    public void eliminarTipoBarco(TipoBarco tipoBarco) {
        tipoBarcoRepositorio.delete(tipoBarco);
    }
    
}
