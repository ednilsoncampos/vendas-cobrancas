package br.com.actusrota.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import br.com.actusrota.entidade.Dinheiro;

public class UtilDinheiro {
	
	private static final int FATOR_DIVISAO = 100;
	private static final int ARREDONDAMENTO = 2;
	private static Currency moeda = Currency
			.getInstance(new Locale("pt", "BR"));

	private UtilDinheiro() {

	}

	public static Dinheiro calcularMediaSoma(Dinheiro valorAtual, int qtdAtual,
			Dinheiro valorNovo, int qtdNovo) {
		if (valorAtual == null || valorNovo == null) {
			return Dinheiro.novo();
		}

		Dinheiro totalAtual = multiplicar(valorAtual, qtdAtual);
		Dinheiro totalNovo = multiplicar(valorNovo, qtdNovo);

		int qtdTotal = qtdAtual + qtdNovo;
		Dinheiro somaTotal = UtilDinheiro.somar(totalAtual, totalNovo);

		Dinheiro valorMedio = UtilDinheiro.dividir(somaTotal, qtdTotal);
		return valorMedio;
	}

	public static Dinheiro calcularMediaSubtrair(Dinheiro valorAtual,
			int qtdAtual, Dinheiro valorNovo, int qtdNovo) {
		if (valorAtual == null || valorNovo == null) {
			return Dinheiro.novo();
		}

		Dinheiro totalAtual = multiplicar(valorAtual, qtdAtual);
		Dinheiro totalNovo = multiplicar(valorNovo, qtdNovo);

		int qtdTotal = qtdAtual - qtdNovo;
		Dinheiro subtracao = UtilDinheiro.subtrair(totalAtual, totalNovo);

		Dinheiro valorMedio = null;
		if (qtdTotal != 0)
			valorMedio = UtilDinheiro.dividir(subtracao, qtdTotal);
		else
			valorMedio = UtilDinheiro.multiplicar(subtracao, qtdTotal);
		return valorMedio;
	}

	public static Dinheiro somar(Dinheiro valor1, Dinheiro valor2) {
		if (valor1 == null && valor2 == null) {
			return Dinheiro.novo();
		}
		if (valor1 == null) {
			return valor2;
		}
		if (valor2 == null) {
			return valor1;
		}
		long resultado = valor1.getValor() + valor2.getValor();
		return Dinheiro.valueOf(resultado, moeda);
	}

	public static Dinheiro somarValores(Dinheiro... valor) {
		if (valor != null) {
			long resultado = 0;
			for (int i = 0; i < valor.length; i++) {
				if (valor[i] != null)
					resultado += valor[i].getValor();
			}
			return Dinheiro.valueOf(resultado, moeda);
		}
		return null;
	}

	public static Dinheiro subtrair(Dinheiro valor1, Dinheiro valor2) {
		if (valor1 == null && valor2 == null) {
			return Dinheiro.novo();
		}
		if (valor1 == null) {
			return valor2;
		}
		if (valor2 == null) {
			return valor1;
		}
		long resultado = valor1.getValor() - valor2.getValor();
		return Dinheiro.valueOf(resultado, moeda);
	}

	public static Dinheiro multiplicar(Dinheiro valor, Number multiplicador) {
		if (valor == null || valor.isNovo()) {
			return Dinheiro.novo();
		}
		BigDecimal bigFator = null;

		if (multiplicador instanceof BigDecimal)
			bigFator = (BigDecimal) multiplicador;
		else
			bigFator = BigDecimal.valueOf(multiplicador.longValue());

		long resultado = bigFator
				.multiply(BigDecimal.valueOf(valor.getValor())).longValue();
		return Dinheiro.valueOf(resultado, moeda);
	}

	public static float calcularPorcentagem(Dinheiro total, Dinheiro valor) {
		Dinheiro resultado = multiplicar(valor, FATOR_DIVISAO);
		BigDecimal bigValor = BigDecimal.valueOf(resultado.getValor());

		BigDecimal bigDecimal = BigDecimal.valueOf(total.getValor());
		BigDecimal divide = bigValor.divide(bigDecimal, ARREDONDAMENTO,
				RoundingMode.HALF_EVEN);
		return divide.floatValue();
	}

	public static Dinheiro dividir(Dinheiro valor, int nParcelas) {
		return divid(valor, nParcelas);
	}

	private static Dinheiro divid(Dinheiro valor, int nParcelas) {
		BigInteger bigValor = BigInteger.valueOf(valor.getValor());
		BigInteger[] resultado = bigValor.divideAndRemainder(BigInteger
				.valueOf(nParcelas));
		return Dinheiro.valueOf(resultado[0].longValue(), moeda);
	}

	private static Dinheiro div(Dinheiro valor, int nParcelas) {
		BigDecimal[] result = new BigDecimal[2];
		BigDecimal val = new BigDecimal(valor.getValor());
		result[0] = val.setScale(0, RoundingMode.DOWN);
		result[1] = val.subtract(result[0]).movePointRight(val.scale());
		System.out.println(result[0] + "kk" + result[1]);
		return Dinheiro.valueOf(result[0].longValue(), moeda);
	}

	public static Dinheiro[] parcelar(Dinheiro dinheiro, int nParcelas) {
		BigInteger bigValor = BigInteger.valueOf(dinheiro.getValor());
		BigInteger[] resultado = bigValor.divideAndRemainder(BigInteger
				.valueOf(nParcelas));

		Dinheiro[] array = new Dinheiro[2];

		for (int i = 0; i < resultado.length; i++) {
			array[i] = Dinheiro.valueOf(resultado[i].longValue(), moeda);
		}
		return array;
	}

	public static BigDecimal dividirArredondar(int valor1, int valor2) {
		BigDecimal big1 = new BigDecimal(valor1);
		BigDecimal big2 = new BigDecimal(valor2);

		BigDecimal resultado = big1.divide(big2, 2, RoundingMode.UP);
		return resultado;
	}

	public static boolean maior(Dinheiro valor1, Dinheiro valor2) {
		return valor1.getValor() > valor2.getValor();
	}

}
