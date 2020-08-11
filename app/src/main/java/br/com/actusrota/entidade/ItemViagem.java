package br.com.actusrota.entidade;


public class ItemViagem implements ItemManter, Comparable<ItemViagem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4877269000285958207L;

	
	private Long id;
	private Integer quantidade;
	private Dinheiro valorUnitario;
	private Dinheiro totalUnitario;
	
	private Produto produto;
	private Viagem viagem;
	private boolean excluido;

	public ItemViagem() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemViagem(Produto produto) {
		this.produto = produto;
		valorUnitario = Dinheiro.novo();
		totalUnitario = Dinheiro.novo();
	} 
	
	public ItemViagem(ItemManter item) {
		this.quantidade = item.getQuantidade();
		this.valorUnitario = item.getValorUnitario();
		this.totalUnitario = item.getTotalUnitario();
		this.produto = item.getProduto();
		this.viagem = item.getViagem();
	}	
	
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
		ItemViagem other = (ItemViagem) obj;
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

	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public Long getId() {
		return id;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public Dinheiro getValorUnitario() {
		return valorUnitario;
	}
	
	public Dinheiro getTotalUnitario() {
		return totalUnitario;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public Viagem getViagem() {
		return viagem;
	}
	
	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}
	
	public boolean isExcluido() {
		return excluido;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setValorUnitario(Dinheiro valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public void setTotalUnitario(Dinheiro totalUnitario) {
		this.totalUnitario = totalUnitario;
	}

	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}

	@Override
	public void setVenda(Venda venda) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Venda getVenda() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(ItemViagem item) {
		return produto.compareTo(item.getProduto());
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
