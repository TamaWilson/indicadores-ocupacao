package com.w2.model.support;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Ocupacao {


	final static int TOTAL_HORARIOS = 92;
	final static int TOTAL_MATUTINO = 36;
	final static int TOTAL_VESPERTINO = 36;
	final static int TOTAL_NOTURNO = 20;

	private int total;
	private int matutino;
	private int vespertino;
	private int noturno;

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = TOTAL_HORARIOS - total;
	}
	public int getMatutino() {
		return matutino;
	}
	public void setMatutino(int matutino) {
		this.matutino = TOTAL_MATUTINO - matutino;
	}
	public int getVespertino() {
		return vespertino;
	}
	public void setVespertino(int verspertino) {
		this.vespertino = TOTAL_VESPERTINO - vespertino;
	}
	public int getNoturno() {
		return noturno;
	}
	public void setNoturno(int noturno) {
		this.noturno = TOTAL_NOTURNO - noturno;
	}

	public double getPorcentagemTotal() {

		int totalOcupado = matutino + vespertino + noturno;
		double porcentagem = totalOcupado/Double.valueOf(TOTAL_HORARIOS)*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public double getPorcentagemMatutino() {

		double porcentagem = this.matutino/Double.valueOf(TOTAL_MATUTINO)*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public double getPorcentagemVespertino() {
		double porcentagem = this.vespertino/Double.valueOf(TOTAL_VESPERTINO)*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public double getPorcentagemNoturno() {
		double porcentagem = this.noturno/Double.valueOf(TOTAL_NOTURNO)*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}
	
	
	
	
	public static int getTotalHorarios() {
		return TOTAL_HORARIOS;
	}
	public static int getTotalMatutino() {
		return TOTAL_MATUTINO;
	}
	public static int getTotalVespertino() {
		return TOTAL_VESPERTINO;
	}
	public static int getTotalNoturno() {
		return TOTAL_NOTURNO;
	}



    


}
