package com.upsin.embarcaciones_poo.servicio;

import com.upsin.embarcaciones_poo.modelo.Usuario;
import com.upsin.embarcaciones_poo.modelo.UsuarioId;
import com.upsin.embarcaciones_poo.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario cargarUsuarioId(String usuario, String passw){
        UsuarioId id = new UsuarioId(usuario, passw);
        return usuarioRepositorio.findById(id).orElse(null);
    }

}
