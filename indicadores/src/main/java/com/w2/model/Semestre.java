package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"semestre", "ano"})
	}) 
public class Semestre {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID_SEMESTRE")
	private int idSemestre;
	
	private int ano;
	
	private int semestre;

	private Boolean ativo;
	
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getSemestre() {
		return semestre;
	}
	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}
	public int getIdSemestre() {
		return idSemestre;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public void setIdSemestre(int idSemestre) {
		this.idSemestre = idSemestre;
	}

}
