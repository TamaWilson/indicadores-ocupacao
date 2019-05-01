package com.w2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2.model.Horario;
import com.w2.model.Sala;
import com.w2.model.Semestre;

public interface HorarioRepositorio extends JpaRepository<Horario, Integer> {
	

		List<Horario> findBySalaAndSemestre(Sala sala, Semestre semestre); 
		Horario findBySalaAndDiaSemanaAndHorarioOrdemAndTipoDescricaoAndSemestre(Sala sala, int diaSemana, int ordem, int tipo, Semestre semestre);
		List<Horario> findByDisciplinaIdAndSemestre(int disciplina, Semestre semestre);
		List<Horario> findBySalaAndTipoDescricao(Sala sala, int tipo);
		
		List<Horario> findBySemestreAndSalaIn(Semestre semestre, List<Sala> salas);
		List<Horario> findBySala(Sala salaTDHS);
}
