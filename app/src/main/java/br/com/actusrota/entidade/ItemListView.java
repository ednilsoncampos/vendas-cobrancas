package br.com.actusrota.entidade;

import java.io.Serializable;

import br.com.actusrota.util.UtilDinheiro;


public class ItemListView implements Serializable, Comparable<ItemListView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Integer quantidade;

	private Dinheiro valorUnitario;
	private Dinheiro totalUnitario;

	private Produto produto;
	
	public ItemListView() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemListView(Produto produto) {
		this();
		this.produto = produto;
	}	
	
	public ItemListView(ItemTabelaPreco item) {
		this();
		this.produto = item.getProduto();
//		this.id = item.getId(); deve setar o id??
		this.valorUnitario = item.getPrecoVenda();
	}	
	
	/**
	 * Quantidade deve ser informada pelo vendedor
	 * @param item
	 */
	public ItemListView(Item item) {
		this();
		this.id = item.getId();
		this.produto = item.getProduto();
		this.valorUnitario = item.getValorUnitario();
	}		
	
	public ItemListView(Integer quantidade, Dinheiro valorUnitario,
			Dinheiro totalUnitario, ItemTabelaPreco item) {
		this();
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.totalUnitario = totalUnitario;
		this.produto = item.getProduto();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
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
		ItemListView other = (ItemListView) obj;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Dinheiro getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Dinheiro valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Dinheiro getTotalUnitario() {
		return totalUnitario;
	}

	public void setTotalUnitario(Dinheiro totalUnitario) {
		this.totalUnitario = totalUnitario;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(quantidade);
		sb.append(" - ");
		sb.append(produto.getDescricao());
		sb.append(" - ");
		sb.append(valorUnitario);
		return sb.toString();
	}

	@Override
	public int compareTo(ItemListView item) {
		return produto.getDescricao().compareToIgnoreCase(item.getProduto().getDescricao());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	public Dinheiro getTotalItem() {
		return UtilDinheiro.multiplicar(valorUnitario, quantidade);				
	}
	
}
