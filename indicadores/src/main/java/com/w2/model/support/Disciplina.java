package com.w2.model.support;

import java.util.List;

import com.w2.model.Horario;

public class Disciplina  {
	
	private int disciplinaId;
	private String disciplinaNome;
	private String disciplinaCodigo;
	private String professorNome;
	private int disciplinaPeriodo;
	private List<Horario> horarios;
	
	public int getDisciplinaId() {
		return disciplinaId;
	}
	public void setDisciplinaId(int disciplinaId) {
		this.disciplinaId = disciplinaId;
	}
	public String getDisciplinaNome() {
		return disciplinaNome;
	}
	public void setDisciplinaNome(String disciplinaNome) {
		this.disciplinaNome = disciplinaNome;
	}
	public String getDisciplinaCodigo() {
		return disciplinaCodigo;
	}
	public void setDisciplinaCodigo(String disciplinaCodigo) {
		this.disciplinaCodigo = disciplinaCodigo;
	}
	public String getProfessorNome() {
		return professorNome;
	}
	public void setProfessorNome(String professorNome) {
		this.professorNome = professorNome;
	}
	public int getDisciplinaPeriodo() {
		return disciplinaPeriodo;
	}
	public void setDisciplinaPeriodo(int disciplinaPeriodo) {
		this.disciplinaPeriodo = disciplinaPeriodo;
	}
	public List<Horario> getHorarios() {
		return horarios;
	}
	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}


}
