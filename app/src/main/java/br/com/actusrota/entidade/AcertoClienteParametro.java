package br.com.actusrota.entidade;

import br.com.actusrota.enumerador.EnumOperacao;

public class AcertoClienteParametro implements IEntidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private int limitePercentual;
	private int margemPercentual;
	private EnumOperacao operacao;
	
	public AcertoClienteParametro() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((operacao == null) ? 0 : operacao.hashCode());
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
		AcertoClienteParametro other = (AcertoClienteParametro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operacao != other.operacao)
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLimitePercentual() {
		return limitePercentual;
	}

	public void setLimitePercentual(int limitePercentual) {
		this.limitePercentual = limitePercentual;
	}

	public int getMargemPercentual() {
		return margemPercentual;
	}

	public void setMargemPercentual(int margemPercentual) {
		this.margemPercentual = margemPercentual;
	}

	public EnumOperacao getOperacao() {
		return operacao;
	}

	public void setOperacao(EnumOperacao operacao) {
		this.operacao = operacao;
	}
	
	public int getMargemTotal() {
		return margemPercentual + limitePercentual;
	}

	@Override
	public boolean isNovo() {
		return id == null || id <= 0;
	}

}
