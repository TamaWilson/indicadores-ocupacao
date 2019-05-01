package com.w2.model.util;

public enum Mesorregioes {

	
	NORTE("Norte"),
	SUL("Sul"), 
	LESTE("Leste"),
	OESTE("Oeste"),
	CENTRO("Centro");

	private String descricao;

	Mesorregioes(String mezorregiao) {
		this.descricao =  mezorregiao;
	}

	public String getDescricao() {
		return descricao;
	}


}
