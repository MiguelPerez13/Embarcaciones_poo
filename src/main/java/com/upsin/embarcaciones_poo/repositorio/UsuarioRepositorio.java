package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.Usuario;
import com.upsin.embarcaciones_poo.modelo.UsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, UsuarioId> {
}
