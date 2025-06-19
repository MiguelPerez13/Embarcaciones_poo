
package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.TipoBarco;
import java.util.List;

public interface ITipoBarcoServicio {
    
    public List<TipoBarco> listarBarcos();
    
    public TipoBarco bucarTipobarcoId(Integer id);
    
    public void guardarTipoBarco(TipoBarco tipoBarco);
    
    public void eliminarTipoBarco(TipoBarco tipoBarco);
    
}
