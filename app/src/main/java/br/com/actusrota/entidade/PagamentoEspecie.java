package br.com.actusrota.entidade;

import br.com.actusrota.enumerador.EnumFormaPagamento;
import br.com.actusrota.enumerador.EnumStatusCheque;

public class PagamentoEspecie extends ContaReceber {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EnumFormaPagamento getFormaPagamento() {
		return EnumFormaPagamento.ESPECIE;
	}
	
	public EnumStatusCheque getStatusCheque() {
		return EnumStatusCheque.COMPENSADO;
	}	

	public String getDataPrevistaCompensar() {
		return "-- --";
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PagamentoEspecie:");
		sb.append(id);
		sb.append(" idMovel:");
		sb.append(getIdMovel());
		sb.append(" valor:");
		sb.append(valor);
		return sb.toString();
	}

}
