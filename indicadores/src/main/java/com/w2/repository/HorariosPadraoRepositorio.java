package com.w2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.HorarioPadrao;
import com.w2.model.Semestre;

public interface HorariosPadraoRepositorio extends JpaRepository<HorarioPadrao, Integer>{

	
	List<HorarioPadrao> findBySemestreOrderByOrdemAsc(Semestre semestre);
	HorarioPadrao findByOrdemAndTipoAndSemestre(int Ordem, int Tipo, Semestre semestre); 
	List<HorarioPadrao> findBySemestre(Semestre semestre);
}
