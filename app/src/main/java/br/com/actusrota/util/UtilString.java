package br.com.actusrota.util;

public class UtilString {

	public static String removerMaskCPF(String texto) {
		texto = texto.replace(".", "").replace("-", "");
		return texto;
	}

	public static String formatarCPF(String cpf) {
		StringBuilder builder = new StringBuilder(cpf);
		builder.insert(3, ".");
		builder.insert(7, ".");
		builder.insert(11, "-");
		return builder.toString();
	}	

	public static String adicionarBarraData(String data) {
		StringBuilder builder = new StringBuilder(data);
		builder.insert(2, "/");
		builder.insert(5, "/");
		return builder.toString();
	}
	
	public static boolean isValorInvalido(String texto) {
		StringBuilder sb = new StringBuilder(texto);
		return (sb == null || sb.toString().trim().length() <= 0);
	}
	
	public static boolean validarCPF(String cpf) {
		if (cpf.length() != 11)
			return false;

		String numDig = cpf.substring(0, 9);
		return calcDigVerif(numDig).equals(cpf.substring(9, 11));
	}
	
	private static String calcDigVerif(String num) {
		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++)
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

		if (soma % 11 == 0 | soma % 11 == 1)
			primDig = new Integer(0);
		else
			primDig = new Integer(11 - (soma % 11));

		soma = 0;
		peso = 11;
		for (int i = 0; i < num.length(); i++)
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1)
			segDig = new Integer(0);
		else
			segDig = new Integer(11 - (soma % 11));

		return primDig.toString() + segDig.toString();
	}
}
