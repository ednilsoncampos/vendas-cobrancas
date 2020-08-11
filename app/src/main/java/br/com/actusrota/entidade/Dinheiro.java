package br.com.actusrota.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Dinheiro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9013233962821945546L;

	private static final long FATOR_FRACIONARIO = 100;
	private static final String SEPARADOR_DECIMAL = ",";
	private static final Locale LOCAL_BR = new Locale("pt", "BR");

	private static Currency moeda = Currency
			.getInstance(new Locale("pt", "BR"));

	private long valor;

	public Dinheiro() {
		this.valor = 0;
	}

	public boolean isNovo() {
		return valor <= 0;
	}

	public static Dinheiro valueOf(String arg) {
		long valor = 0;
		Currency moeda = Currency.getInstance(new Locale("pt", "BR"));
		if (arg != null && !(arg.trim().length() == 0)) {
			arg = arg.replace(".", "").trim();
			String[] fatores = arg.split(SEPARADOR_DECIMAL);
			String fracao = "0";
			String decimo = "0";

			if (fatores != null && fatores.length > 0) {
				decimo = fatores[0];
				if (fatores.length >= 2) {
					fracao = fatores[1];
				}
			}
			valor = Long.parseLong(fracao);
			valor += Long.parseLong(decimo) * FATOR_FRACIONARIO;
		}
		return new Dinheiro(valor, moeda);
	}

	private Dinheiro(long valor, Currency moeda) {
		this.valor = valor;
		// this.moeda = moeda;
		// this.moeda = Currency.getInstance(new Locale("pt", "BR"));
	}

	public static Dinheiro novo() {
		Currency moeda = Currency.getInstance(new Locale("pt", "BR"));
		return new Dinheiro(0, moeda);
	}

	/**
	 * Formata o valor informado. Ex.: se recebe 50 como valor formata para
	 * 50,00 reiais
	 * 
	 * @param valor
	 * @param moeda
	 * @return
	 */
	public static Dinheiro valueOf(String valor, Currency moeda) {
		moeda = Currency.getInstance(new Locale("pt", "BR"));
		return valueOf(new BigDecimal(valor.replace(",", ".")), moeda);
	}

	/**
	 * Formata o valor informado. Ex.: se recebe 50 como valor formata para 0,50
	 * centavos
	 * 
	 * @param valor
	 * @param moeda
	 * @return
	 */
	public static Dinheiro valueOf(long valor, Currency moeda) {
		return new Dinheiro(valor, moeda);
	}

	public static Dinheiro valueOf(long valor) {
		return new Dinheiro(valor, moeda);
	}

	private static Dinheiro valueOf(BigDecimal valor, Currency moeda) {
		return new Dinheiro(toRepresentacaoLong(valor, moeda), moeda);
	}

	private static long toRepresentacaoLong(BigDecimal valor, Currency moeda) {
		return valor.movePointRight(moeda.getDefaultFractionDigits())
				.longValue();
	}

	private static BigDecimal deRepresentacaoLong(long soma, Currency moeda) {
		BigDecimal valor = new BigDecimal(soma);
		return valor.movePointLeft(moeda.getDefaultFractionDigits());
	}

	public Currency getMoeda() {
		return moeda;
	}

	public long getValor() {
		return valor;
	}

	public BigDecimal getValorBigDecimal() {
		return deRepresentacaoLong(valor, moeda);
	}

	public String getValorFormatado() {
		DecimalFormat dfUnidade = new DecimalFormat("00",
				new DecimalFormatSymbols(LOCAL_BR));
		DecimalFormat dfDecimal = new DecimalFormat("###,###.###",
				new DecimalFormatSymbols(LOCAL_BR));

		long v = valor / FATOR_FRACIONARIO;
		long cents = this.valor % FATOR_FRACIONARIO;
		String valor = dfDecimal.format(v) + SEPARADOR_DECIMAL
				+ dfUnidade.format(cents);
		return valor;
	}

	String valorTeste() {
		NumberFormat moeda3 = NumberFormat.getCurrencyInstance();
		BigDecimal valorMoeda3Alterado = new BigDecimal(this.valor);
		return moeda3.format(valorMoeda3Alterado);
	}

	@Override
	public String toString() {
		return getValorFormatado();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (valor ^ (valor >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dinheiro other = (Dinheiro) obj;
		if (valor != other.valor)
			return false;
		return true;
	}
}
