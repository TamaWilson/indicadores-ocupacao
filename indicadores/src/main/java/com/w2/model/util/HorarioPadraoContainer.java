package com.w2.model.util;

import java.util.ArrayList;

import com.w2.model.HorarioPadrao;


public class HorarioPadraoContainer {

	
	private ArrayList<HorarioPadrao> horarios;
	
	public HorarioPadraoContainer() { 
		
		this.horarios = new ArrayList<HorarioPadrao>();
	}

	public ArrayList<HorarioPadrao> getHorarios() {
		return horarios;
	}

	public void setHorarios(ArrayList<HorarioPadrao> horarios) {
		this.horarios = horarios;
	}
	
	public void add(HorarioPadrao horario) { 
		this.horarios.add(horario);
	}
	
}
