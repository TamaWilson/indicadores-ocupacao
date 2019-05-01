package com.w2.model.util;

public enum Turnos {

	
	MANHA("MANHÃ", "Matutino", 1),
	TARDE("TARDE", "Vespertino", 2), 
	NOITE("NOITE", "Noturno", 3);

	private String identificador;
	private String descricao;
	private int tipo;
	
	Turnos(String identificador, String descricao, int tipo) {
		this.descricao =  descricao;
		this.identificador = identificador;
		this.tipo = tipo;
	}

	public String getIdentificador() {
		return identificador;
	}


	public String getDescricao() {
		return descricao;
	}

	
	public int getTipo() { 
		
		return tipo;
	}

}
