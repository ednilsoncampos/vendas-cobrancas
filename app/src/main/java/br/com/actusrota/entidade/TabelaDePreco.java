package br.com.actusrota.entidade;

import java.util.Set;

public class TabelaDePreco implements IEntidade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
//	private Date dataCadastro;
	private Rota rota;

	private Set<ItemTabelaPreco> itensTabelaPreco;

	public TabelaDePreco() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Date getDataCadastro() {
//		return dataCadastro;
//	}
//
//	public void setDataCadastro(Date dataCadastro) {
//		this.dataCadastro = dataCadastro;
//	}
//
//	public String getDataString() {
//		SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
//		return formatData.format(dataCadastro);
//	}
	
	public void setDataString(String data) {
		
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Set<ItemTabelaPreco> getItensTabelaPreco() {
		return itensTabelaPreco;
	}

	public void setItensTabelaPreco(Set<ItemTabelaPreco> itensTabelaPreco) {
		this.itensTabelaPreco = itensTabelaPreco;
	}

	@Override
	public boolean isNovo() {
		return id == null || id <= 0;
	}

}
