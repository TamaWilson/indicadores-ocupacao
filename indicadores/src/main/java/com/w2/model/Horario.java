package com.w2.model;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity
public class Horario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idHorario;
	
	private int idCurso;
	private int disciplinaId;

	private int turmaPeriodo;
	
	private int tipoDescricao;
	private int horarioOrdem;
	private int diaSemana;
	
	
	//@Transient
	private String disciplinaNome;
	
	@Transient
	private String professorNome;
	
	@Transient
	private String disciplinaCodigo;
	
	@Transient
	private HashMap<String, String> status;
	
	
	@OneToOne
	@JoinColumn(name = "ID_SEMESTRE")
	private Semestre semestre;
	
	
	@OneToOne
	@JoinColumn(name="ID_SALA")
	private Sala sala;
	
	@OneToOne
	@JoinColumn(name="ID_HORARIO_PADRAO")
	private HorarioPadrao horarioPadrao;
	
	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}

	public int getIdHorario() {
		return idHorario;
	}


	public int getIdCurso() {
		return idCurso;
	}


	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}


	public int getDisciplinaId() {
		return disciplinaId;
	}


	public void setDisciplinaId(int disciplinaId) {
		this.disciplinaId = disciplinaId;
	}


	public int getTurmaPeriodo() {
		return turmaPeriodo;
	}


	public void setTurmaPeriodo(int turmaPeriodo) {
		this.turmaPeriodo = turmaPeriodo;
	}


	public int getTipoDescricao() {
		return tipoDescricao;
	}


	public void setTipoDescricao(int tipoDescricao) {
		this.tipoDescricao = tipoDescricao;
	}


	public int getHorarioOrdem() {
		return horarioOrdem;
	}


	public void setHorarioOrdem(int horarioOrdem) {
		this.horarioOrdem = horarioOrdem;
	}


	public int getDiaSemana() {
		return diaSemana;
	}


	public void setDiaSemana(int diaSemana) {
		this.diaSemana = diaSemana;
	}


	public Semestre getSemestre() {
		return semestre;
	}


	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}


	public Sala getSala() {
		return sala;
	}


	public void setSala(Sala sala) {
		this.sala = sala;
	}


	public HorarioPadrao getHorarioPadrao() {
		return horarioPadrao;
	}


	public void setHorarioPadrao(HorarioPadrao horarioPadrao) {
		this.horarioPadrao = horarioPadrao;
	}

	public String getDisciplinaNome() {
		return disciplinaNome;
	}

	public void setDisciplinaNome(String disciplinaNome) {
		this.disciplinaNome = disciplinaNome;
	}

	public String getProfessorNome() {
		return professorNome;
	}

	public void setProfessorNome(String professorNome) {
		this.professorNome = professorNome;
	}

	public String getDisciplinaCodigo() {
		return disciplinaCodigo;
	}

	public void setDisciplinaCodigo(String disciplinaCodigo) {
		this.disciplinaCodigo = disciplinaCodigo;
	}

	public HashMap<String, String> getStatus() {
		return status;
	}

	public void setStatus(HashMap<String, String> status) {
		this.status = status;
	}
	
	
	
}
