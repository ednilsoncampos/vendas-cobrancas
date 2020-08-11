package br.com.actusrota.entidade;

import java.util.Date;

import br.com.actusrota.enumerador.EnumStatusVenda;

public class ContaReceber implements IEntidade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Long id;
	protected Date dataPagamento;
	protected Dinheiro valor;
	
	protected boolean comissaoPaga;
	
	private Character idStatusVenda;
	private EnumStatusVenda statusVenda;
	
	// campos diferentes do sistema web
	private Long idVenda;//FK_VENDA_MOVEL
//	private Long idVendaWeb;
	private Long idViagem;
	private Long idWeb;
	private Long idMovel;
	
	public ContaReceber() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataPagamento == null) ? 0 : dataPagamento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		ContaReceber other = (ContaReceber) obj;
		if (dataPagamento == null) {
			if (other.dataPagamento != null)
				return false;
		} else if (!dataPagamento.equals(other.dataPagamento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dinheiro getValor() {
		return valor;
	}

	public void setValor(Dinheiro valor) {
		this.valor = valor;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public boolean isComissaoPaga() {
		return comissaoPaga;
	}

	public void setComissaoPaga(boolean comissaoPaga) {
		this.comissaoPaga = comissaoPaga;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public Long getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

	public Long getIdViagem() {
		return idViagem;
	}

	public void setIdViagem(Long idViagem) {
		this.idViagem = idViagem;
	}

	public Long getIdWeb() {
		return idWeb;
	}

	public void setIdWeb(Long idWeb) {
		this.idWeb = idWeb;
	}
//
//	public Long getIdVendaWeb() {
//		return idVendaWeb;
//	}
//
//	public void setIdVendaWeb(Long idVendaWeb) {
//		this.idVendaWeb = idVendaWeb;
//	}

	public Long getIdMovel() {
		return idMovel;
	}

	public void setIdMovel(Long idMovel) {
		this.idMovel = idMovel;
	}
	
	public Character getIdStatusVenda() {
		return idStatusVenda;
	}

	public void setIdStatusVenda(Character idStatusVenda) {
		this.idStatusVenda = idStatusVenda;
	}

	public EnumStatusVenda getStatusVenda() {
		return EnumStatusVenda.consultarPorId(idStatusVenda);
	}

	public void setStatusVenda(EnumStatusVenda statusVenda) {
		if (statusVenda != null)
			this.idStatusVenda = statusVenda.getId();
		this.statusVenda = statusVenda;
	}

}
