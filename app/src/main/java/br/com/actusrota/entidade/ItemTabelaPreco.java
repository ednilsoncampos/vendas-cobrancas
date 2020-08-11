package br.com.actusrota.entidade;


public class ItemTabelaPreco implements IEntidade, Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Produto produto;
	private TabelaDePreco tabelaDePreco;
	
	private Dinheiro precoVenda;
	private Dinheiro precoMinimoVenda;
	//Campo não deveria exister
	private Integer quantidade;
	
	public ItemTabelaPreco() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemTabelaPreco(Long id, Produto produto,
			TabelaDePreco tabelaDePreco, Dinheiro precoVenda) {
		super();
		this.id = id;
		this.produto = produto;
		this.tabelaDePreco = tabelaDePreco;
		this.precoVenda = precoVenda;
	}

	public ItemTabelaPreco(ItemListView item) {
		this.quantidade = item.getQuantidade();
		this.precoVenda = item.getValorUnitario();
		this.produto = item.getProduto();
		this.id = item.getId();		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result
				+ ((tabelaDePreco == null) ? 0 : tabelaDePreco.hashCode());
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
		ItemTabelaPreco other = (ItemTabelaPreco) obj;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (tabelaDePreco == null) {
			if (other.tabelaDePreco != null)
				return false;
		} else if (!tabelaDePreco.equals(other.tabelaDePreco))
			return false;
		return true;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public TabelaDePreco getTabelaDePreco() {
		return tabelaDePreco;
	}

	public void setTabelaDePreco(TabelaDePreco tabelaDePreco) {
		this.tabelaDePreco = tabelaDePreco;
	}

	public Dinheiro getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Dinheiro precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Dinheiro getPrecoMinimoVenda() {
		return precoMinimoVenda;
	}


	public void setPrecoMinimoVenda(Dinheiro precoMinimoVenda) {
		this.precoMinimoVenda = precoMinimoVenda;
	}

	@Override
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	@Override
	public Integer getQuantidade() {
		return quantidade;
	}


	@Override
	public Dinheiro getValorUnitario() {
		return null;
	}


	@Override
	public Dinheiro getTotalUnitario() {
		return null;
	}


	@Override
	public boolean isNovo() {
		return id == null || id <= 0;
	}


	@Override
	public boolean isExcluido() {
		return false;
	}


	@Override
	public void setValorUnitario(Dinheiro valor) {
		
	}

	@Override
	public void setTotalUnitario(Dinheiro somar) {
		
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
