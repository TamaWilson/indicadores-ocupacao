package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.w2.model.support.Ocupacao;

@Entity
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID_SALA")
	private int idSala;
	
	@OneToOne
	@JoinColumn(name = "ID_PREDIO")
	private Predio predio;
	
	private String numeroSala;
	
	@OneToOne
	@JoinColumn(name = "ID_SEMESTRE")
	private Semestre semestre;
	
	
	@OneToOne
	@JoinColumn(name= "ID_PLANTA")
	private Planta planta;
	
	@Transient
	private Ocupacao ocupacao;
	
	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	public String getNumeroSala() {
		return numeroSala;
	}

	public void setNumeroSala(String numeroSala2) {
		this.numeroSala = numeroSala2;
	}

	public Ocupacao getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(Ocupacao ocupacao) {
		this.ocupacao = ocupacao;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Semestre getSemestre() {
		return semestre;
	}

	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}	
	
	
	
	
}


