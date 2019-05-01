package com.w2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Predio {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID_PREDIO")
	private int idPredio;

	@OneToOne
	@JoinColumn(name="ID_CENTRO")
	private Centro centro;
	
	private String nomePredio;
	
	private String endereco;
	
	private String cep;
	
	private String telefone;

	private String celular;
	
	private String email;
	
	private String diretor;
	
	private int presenciais;
	
	private int ead;
	
	private float areaTerreno;
	
	private float areaConstruida;
	
	private String propriedade;
	
    private String documento;
    
    @OneToOne
	@JoinColumn(name = "ID_SEMESTRE")
	private Semestre semestre;
    
	@Column(columnDefinition="TEXT")
    private String observacao;
	
    private String lat;
	
	private String lon;
	


	
	public int getIdPredio() {
		return idPredio;
	}




	public void setIdPredio(int idPredio) {
		this.idPredio = idPredio;
	}



	
	public String getNomePredio() {
		return nomePredio;
	}




	public void setNomePredio(String nomePredio) {
		this.nomePredio = nomePredio;
	}
	

	



	public Centro getCentro() {
		return centro;
	}




	public void setCentro(Centro centro) {
		this.centro = centro;
	}




	public String getEndereco() {
		return endereco;
	}




	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}




	public String getCep() {
		return cep;
	}




	public void setCep(String cep) {
		this.cep = cep;
	}




	public String getTelefone() {
		return telefone;
	}




	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getDiretor() {
		return diretor;
	}




	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}




	public int getPresenciais() {
		return presenciais;
	}




	public void setPresenciais(int presenciais) {
		this.presenciais = presenciais;
	}




	public int getEad() {
		return ead;
	}




	public void setEad(int ead) {
		this.ead = ead;
	}




	public float getAreaTerreno() {
		return areaTerreno;
	}




	public void setAreaTerreno(float areaTerreno) {
		this.areaTerreno = areaTerreno;
	}




	public float getAreaConstruida() {
		return areaConstruida;
	}




	public void setAreaConstruida(float areaConstruida) {
		this.areaConstruida = areaConstruida;
	}




	public String getPropriedade() {
		return propriedade;
	}




	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}




	public String getDocumento() {
		return documento;
	}




	public void setDocumento(String documento) {
		this.documento = documento;
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




	public String getCelular() {
		return celular;
	}




	public void setCelular(String celular) {
		this.celular = celular;
	}




	public String getObservacao() {
		return observacao;
	}




	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}




	public Semestre getSemestre() {
		return semestre;
	}




	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}
	
	
	

}
