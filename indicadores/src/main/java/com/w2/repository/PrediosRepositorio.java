package com.w2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Predio;
import com.w2.model.Semestre;
import com.w2.repository.util.ObjetoRepositorioCustom;

public interface PrediosRepositorio extends JpaRepository<Predio, Integer>, ObjetoRepositorioCustom {

	
	List<Predio> findPredioBySemestreAndCentro_SiglaLike(Semestre semestre, String sigla);
	Predio findByIdPredio(int predio);
	Predio findByIdPredioAndSemestre(int predio, Semestre semestre);
	
	List<Predio> findBySemestre(Semestre semestre);
}
