package br.com.actusrota.negocio;

import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.util.UtilDinheiro;

public class AcertoClienteNegocio {
	private AcertoClienteParametro acertoCliente;

	public AcertoClienteNegocio(AcertoClienteParametro acertoCliente) {
		this.acertoCliente = acertoCliente;
	}

	public boolean isMargemValida(Dinheiro totalRecebimentos, Dinheiro valor) {
		float porcentagem = UtilDinheiro.calcularPorcentagem(totalRecebimentos, valor);

		boolean valida = acertoCliente.getMargemTotal() > porcentagem;
		if (valida) {
			System.err.printf("%.2f é menor %d OK \n", porcentagem,
					acertoCliente.getMargemTotal());
		} else {
			System.err.printf("%.2f é maior %d Inválida \n", porcentagem,
					acertoCliente.getMargemTotal());
		}
		return valida;
	}
}
