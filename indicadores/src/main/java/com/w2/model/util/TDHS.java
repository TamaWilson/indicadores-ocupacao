package com.w2.model.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TDHS {

	private static final int HORARIOS_TOTAL = 92;
	private static final int HORARIOS_DIURNO = 36;
	private static final int HORARIOS_NOTURNO = 20;

	private int totalHorarios;
	private int totalHorariosDiurno;
	private int totalHorariosNoturno;
	private int totalDisponivelGeral;
	private int totalDisponivelMatutino;
	private int totalDisponivelVespertino;
	private int totalDisponivelNoturno;



	public int getTotalHorarios() {
		return totalHorarios;
	}
	public void setTotalHorarios(int totalHorarios) {
		this.totalHorarios = totalHorarios * HORARIOS_TOTAL;
	}
	public int getTotalHorariosDiurno() {
		return totalHorariosDiurno;
	}
	public void setTotalHorariosDiurno(int totalHorariosDiurno) {
		this.totalHorariosDiurno = totalHorariosDiurno * HORARIOS_DIURNO;
	}
	public int getTotalHorariosNoturno() {
		return totalHorariosNoturno;
	}
	public void setTotalHorariosNoturno(int totalHorariosNoturno) {
		this.totalHorariosNoturno = totalHorariosNoturno * HORARIOS_NOTURNO;
	}

	public void setTotalDisponivelGeral() {
		this.totalDisponivelGeral = totalDisponivelMatutino + totalDisponivelVespertino + totalDisponivelNoturno;
	}

	public int getTotalDisponivelGeral() {
		return this.totalDisponivelGeral;
	}

	public int getTotalDisponivelMatutino() {
		return totalDisponivelMatutino;
	}
	public void setTotalDisponivelMatutino(int totalDisponivelMatutino) {
		this.totalDisponivelMatutino += totalDisponivelMatutino;
	}
	public int getTotalDisponivelVespertino() {
		return totalDisponivelVespertino;
	}
	public void setTotalDisponivelVespertino(int totalDisponivelVespertino) {
		this.totalDisponivelVespertino += totalDisponivelVespertino;
	}
	public int getTotalDisponivelNoturno() {
		return totalDisponivelNoturno;
	}
	public void setTotalDisponivelNoturno(int totalDisponivelNoturno) {
		this.totalDisponivelNoturno += totalDisponivelNoturno;
	}




	public double getTDHSGeral() {
		double porcentagem = (double) totalDisponivelGeral / totalHorarios*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}



	public double getTDHSMatutino() {
		double porcentagem = (double) totalDisponivelMatutino / totalHorariosDiurno*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public double getTDHSVespertino() {
		double porcentagem = (double) totalDisponivelVespertino / totalHorariosDiurno*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}


	public double getTDHSNoturno() {
		double porcentagem = (double) totalDisponivelNoturno / totalHorariosNoturno*100;

		BigDecimal bd = new BigDecimal(porcentagem);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}



}
