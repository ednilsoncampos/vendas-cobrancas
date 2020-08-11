package br.com.actusrota.entidade;

public interface Item extends IEntidade {
	Produto getProduto();
	void setProduto(Produto produto);
	Long getId();
	void setId(Long id);
	
	Long getIdWeb();
	void setIdWeb(Long idWeb);

	Integer getQuantidade();

	Dinheiro getValorUnitario();
	
	Dinheiro getTotalUnitario();
	
	boolean isNovo();

	boolean isExcluido();

	void setValorUnitario(Dinheiro valor);

	void setQuantidade(Integer i);

	void setTotalUnitario(Dinheiro somar);
}
