package br.com.actusrota.entidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.actusrota.enumerador.EnumStatusModificacao;
import br.com.actusrota.enumerador.EnumStatusSincronizar;
import br.com.actusrota.enumerador.EnumStatusVenda;
import br.com.actusrota.util.UtilDinheiro;

public class Venda implements IEntidade, Comparable<Venda> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idMovel;
	private Date dataVenda;
	private Date dataVencimento;

	private Dinheiro valorTotal;

	private Dinheiro comissaoFuncinario;

	private Cliente cliente;

	private Character idStatusVenda;
	@SuppressWarnings("unused")
	private EnumStatusVenda statusVenda;

	private Set<ItemVenda> itensVenda;
	private Set<ItemBrinde> itensBrinde;
	private Set<ItemBrindeExtra> itensBrindeExtra;
	private Set<ItemDevolucao> itensDevolucao;
	private Set<ItemTroca> itensTroca;

	private Set<PagamentoCheque> pagamentosCheque;
	private Set<PagamentoEspecie> pagamentosEspecie;

	private Dinheiro comissaoClienteEspecie;// brinde em R$
	// atributos Transient
	private Dinheiro valorTotalBrindeProduto;
	private Dinheiro valorTotalBrindeExtra;
	private Dinheiro somaTotalBrinde;
	private Dinheiro valorTotalDevolucao;
	private Dinheiro valorTotalTroca;
	private Dinheiro valorTotalCheque;
	private Dinheiro valorTotalEspecie;
	private Dinheiro valorTotalRecebido;
	private Dinheiro valorReceber;
	private Dinheiro valorLucro;
//	private Long idViagem;
	private Viagem viagem;
//	private Long idRota;
	private Rota rota;
	private Long idVendaWeb;

	private String percentualVenda = "0";
	private String percentualBrindeProduto = "0";
	private String percentualBrindeRS = "0";

	private String percentualDevolucao = "0 %";
	private EnumStatusSincronizar statusSincronizar;

	private EnumStatusModificacao statusModificacao;

	// private String usuarioConexao;

	public Venda() {
		valorTotal = Dinheiro.novo();
		idStatusVenda = EnumStatusVenda.ABERTA.getId();
		statusVenda = EnumStatusVenda.ABERTA;
		statusSincronizar = EnumStatusSincronizar.NAO_ENVIADA;
		initValoresTransient();
		criarPagametnos();
	}

