package com.w2.model.util;

public enum HorararioOrdem {

	
	PRIMEIRO("PRIMEIRO HORÁRIO", 1),
	SEGUNDO("SEGUNDO HORÁRIO", 2),
	TERCEIRO("TERCEIRO HORÁRIO", 3),
	QUARTO("QUARTO HORÁRIO", 4),
	QUINTO("QUINTO HORÁRIO", 5),
	SEXTO("SEXTO HORÁRIO", 6);
	
	private String descricao;
	private int ordem;
	
	HorararioOrdem(String descrição, int ordem) {
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
