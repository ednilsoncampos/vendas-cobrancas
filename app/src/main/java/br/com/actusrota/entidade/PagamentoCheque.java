package br.com.actusrota.entidade;

import java.util.Date;

import br.com.actusrota.enumerador.EnumFormaPagamento;
import br.com.actusrota.enumerador.EnumStatusCheque;
import br.com.actusrota.enumerador.EnumStatusVenda;

public class PagamentoCheque extends ContaReceber {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String numeroCheque;
	private Character idStatusCheque;
	@SuppressWarnings("unused")
	private EnumStatusCheque statusCheque;

	private Character idFormaPagamento;
	@SuppressWarnings("unused")
	private EnumFormaPagamento formaPagamento;
	
	private Date dataPrevistaCompensar;

	public PagamentoCheque() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((dataPrevistaCompensar == null) ? 0 : dataPrevistaCompensar
						.hashCode());
		result = prime
				* result
				+ ((idFormaPagamento == null) ? 0 : idFormaPagamento.hashCode());
		result = prime * result
				+ ((numeroCheque == null) ? 0 : numeroCheque.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoCheque other = (PagamentoCheque) obj;
		if (dataPrevistaCompensar == null) {
			if (other.dataPrevistaCompensar != null)
				return false;
		} else if (!dataPrevistaCompensar.equals(other.dataPrevistaCompensar))
			return false;
		if (idFormaPagamento == null) {
			if (other.idFormaPagamento != null)
				return false;
		} else if (!idFormaPagamento.equals(other.idFormaPagamento))
			return false;
		if (numeroCheque == null) {
			if (other.numeroCheque != null)
				return false;
		} else if (!numeroCheque.equals(other.numeroCheque))
			return false;
		return true;
	}

	/**
	 * Todo cheque cadastrado é tem o status EnumStatusCheque.EM_COMPENSACAO como valor padrão
	 * @return
	 */
	public Character getIdStatusCheque() {
		return idStatusCheque;
	}

	public void setIdStatusCheque(Character idStatusCheque) {
		this.idStatusCheque = idStatusCheque;
	}

	public EnumStatusCheque getStatusCheque() {
		return EnumStatusCheque.consultarPorId(idStatusCheque);
	}

	public void setStatusCheque(EnumStatusCheque statusCheque) {
		if (statusCheque != null)
			this.idStatusCheque = statusCheque.getId();
		this.statusCheque = statusCheque;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public Date getDataPrevistaCompensar() {
		return dataPrevistaCompensar;
	}

	public void setDataPrevistaCompensar(Date dataPrevistaCompensar) {
		this.dataPrevistaCompensar = dataPrevistaCompensar;
	}

	public Character getIdFormaPagamento() {
		return idFormaPagamento;
	}

	public void setIdFormaPagamento(Character idFormaPagamento) {
		this.idFormaPagamento = idFormaPagamento;
	}

	public EnumFormaPagamento getFormaPagamento() {
		return EnumFormaPagamento.consultarPorId(idFormaPagamento);
	}

	public void setFormaPagamento(EnumFormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
		if (formaPagamento != null) {
			this.idFormaPagamento = formaPagamento.getId();
		} else {
			this.idFormaPagamento = null;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PagamentoCheque:");
		sb.append(id);
		sb.append(" idMovel:");
		sb.append(getIdMovel());
		sb.append(" valor:");
		sb.append(valor);
		return sb.toString();
	}

}
