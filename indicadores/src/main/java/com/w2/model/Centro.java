package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.w2.model.util.Mesorregioes;

@Entity
public class Centro {


	@Id
	@Column(name="ID_CENTRO")
	private int idCentro;
	
	private String sigla; 
	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	private String nomeCentro;
	
	@Enumerated(EnumType.STRING)
	private Mesorregioes mesorregiao;
	
	private String lat;
	
	private String lon;

	public int getIdCentro() {
		return idCentro;
	}


	public String getNomeCentro() {
		return nomeCentro;
	}

	public void setNomeCentro(String nomeCentro) {
		this.nomeCentro = nomeCentro;
	}

	public Mesorregioes getMesorregiao() {
		return mesorregiao;
	}

	public void setMesorregiao(Mesorregioes mesorregiao) {
		this.mesorregiao = mesorregiao;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public void setIdCentro(int idCentro) {
		this.idCentro = idCentro;
	}

	
	
	
	
}



