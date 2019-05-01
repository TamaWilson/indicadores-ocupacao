package com.w2.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Planta;
import com.w2.model.Predio;
import com.w2.repository.util.ObjetoRepositorioCustom;

public interface PlantasRepositorio extends JpaRepository<Planta, Integer>, ObjetoRepositorioCustom {
 
	
	 Planta findByIdPlanta(int idPlanta);

     List<Planta> findByPredio(Predio predio);
}
