package br.com.actusrota.entidade;


public class ItemDevolucao implements ItemManter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idWeb;
	private Integer quantidade;
	
	private Dinheiro valorUnitario;
	private Dinheiro totalUnitario;
	
	private Produto produto;
	private Venda venda;
	private Viagem viagem;
	
	public ItemDevolucao() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemDevolucao(ItemManter item) {
		this.quantidade = item.getQuantidade();
		this.valorUnitario = item.getValorUnitario();
		this.totalUnitario = item.getTotalUnitario();
		this.produto = item.getProduto();
		this.venda = item.getVenda();
		this.viagem = item.getViagem();
	}	
	
	public ItemDevolucao(ItemListView item) {
		this.quantidade = item.getQuantidade();
		this.valorUnitario = item.getValorUnitario();
		this.totalUnitario = item.getTotalUnitario();
		this.produto = item.getProduto();
		this.id = item.getId();
	}		
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		ItemDevolucao other = (ItemDevolucao) obj;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
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
	
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
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
	
	/**
	 * ValorUnitario: no ato da devolução o preço do produto devolvido assume o preço do item da viagem atual.
	 * se necessário consultar o preço anterior, na data da venda, basta verificar a venda.
	 */
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

	public Long getIdWeb() {
		return idWeb;
	}

	public void setIdWeb(Long idWeb) {
		this.idWeb = idWeb;
	}

}
