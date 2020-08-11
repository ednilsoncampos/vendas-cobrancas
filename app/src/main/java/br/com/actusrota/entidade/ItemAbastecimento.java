package br.com.actusrota.entidade;

import java.io.Serializable;
import java.util.Date;

public class ItemAbastecimento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1631518099695295035L;
	
	private Long id;
	private Dinheiro valorTotal;
	private int qtdLitros;
	private Dinheiro precoLitro;
	private Date dataAbastecimento;
	private Abastecimento abastecimento;
	private Cidade cidade;
	
	public ItemAbastecimento() {
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abastecimento == null) ? 0 : abastecimento.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime
				* result
				+ ((dataAbastecimento == null) ? 0 : dataAbastecimento
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((valorTotal == null) ? 0 : valorTotal.hashCode());
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
		ItemAbastecimento other = (ItemAbastecimento) obj;
		if (abastecimento == null) {
			if (other.abastecimento != null)
				return false;
		} else if (!abastecimento.equals(other.abastecimento))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (dataAbastecimento == null) {
			if (other.dataAbastecimento != null)
				return false;
		} else if (!dataAbastecimento.equals(other.dataAbastecimento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		return true;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dinheiro getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Dinheiro valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public int getQtdLitros() {
		return qtdLitros;
	}

	public void setQtdLitros(int qtdLitros) {
		this.qtdLitros = qtdLitros;
	}

	public Dinheiro getPrecoLitro() {
		return precoLitro;
	}

	public void setPrecoLitro(Dinheiro precoLitro) {
		this.precoLitro = precoLitro;
	}

	public Abastecimento getAbastecimento() {
		return abastecimento;
	}

	public void setAbastecimento(Abastecimento abastecimento) {
		this.abastecimento = abastecimento;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Date getDataAbastecimento() {
		return dataAbastecimento;
	}

	public void setDataAbastecimento(Date dataAbastecimento) {
		this.dataAbastecimento = dataAbastecimento;
	}

}
