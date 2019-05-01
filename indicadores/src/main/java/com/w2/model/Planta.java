package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Planta {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID_PLANTA")
	private int idPlanta;
	
	
	@OneToOne
	@JoinColumn(name = "ID_SEMESTRE")
	private Semestre semestre;
	
	
	@OneToOne
	@JoinColumn(name = "ID_PREDIO")
	private Predio predio;
	
	
	@Column(columnDefinition="TEXT")
	private String mapeamento;
	
	private String arquivoNome;
	
	private int nivel;
	

	public void setIdPlanta(int idPlanta) {
		this.idPlanta = idPlanta;
	}



	public int getIdPlanta() {
		return idPlanta;
	}



	public Semestre getSemestre() {
		return semestre;
	}

	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}

	public String getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(String mapeamento) {
		this.mapeamento = mapeamento;
	}

	public String getArquivoNome() {
		return arquivoNome;
	}

	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}



	public Predio getPredio() {
		return predio;
	}



	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	
}
