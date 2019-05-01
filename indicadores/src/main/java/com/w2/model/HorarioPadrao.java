package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class HorarioPadrao {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID_HORARIO_PADRAO")
	private int idHorarioPadrao;

	private String inicio;
	
	private String fim;
	
	private String turno;
	
	private int tipo;
		
	private int ordem;
	
	@OneToOne
	@JoinColumn(name = "ID_SEMESTRE")
	private Semestre semestre;

	
	public void setIdHorarioPadrao(int idHorarioPadrao) {
		this.idHorarioPadrao = idHorarioPadrao;
	}



	public int getIdHorarioPadrao() {
		return idHorarioPadrao;
	}

	

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public Semestre getSemestre() {
		return semestre;
	}

	public void setSemestre(Semestre semestreSalvo) {
		this.semestre = semestreSalvo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}



	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	

	
	
}
