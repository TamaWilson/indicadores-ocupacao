package com.w2.initialize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Inicializacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idStatus;
	
	private Boolean status;
	
	private Boolean configuraçao;

	public int getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getConfiguraçao() {
		return configuraçao;
	}

	public void setConfiguraçao(Boolean configuraçao) {
		this.configuraçao = configuraçao;
	}
	
	
	
	
	
}