//	private Venda(Venda venda) {
//		super();
//		this.id = venda.id;
//		this.dataVenda = venda.dataVenda;
//		this.dataVencimento = venda.dataVencimento;
//		this.valorTotal = venda.valorTotal;
//		this.comissaoFuncinario = venda.comissaoFuncinario;
//		this.comissaoClienteEspecie = venda.comissaoClienteEspecie;
//		this.cliente = venda.cliente;
//		this.idStatusVenda = venda.idStatusVenda;
//		this.statusVenda = venda.statusVenda;
//		this.itensVenda = venda.itensVenda;
//		this.itensBrinde = venda.itensBrinde;
//		this.itensBrindeExtra = venda.itensBrindeExtra;
//		this.itensDevolucao = venda.itensDevolucao;
//		this.itensTroca = venda.itensTroca;
//		this.pagamentosCheque = venda.pagamentosCheque;
//		this.pagamentosEspecie = venda.pagamentosEspecie;
//		this.valorTotalBrinde = venda.valorTotalBrinde;
//		this.valorTotalBrindeExtra = venda.valorTotalBrindeExtra;
//		this.somaTotalBrinde = venda.somaTotalBrinde;
//		this.valorTotalDevolucao = venda.valorTotalDevolucao;
//		this.valorTotalTroca = venda.valorTotalTroca;
//		this.valorTotalCheque = venda.valorTotalCheque;
//		this.valorTotalEspecie = venda.valorTotalEspecie;
//		this.valorTotalRecebido = venda.valorTotalRecebido;
//		this.valorReceber = venda.valorReceber;
//		this.valorLucro = venda.valorLucro;
//		this.idViagem = venda.idViagem;
//		this.percentualVenda = venda.percentualVenda;
//		this.idMovel = venda.idMovel;
//		this.idRota = venda.getIdRota();
//	}

	private void criarPagametnos() {
		criarPagamentoEspecie();
		criarPagementosCheque();
	}

	public Venda(Long id) {
		this();
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result
				+ ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result
				+ ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idStatusVenda == null) ? 0 : idStatusVenda.hashCode());
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
		Venda other = (Venda) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (dataVencimento == null) {
			if (other.dataVencimento != null)
				return false;
		} else if (!dataVencimento.equals(other.dataVencimento))
			return false;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idStatusVenda == null) {
			if (other.idStatusVenda != null)
				return false;
		} else if (!idStatusVenda.equals(other.idStatusVenda))
			return false;
		return true;
	}

	private void initValoresTransient() {
		this.valorTotalBrindeProduto = Dinheiro.novo();
		this.setValorTotalBrindeExtra(Dinheiro.novo());
		this.valorTotalDevolucao = Dinheiro.novo();
		this.valorTotalTroca = Dinheiro.novo();
		this.somaTotalBrinde = Dinheiro.novo();
		this.valorTotalCheque = Dinheiro.novo();
		this.valorTotalEspecie = Dinheiro.novo();
		this.valorReceber = Dinheiro.novo();
		this.valorLucro = Dinheiro.novo();
		this.valorTotalRecebido = Dinheiro.novo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Dinheiro getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Dinheiro valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Dinheiro getComissaoFuncinario() {
		return comissaoFuncinario;
	}

	public void setComissaoFuncinario(Dinheiro comissaoFuncinario) {
		this.comissaoFuncinario = comissaoFuncinario;
	}

	public Dinheiro getComissaoClienteEspecie() {
		return comissaoClienteEspecie;
	}

	public void setComissaoClienteEspecie(Dinheiro comissaoClienteEspecie) {
		this.comissaoClienteEspecie = comissaoClienteEspecie;
	}

	public Character getIdStatusVenda() {
		return idStatusVenda;
	}

	public void setIdStatusVenda(Character idStatusVenda) {
		this.idStatusVenda = idStatusVenda;
	}

	public EnumStatusVenda getStatusVenda() {
		return EnumStatusVenda.consultarPorId(idStatusVenda);
	}

	public void setStatusVenda(EnumStatusVenda statusVenda) {
		if (statusVenda != null)
			this.idStatusVenda = statusVenda.getId();
		this.statusVenda = statusVenda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(Set<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public Set<ItemBrinde> getItensBrinde() {
		return itensBrinde;
	}

	public void setItensBrinde(Set<ItemBrinde> itensBrinde) {
		this.itensBrinde = itensBrinde;
	}

	/**
	 * CascadeType.ALL senï¿½rios: INSERT: 1 - sempre que houver ItemDevolucao e
	 * a venda for inserida ItemDevolucao tbm serï¿½ 2 - sempre que nï¿½o houver
	 * ItemDevolucao e a venda for inserida ItemDevolucao nï¿½o serï¿½ inserido
	 * UPDATE: 1 - se apï¿½s trazer itens do banco e algum ItemDevolucao for
	 * removido e a venda for atualizada o msm serï¿½ deletado 2 - se apï¿½s
	 * trazer itens do banco e algum ItemDevolucao for adicionado e a venda for
	 * atualizada o msm serï¿½ adicionado com os anteriores 3 -
	 * 
	 * @return
	 */
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
	 * venda_pagamento_cheque senï¿½rios para venda: INSERT: nenhuma aï¿½ï¿½o
	 * UPDATE: podem ser atualizados mediante circunstï¿½ncias especï¿½ficas:
	 * -Ex: 1 - ao salvar pagamento em Dinheiro nï¿½o podem ser atualizados 2 -
	 * ao salvar pagamento em Cheque devem ser atualizados 3 - Como fazer?
	 * REMOVE: devem ser removidos
	 * 
	 * @return
	 */
	public Set<PagamentoCheque> getPagamentosCheque() {
		return pagamentosCheque;
	}

	public void setPagamentosCheque(Set<PagamentoCheque> pagamentosCheque) {
		this.pagamentosCheque = pagamentosCheque;
	}

	public List<ContaReceber> getPagementos() {
		List<ContaReceber> pagamentos = new ArrayList<ContaReceber>();
		if (pagamentosEspecie != null)
			pagamentos.addAll(pagamentosEspecie);
		if (pagamentosCheque != null)
			pagamentos.addAll(pagamentosCheque);

		return pagamentos;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public boolean isVendaWeb() {
		return idVendaWeb != null && idVendaWeb > 0;
	}

	public boolean isVendaMovel() {
		return idVendaWeb != null && idVendaWeb > 0;
	}

	public Dinheiro getSomaTotalBrinde() {
		return somaTotalBrinde;
	}

	public void setSomaTotalBrinde(Dinheiro somaTotalBrinde) {
		this.somaTotalBrinde = somaTotalBrinde;
	}

	public Dinheiro getValorTotalDevolucao() {
		return valorTotalDevolucao;
	}

	public void setValorTotalDevolucao(Dinheiro valorTotalDevolucao) {
		this.valorTotalDevolucao = valorTotalDevolucao;
	}

	public Dinheiro getValorTotalTroca() {
		return valorTotalTroca;
	}

	public void setValorTotalTroca(Dinheiro valorTotalTroca) {
		this.valorTotalTroca = valorTotalTroca;
	}

	public Dinheiro getValorTotalCheque() {
		return valorTotalCheque;
	}

	public void setValorTotalCheque(Dinheiro valorTotalCheque) {
		this.valorTotalCheque = valorTotalCheque;
	}

	public Dinheiro getValorTotalEspecie() {
		return valorTotalEspecie;
	}

	public void setValorTotalEspecie(Dinheiro valorTotalEspecie) {
		this.valorTotalEspecie = valorTotalEspecie;
	}

	public void somarTodosItens() {
		if (!isNovo()) {
			// initValores();
			somarBrinde();
			somarBrindeExtra();
			somarTotalBrinde();
			somarDevolucao();
			somarTroca();
			somarRecebimentos();
			calcularValorReceber();
			calcularLucro();
		}
	}

	public void somarDevolucao() {
		if (itensDevolucao != null && !itensDevolucao.isEmpty()) {
			for (ItemDevolucao item : itensDevolucao) {
				this.valorTotalDevolucao = UtilDinheiro.somar(
						this.valorTotalDevolucao, item.getTotalUnitario());
			}
		}
	}

	public void somarTroca() {
		if (itensTroca != null && !itensTroca.isEmpty()) {
			for (ItemTroca item : itensTroca) {
				this.valorTotalTroca = UtilDinheiro.somar(this.valorTotalTroca,
						item.getTotalUnitario());
			}
		}
	}

	public void somarBrinde() {
		if (itensBrinde != null && !itensBrinde.isEmpty()) {
			for (ItemBrinde item : itensBrinde) {
				this.valorTotalBrindeProduto = UtilDinheiro.somar(
						this.valorTotalBrindeProduto, item.getTotalUnitario());
			}
		}
		// if (comissaoClienteEspecie != null) {
		// this.valorTotalBrinde = UtilDinheiro.somar(this.valorTotalBrinde,
		// comissaoClienteEspecie);
		// }
	}

	public void somarBrindeExtra() {
		if (itensBrindeExtra != null && !itensBrindeExtra.isEmpty()) {
			for (ItemBrindeExtra item : itensBrindeExtra) {
				this.valorTotalBrindeExtra = UtilDinheiro.somar(
						this.valorTotalBrindeExtra, item.getTotalUnitario());
			}
		}
	}

	private void somarCheque() {
		if (pagamentosCheque != null && !pagamentosCheque.isEmpty()) {
			for (PagamentoCheque pagamento : pagamentosCheque) {
				this.valorTotalCheque = UtilDinheiro.somar(
						this.valorTotalCheque, pagamento.getValor());
			}
		}
	}

	private void somarEspecie() {
		if (pagamentosEspecie != null && !pagamentosEspecie.isEmpty()) {
			for (PagamentoEspecie pagamento : pagamentosEspecie) {
				this.valorTotalEspecie = UtilDinheiro.somar(
						this.valorTotalEspecie, pagamento.getValor());
			}
		}
	}

	public void somarRecebimentos() {
		somarCheque();
		somarEspecie();
		somarTotalRecebido();
	}

	private void somarTotalRecebido() {
		this.valorTotalRecebido = UtilDinheiro.somarValores(
				this.valorTotalEspecie, valorTotalCheque);
	}

	private void somarTotalBrinde() {
		this.somaTotalBrinde = UtilDinheiro.somarValores(this.valorTotalBrindeProduto,
				valorTotalBrindeExtra, comissaoClienteEspecie);
	}

	public void calcularValorReceber() {
		Dinheiro soma = UtilDinheiro.somarValores(valorTotalCheque,
				valorTotalEspecie, valorTotalDevolucao, comissaoClienteEspecie);
		Dinheiro vendaTroca = UtilDinheiro.somarValores(valorTotal,
				valorTotalTroca);
		valorReceber = UtilDinheiro.subtrair(vendaTroca, soma);
	}

	public void calcularLucro() {
		Dinheiro totalRecebido = UtilDinheiro.somar(valorTotalCheque,
				valorTotalEspecie);
		Dinheiro totalBrinde = UtilDinheiro.somarValores(valorTotalBrindeProduto,
				valorTotalBrindeExtra, comissaoClienteEspecie);
		valorLucro = UtilDinheiro.subtrair(totalRecebido, totalBrinde);
	}

	public Dinheiro getValorReceber() {
		return valorReceber;
	}

	public Dinheiro getValorLucro() {
		return valorLucro;
	}

	@Override
	public String toString() {
		if (isNovo())
			return super.toString();
		return String.valueOf(id);
	}

	public List<ItemVenda> getListaItens() {
		return new ArrayList<ItemVenda>(itensVenda);
	}

//	public Long getIdViagem() {
//		return idViagem;
//	}
//
//	public void setIdViagem(Long idViagem) {
//		this.idViagem = idViagem;
//	}

	public Set<ItemBrindeExtra> getItensBrindeExtra() {
		return itensBrindeExtra;
	}

	public void setItensBrindeExtra(Set<ItemBrindeExtra> itensBrindeExtra) {
		this.itensBrindeExtra = itensBrindeExtra;
	}

	public Dinheiro getValorTotalBrindeExtra() {
		return valorTotalBrindeExtra;
	}

	public void setValorTotalBrindeExtra(Dinheiro valorTotalBrindeExtra) {
		this.valorTotalBrindeExtra = valorTotalBrindeExtra;
	}

	@Override
	public int compareTo(Venda venda) {
		return venda.getDataVenda().compareTo(dataVenda);
	}

	public void criarItensVenda() {
		setItensVenda(new HashSet<ItemVenda>(0));
	}

	public void criarItensBrinde() {
		setItensBrinde(new HashSet<ItemBrinde>(0));
	}

	public void criarItensBrindeExtra() {
		setItensBrindeExtra(new HashSet<ItemBrindeExtra>(0));
	}

	public void criarItensDevolucao() {
		setItensDevolucao(new HashSet<ItemDevolucao>(0));
	}

	public void criarItensTroca() {
		setItensTroca(new HashSet<ItemTroca>(0));
	}

	public void criarPagementosCheque() {
		setPagamentosCheque(new HashSet<PagamentoCheque>(0));
	}

	public void criarPagamentoEspecie() {
		setPagamentosEspecie(new HashSet<PagamentoEspecie>(0));
	}

	public void fecharVenda() {
		Dinheiro totalRecebimentos = somarRecebimentosFecharVenda();
		somarDevolucao();
//		Dinheiro totalBrinde = UtilDinheiro.somar(
//				getValorTotalBrindeProduto(),
//				getComissaoClienteEspecie());

		Dinheiro recebDevBrinde = UtilDinheiro.somarValores(totalRecebimentos,
				getValorTotalDevolucao(), getComissaoClienteEspecie());
		
		System.err.println("total recebimentos:" + totalRecebimentos);
		System.err.println("total devolução:" + getValorTotalDevolucao());
		System.err.println("brinde R$:" + getComissaoClienteEspecie());
		System.err.println("total venda:" + getValorTotal());
		System.err.println("dev + recb + comissao:" + recebDevBrinde);
		
		boolean resposta = recebDevBrinde.getValor() >= getValorTotal()
				.getValor();
		if (resposta)
			setStatusVenda(EnumStatusVenda.FECHADA);
		else
			setStatusVenda(EnumStatusVenda.EM_COMPENSACAO);
	}

	private Dinheiro somarRecebimentosFecharVenda() {
		Dinheiro totalRecebimentos = Dinheiro.novo();
		if (getPagamentosCheque() != null) {
			for (PagamentoCheque pagamento : getPagamentosCheque())
				totalRecebimentos = UtilDinheiro.somar(totalRecebimentos,
						pagamento.getValor());
		}
		if (getPagamentosEspecie() != null) {
			for (PagamentoEspecie pagamento : getPagamentosEspecie())
				totalRecebimentos = UtilDinheiro.somar(totalRecebimentos,
						pagamento.getValor());
		}
		return totalRecebimentos;
	}

	public EnumStatusSincronizar getStatusSincronizar() {
		return statusSincronizar;
	}

	public void setStatusSincronizar(EnumStatusSincronizar statusSincronizar) {
		this.statusSincronizar = statusSincronizar;
	}

	/*
	 * public String getUsuarioConexao() { return usuarioConexao; }
	 * 
	 * public void setUsuarioConexao(String usuarioConexao) {
	 * this.usuarioConexao = usuarioConexao; }
	 */

	public Long getIdVendaWeb() {
		return idVendaWeb;
	}

	public void setIdVendaWeb(Long idVendaWeb) {
		this.idVendaWeb = idVendaWeb;
	}

	public Long getIdMovel() {
		return idMovel;
	}

	public void setIdMovel(Long idMovel) {
		this.idMovel = idMovel;
	}

	public boolean isSemItensVenda() {
		return getItensVenda() == null || getItensVenda().isEmpty();
	}

	public boolean isSemBrinde() {
		return getItensBrinde() == null || getItensBrinde().isEmpty();
	}

	public boolean isSemDevolucao() {
		return getItensDevolucao() == null || getItensDevolucao().isEmpty();
	}

	public boolean isSemBrindeExtra() {
		return getItensBrindeExtra() == null || getItensBrindeExtra().isEmpty();
	}

	public boolean isSemTroca() {
		return getItensTroca() == null || getItensTroca().isEmpty();
	}

	public boolean isSemPagamentoCheque() {
		return getPagamentosCheque() == null || getPagamentosCheque().isEmpty();
	}

	public boolean isSemPagamentoEspecie() {
		return getPagamentosEspecie() == null
				|| getPagamentosEspecie().isEmpty();
	}
	
	/**
	 * Calcula o percentual de recebimentos sobre a venda
	 * 
	 * @return
	 */
	public String getPercentualRecebimentoVenda() {
		String percentualRecebimentoVenda = "0";
		if (valorTotal == null || valorTotal.isNovo()) {
			return percentualRecebimentoVenda;
		}
		try {

			double valor = (double) valorTotalRecebido.getValor() * 100
					/ valorTotal.getValor();
			percentualRecebimentoVenda = String.format("%1$,.2f", valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentualRecebimentoVenda;
	}		

	/**
	 * Calcula o percentual do brinde sobre a venda e os recebimentos
	 * 
	 * @return
	 */
	public String getPercentualVenda() {
		if (valorTotal == null || valorTotal.isNovo()) {
			return percentualVenda;
		}
		try {
			if (valorTotalBrindeProduto == null || valorTotalBrindeProduto.isNovo()) {
				return percentualVenda;
			}

			
			Dinheiro totalBrinde = UtilDinheiro.somar(valorTotalBrindeProduto,
					comissaoClienteEspecie == null ? Dinheiro.novo() : comissaoClienteEspecie);

			Dinheiro totalRecebido = UtilDinheiro.somar(valorTotalCheque,
					valorTotalEspecie);

			double valor = 0d;
			if (totalRecebido.getValor() > 0) {
				valor = (double) totalBrinde.getValor() * 100
						/ totalRecebido.getValor();
			}
			percentualVenda = String.format("%1$,.2f", valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentualVenda;
	}

	public void setPercentualVenda(String percentualVenda) {
		this.percentualVenda = percentualVenda;
	}

	/**
	 * Calcular percentual de devolução sobre a venda
	 * 
	 * @return
	 */
	public String getPercentualDevolucao() {
		if (valorTotal == null || valorTotal.isNovo()) {
			return percentualDevolucao;
		}
		try {
			if (valorTotalDevolucao == null || valorTotalDevolucao.isNovo()) {
				return percentualDevolucao;
			}
			double valor = (double) valorTotalDevolucao.getValor() * 100
					/ valorTotal.getValor();
			percentualDevolucao = String.format("%1$,.2f", valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentualDevolucao;
	}

	public void setPercentualDevolucao(String percentualDevolucao) {
		this.percentualDevolucao = percentualDevolucao;
	}

	public String getPercentualBrindeProduto() {
		if (valorTotal == null || valorTotal.isNovo()) {
			return percentualBrindeProduto;
		}
		try {
			// if (valorTotalBrinde == null || valorTotalBrinde.isNovo()) {
			// return percentualBrindeProduto +" %";
			// }
			Dinheiro valorTotalBrinde = Dinheiro.novo();
			if (itensBrinde != null && !itensBrinde.isEmpty()) {
				for (ItemBrinde item : itensBrinde) {
					valorTotalBrinde = UtilDinheiro.somar(valorTotalBrinde,
							item.getTotalUnitario());
				}
			}

			double valor = (double) valorTotalBrinde.getValor() * 100
					/ valorTotal.getValor();
			percentualBrindeProduto = String.format("%1$,.2f", valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentualBrindeProduto;
	}

	public void setPercentualBrindeProduto(String percentualBrindeProduto) {
		this.percentualBrindeProduto = percentualBrindeProduto;
	}

	public String getPercentualBrindeRS() {
		if (comissaoClienteEspecie != null) {
			double valor = (double) comissaoClienteEspecie.getValor() * 100
					/ valorTotal.getValor();
			percentualBrindeRS = String.format("%1$,.2f", valor);
		}
		return percentualBrindeRS;
	}

	public void setPercentualBrindeRS(String percentualBrindeRS) {
		this.percentualBrindeRS = percentualBrindeRS;
	}

	public Dinheiro getValorTotalRecebido() {
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(Dinheiro valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public EnumStatusModificacao getStatusModificacao() {
		return statusModificacao;
	}

	public void setStatusModificacao(EnumStatusModificacao statusModificacao) {
		this.statusModificacao = statusModificacao;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public Dinheiro getValorTotalBrindeProduto() {
		return valorTotalBrindeProduto;
	}

	public void setValorTotalBrindeProduto(Dinheiro valorTotalBrindeProduto) {
		this.valorTotalBrindeProduto = valorTotalBrindeProduto;
	}
	
}
