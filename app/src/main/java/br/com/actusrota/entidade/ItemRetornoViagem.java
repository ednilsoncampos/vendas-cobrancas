package br.com.actusrota.entidade;

import java.io.Serializable;

public class ItemRetornoViagem implements Serializable, Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer quantidade;
	
	private Dinheiro valorUnitario;
	private Dinheiro totalUnitario;
	
	private Produto produto;
	private Viagem viagem;
	
	private boolean danificado;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((viagem == null) ? 0 : viagem.hashCode());
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
		ItemRetornoViagem other = (ItemRetornoViagem) obj;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (viagem == null) {
			if (other.viagem != null)
				return false;
		} else if (!viagem.equals(other.viagem))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Dinheiro getTotalUnitario() {
		return totalUnitario;
	}
	public void setTotalUnitario(Dinheiro totalUnitario) {
		this.totalUnitario = totalUnitario;
	}
	
	public Viagem getViagem() {
		return viagem;
	}
	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public void setValorUnitario(Dinheiro valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	@Override
	public Produto getProduto() {
		return this.produto;
	}
	@Override
	public Integer getQuantidade() {
		return this.quantidade;
	}
	
	@Override
	public Dinheiro getValorUnitario() {
		return this.valorUnitario;
	}
	
	@Override
	public boolean isNovo() {
		return id == null || id == 0;
	}
	
	@Override
	public boolean isExcluido() {
		return false;
	}
	public boolean isDanificado() {
		return danificado;
	}
	public void setDanificado(boolean danificado) {
		this.danificado = danificado;
	}
	@Override
	public Long getIdWeb() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setIdWeb(Long idWeb) {
		// TODO Auto-generated method stub
		
	}

}
