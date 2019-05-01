package com.w2.model.util;

import java.util.List;

import com.w2.model.HorarioPadrao;
import com.w2.model.Semestre;

public class HorarioSemestreContainer {
	
	
	
	private Semestre semestre;
	
	private List<HorarioPadrao> horariosPadrao;

	public Semestre getSemestre() {
		return semestre;
	}

	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}

	public List<HorarioPadrao> getHorariosPadrao() {
		return horariosPadrao;
	}

	public void setHorariosPadrao(List<HorarioPadrao> horariosPadrao) {
		this.horariosPadrao = horariosPadrao;
	}
	

	
	
}
