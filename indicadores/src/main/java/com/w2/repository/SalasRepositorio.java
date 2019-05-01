package com.w2.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Planta;
import com.w2.model.Predio;
import com.w2.model.Sala;
import com.w2.model.Semestre;
import com.w2.repository.util.ObjetoRepositorioCustom;

public interface SalasRepositorio extends JpaRepository<Sala, Integer>, ObjetoRepositorioCustom{

	
	List<Sala> findSalaByPredioOrderByNumeroSalaAsc(Predio predio);
	Sala findByPredioAndNumeroSala(Predio predio, int numero);
	
	List<Sala> findByPredio(Predio predio);
	List<Sala> findByPlanta(Planta planta);
	Sala findFirstByPlanta(Planta planta);	
	List<Sala> findBySemestre(Semestre semestreAtivo);
	
}
