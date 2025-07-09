package com.upsin.embarcaciones_poo.repositorio;

import com.upsin.embarcaciones_poo.modelo.EmbarcacionDescarga;
import com.upsin.embarcaciones_poo.modelo.EmbarcacionDescargaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbarcacionDescargaRepositorio extends JpaRepository<EmbarcacionDescarga, EmbarcacionDescargaId>{

}