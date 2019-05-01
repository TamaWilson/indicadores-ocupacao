package com.w2.model.util;

public enum DiasSemana {

	
	SEGUNDA("SEGUNDA-FEIRA", 1),
	TERCA("TERÇA-FEIRA", 2),
	QUARTA("QUARTA-FEIRA", 3),
	QUINTA("QUINTA-FEIRA", 4),
	SEXTA("SEXTA-FEIRA", 5),
	SABADO("SÁBADO", 6);
	
	private String descricao;
	private int ordem;
	
	DiasSemana(String descrição, int ordem) {
		this.descricao =  descrição;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	
	
}
