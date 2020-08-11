package br.com.actusrota.entidade;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import br.com.actusrota.util.UtilDinheiro;

public class Viagem implements IEntidade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1912592741431137159L;

	private Long id;
	private Funcionario responsavel;
//	private Rota rota;
	private Date dataSaida;
	private Date dataRetorno;
	private Dinheiro valorTotalSaida;
	private Dinheiro adiantamento;
	private String veiculo;

	private TabelaDePreco tabelaDePreco;
	private Abastecimento abastecimento;

	private Set<Venda> vendas;
	private Set<Despesa> despesas;
	private Set<ItemViagem> itensViagem;
//	private Set<ItemRetornoViagem> itensRetorno;
	private Set<ItemBrinde> itensBrinde;
	private Set<ItemBrindeExtra> itensBrindeExtra;
	private Set<ItemDevolucao> itensDevolucao;
	private Set<ItemTroca> itensTroca;
	private Set<PagamentoCheque> pagamentosCheque;
	private Set<PagamentoEspecie> pagamentosEspecie;

	// atributos Transient
//	private Dinheiro totalRetorno;
	private Dinheiro totalDespesa;
	
	public Viagem() {
		initValoresTransient();
	}
	
	public Viagem(Long id) {
		super();
		this.id = id;
	}	

	public void initValoresTransient() {
//		this.totalRetorno = Dinheiro.novo();
		this.totalDespesa = Dinheiro.novo();
	}

	public void somarTodosItens() {
		if (!isNovo()) {
//			somarRetorno();
			somarDespesa();
		}
	}

//	private void somarRetorno() {
//		if (!itensRetorno.isEmpty()) {
//			for (ItemRetornoViagem item : itensRetorno) {
//				this.totalRetorno = UtilDinheiro.somar(this.totalRetorno,
//						item.getTotalUnitario());
//			}
//		}
//	}

	private void somarDespesa() {
		if (!despesas.isEmpty()) {
			for (Despesa despesa : despesas) {
				this.totalDespesa = UtilDinheiro.somar(this.totalDespesa,
						despesa.getValorDespesa());
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataSaida == null) ? 0 : dataSaida.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((responsavel == null) ? 0 : responsavel.hashCode());
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
		Viagem other = (Viagem) obj;
		if (dataSaida == null) {
			if (other.dataSaida != null)
				return false;
		} else if (!dataSaida.equals(other.dataSaida))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (responsavel == null) {
			if (other.responsavel != null)
				return false;
		} else if (!responsavel.equals(other.responsavel))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public Funcionario getResponsavel() {
		return responsavel;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public Date getDataRetorno() {
		return dataRetorno;
	}

	public Set<ItemViagem> getItensViagem() {
		if (itensViagem == null)
			itensViagem = new HashSet<ItemViagem>(0);
		return itensViagem;
	}

//	public Set<ItemRetornoViagem> getItensRetorno() {
//		return itensRetorno;
//	}
//
//	public void setItensRetorno(Set<ItemRetornoViagem> itensRetorno) {
//		this.itensRetorno = itensRetorno;
//	}

	public Set<Despesa> getDespesas() {
		return despesas;
	}

	public void setDespesas(Set<Despesa> despesas) {
		this.despesas = despesas;
	}

	public Dinheiro getValorTotalSaida() {
		return valorTotalSaida;
	}

	public Dinheiro getAdiantamento() {
		return adiantamento;
	}

	public void somarValorTotalSaida(Dinheiro totalUnitario) {
		this.valorTotalSaida = UtilDinheiro.somar(this.valorTotalSaida,
				totalUnitario);
	}

	public void subtrairValorTotalSaida(Dinheiro totalUnitario) {
		this.valorTotalSaida = UtilDinheiro.subtrair(this.valorTotalSaida,
				totalUnitario);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}

	public void setItensViagem(Set<ItemViagem> itensViagem) {
		this.itensViagem = itensViagem;
	}

	public void setValorTotalSaida(Dinheiro valorTotalSaida) {
		this.valorTotalSaida = valorTotalSaida;
	}

	public void setAdiantamento(Dinheiro adiantamento) {
		this.adiantamento = adiantamento;
	}

	public String getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

	public void setResponsavel(Funcionario responsavel) {
		this.responsavel = responsavel;
	}

	public TabelaDePreco getTabelaDePreco() {
		return tabelaDePreco;
	}

	public Abastecimento getAbastecimento() {
		return abastecimento;
	}

	public void setAbastecimento(Abastecimento abastecimento) {
		this.abastecimento = abastecimento;
	}

	public void setTabelaDePreco(TabelaDePreco tabelaDePreco) {
		this.tabelaDePreco = tabelaDePreco;
	}

	public Set<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}

	public Set<ItemBrinde> getItensBrinde() {
		return itensBrinde;
	}

	public void setItensBrinde(Set<ItemBrinde> itensBrinde) {
		this.itensBrinde = itensBrinde;
	}

	public Set<ItemDevolucao> getItensDevolucao() {
		return itensDevolucao;
	}

	public void setItensDevolucao(Set<ItemDevolucao> itensDevolucao) {
		this.itensDevolucao = itensDevolucao;
	}
	
	public Set<ItemTroca> getItensTroca() {
		return itensTroca;
	}

	public void setItensTroca(Set<ItemTroca> itensTroca) {
		this.itensTroca = itensTroca;
	}	

	public void setPagamentosEspecie(Set<PagamentoEspecie> pagamentosEspecie) {
		this.pagamentosEspecie = pagamentosEspecie;
	}

	public Set<PagamentoEspecie> getPagamentosEspecie() {
		return pagamentosEspecie;
	}

	/**
	 * viagem_pagamento_cheque senários para viagem: INSERT: nenhuma ação
	 * UPDATE: podem ser atualizados mediante circunstâncias específicas: -Ex: 1
	 * - ao salvar pagamento em Dinheiro não podem ser atualizados 2 - ao salvar
	 * pagamento em Cheque devem ser atualizados 3 - Como fazer? REMOVE: devem
	 * ser removidos
	 * 
	 * @return
	 */
	public Set<PagamentoCheque> getPagamentosCheque() {
		return pagamentosCheque;
	}

	public void setPagamentosCheque(Set<PagamentoCheque> pagamentosCheque) {
		this.pagamentosCheque = pagamentosCheque;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public void prepararExcluirVenda(Venda venda) {
		this.pagamentosCheque = venda.getPagamentosCheque();
	}

//	public Dinheiro getTotalRetorno() {
//		return totalRetorno;
//	}
//
//	public void setTotalRetorno(Dinheiro totalRetorno) {
//		this.totalRetorno = totalRetorno;
//	}

	public Dinheiro getTotalDespesa() {
		return totalDespesa;
	}

	public void setTotalDespesa(Dinheiro totalDespesa) {
		this.totalDespesa = totalDespesa;
	}

	public Set<ItemBrindeExtra> getItensBrindeExtra() {
		return itensBrindeExtra;
	}

	public void setItensBrindeExtra(Set<ItemBrindeExtra> itensBrindeExtra) {
		this.itensBrindeExtra = itensBrindeExtra;
	}
	
	@Override
	public String toString() {
		if (isNovo())
			return super.toString();
		return "viagem:"+String.valueOf(id);
	}
	
	public ItemViagem buscarItem(String codigo) {
		for(ItemViagem item : itensViagem) {
			if(codigo.equals(item.getProduto().getCodigo())) {
				return item;
			}
		}
		return null;
	}


}
