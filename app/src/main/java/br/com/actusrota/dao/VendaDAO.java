package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaVenda.COLUNAS_VENDA;
import static br.com.actusrota.tabela.TabelaVenda.COMISSAO_CLIENTE;
import static br.com.actusrota.tabela.TabelaVenda.COMISSAO_FUNCIONARIO;
import static br.com.actusrota.tabela.TabelaVenda.DATA_VENCIMENTO;
import static br.com.actusrota.tabela.TabelaVenda.DATA_VENDA;
import static br.com.actusrota.tabela.TabelaVenda.FK_CLIENTE;
import static br.com.actusrota.tabela.TabelaVenda.FK_VIAGEM;
import static br.com.actusrota.tabela.TabelaVenda.ID;
import static br.com.actusrota.tabela.TabelaVenda.ID_SATUS_VENDA;
import static br.com.actusrota.tabela.TabelaVenda.ID_VENDA_WEB;
import static br.com.actusrota.tabela.TabelaVenda.TABELA_VENDA;
import static br.com.actusrota.tabela.TabelaVenda.VALOR_TOTAL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemBrinde;
import br.com.actusrota.entidade.ItemBrindeExtra;
import br.com.actusrota.entidade.ItemDevolucao;
import br.com.actusrota.entidade.ItemTroca;
import br.com.actusrota.entidade.ItemVenda;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.entidade.PagamentoEspecie;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.Telefone;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.enumerador.EnumStatusModificacao;
import br.com.actusrota.enumerador.EnumStatusSincronizar;
import br.com.actusrota.enumerador.EnumStatusVenda;
import br.com.actusrota.tabela.TabelaContaReceber;
import br.com.actusrota.tabela.TabelaItem;
import br.com.actusrota.tabela.TabelaItemBrinde;
import br.com.actusrota.tabela.TabelaItemBrindeExtra;
import br.com.actusrota.tabela.TabelaItemDevolucao;
import br.com.actusrota.tabela.TabelaItemTroca;
import br.com.actusrota.tabela.TabelaItemVenda;
import br.com.actusrota.tabela.TabelaPagamentoCheque;
import br.com.actusrota.tabela.TabelaPagamentoEspecie;
import br.com.actusrota.tabela.TabelaVenda;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilMensagem;

public class VendaDAO extends GenericoDAO<Venda> implements IDAO<Venda> {

	private ProdutoDAO produtoDAO;
	private ClienteDAO clienteDAO;
	private ItemBrindeDAO itemBrindeDAO;
	private ItemBrindeExtraDAO itemBrindeExtraDAO;
	private ItemDevolucaoDAO itemDevolucaoDAO;
	private ItemTrocaDAO itemTrocaDAO;
	private PagmentoChequeDAO pagamentoChequeDAO;
	private PagmentoDinherioDAO pagamentoDinherioDAO;
	private CidadeDAO cidadeDAO;

	public VendaDAO(Context context) {
		super(context, TABELA_VENDA);
		produtoDAO = new ProdutoDAO(context);
		clienteDAO = new ClienteDAO(context);
		cidadeDAO = new CidadeDAO(context);

		itemBrindeDAO = new ItemBrindeDAO(context, getDbAdapter());
		itemBrindeExtraDAO = new ItemBrindeExtraDAO(context, getDbAdapter());
		itemDevolucaoDAO = new ItemDevolucaoDAO(context, getDbAdapter());
		itemTrocaDAO = new ItemTrocaDAO(context, getDbAdapter());
		pagamentoChequeDAO = new PagmentoChequeDAO(context, getDbAdapter());
		pagamentoDinherioDAO = new PagmentoDinherioDAO(context, getDbAdapter());
	}

	/**
	 * Toda venda importada tem id(neste caso representa o id web) Deve-se
	 * verificar se tem id movel, se existir faz um update senï¿½o um insert
	 * 
	 * @param dados
	 */
	@Override
	public void adicionar(Collection<Venda> dados) {
		for (Venda venda : dados) {

			inserirCliente(venda);

			venda.setIdVendaWeb(venda.getId());
			if (venda.getIdMovel() == null || venda.getIdMovel() == 0) {
				if (venda.isVendaWeb()) {
					Cursor cs = null;
					try {
						abrirBanco();
						cs = listar(COLUNAS_VENDA, ID_VENDA_WEB, venda
								.getIdVendaWeb().toString());
						if (cs.moveToFirst()) {// evita duplicação de venda
												// importada(importar a mesma
												// venda 2 vezes consecutivas)
							Venda vendaWeb = montarEntidade(cs);
							venda.setIdMovel(vendaWeb.getIdMovel());
							venda.setId(vendaWeb.getIdMovel());
							atualizarVendaImportada(venda);
						}
					} catch (Exception e) {
						e.printStackTrace();
						UtilMensagem.mostarMensagemLonga(contexto,
								"Erro ao salvar venda web:" + e.getMessage());
						return;
					} finally {
						if (cs != null)
							cs.close();
						// fecharBanco();
					}
				} else {
					venda.setId(null);
					adicionarImportada(venda);
				}

			} else {
				Venda movel = consultarPorId(venda.getIdMovel());// a venda web
																	// pode ter
																	// idMovel
																	// mas não
																	// estar
																	// cadastrada
				if (movel == null || movel.isNovo()) {
					venda.setId(null);
					adicionarImportada(venda);
					continue;
				}
				venda.setId(venda.getIdMovel());// se o id ja existir?
				atualizarVendaImportada(venda);
			}
		}
	}

	@Override
	public void adicionarImportacao(Collection<Venda> dados) {
		for (Venda vendaImportada : dados) {

			inserirCliente(vendaImportada);

			vendaImportada.setIdVendaWeb(vendaImportada.getId());
			Cursor cs = null;
			try {
				abrirBanco();
				cs = listar(COLUNAS_VENDA, ID_VENDA_WEB, vendaImportada
						.getIdVendaWeb().toString());
				// evita duplicação de venda importada(importar a mesma
				// venda 2 vezes consecutivas)
				// seta o idMovel na venda importada
				if (cs.moveToFirst()) {
					Venda vendaWebCadastradaMovel = montarEntidade(cs);
					vendaImportada.setIdMovel(vendaWebCadastradaMovel
							.getIdMovel());
					vendaImportada.setId(vendaWebCadastradaMovel.getIdMovel());
					// Venda importada sobrepõe a venda movel
					atualizarVendaImportada(vendaImportada);
				} else {
					vendaImportada.setId(null);
					adicionarImportada(vendaImportada);
				}
			} catch (Exception e) {
				e.printStackTrace();
				UtilMensagem.mostarMensagemLonga(contexto,
						"Erro ao salvar venda web:" + e.getMessage());
				return;
			} finally {
				if (cs != null)
					cs.close();
				fecharBanco();
			}

		}
	}

	/**
	 * Tudo novo, somente insert
	 * 
	 * @param venda
	 */
	public void adicionarImportada(Venda venda) {
		try {
			// venda.setStatusModificacao(EnumStatusModificacao.NAO_MODIFICADA);
			// TODO: mudar o tipo de transação
			super.adicionarImportacao(venda);

			Long idAtual = null;
			if (venda.isNovo()) {
				try {
					idAtual = buscarMaxID();
					venda.setId(idAtual);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				for (ItemVenda item : venda.getItensVenda()) {
					item.getVenda().setId(idAtual);
				}
			}

			abrirBanco();
			iniciarTransacao();

			adicionarItensSemComitar(venda.getItensVenda());

			adicionarItensImportados(venda);
			adicionarPagamentos(venda);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(venda);
			System.out.println(venda.getItensVenda());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public void atualizarVendaImportada(Venda venda) {
		try {
			// TODO: mudar o tipo de transação
			super.atualizar(venda.getId(), venda);

			// Seta o idMovel da venda nos itens
			for (ItemVenda item : venda.getItensVenda()) {
				item.getVenda().setId(venda.getId());
			}

			abrirBanco();
			iniciarTransacao();

			// adicionarItensSemComitar(venda.getItensVenda());
			// adicionarItensImportados(venda);

			atualizarItensSemComitar(venda.getItensVenda());
			atualizarItensImportados(venda);

			atualizarPagamentos(venda);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(venda);
			System.out.println(venda.getItensVenda());
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	/**
	 * Os pagamentos que chegam chegam com idWeb e idMovel definidos
	 * 
	 * @param venda
	 */
	private void atualizarPagamentos(Venda venda) {
		for (PagamentoCheque pagamentoWeb : venda.getPagamentosCheque()) {
			pagamentoWeb.setIdVenda(venda.getId());
			pagamentoWeb.setIdViagem(venda.getViagem().getId());

			// boolean cadastrado = pagamentoChequeDAO.isCadastrado(
			// TabelaContaReceber.ID_WEB, pagamentoWeb.getIdWeb());

			PagamentoCheque pagamento = pagamentoChequeDAO.consultar(
					TabelaContaReceber.ID_WEB, pagamentoWeb.getIdWeb());

			if (pagamento != null) {
				pagamentoWeb.setId(pagamento.getId());
				pagamentoChequeDAO.atualizarSemComitar(pagamentoWeb.getId(),
						pagamentoWeb);
			} else {
				pagamentoWeb.setId(null);
				pagamentoChequeDAO.adicionarSemComitar(pagamentoWeb);
			}

			// if (pagamentoWeb.getIdMovel() == null
			// || pagamentoWeb.getIdMovel() <= 0) {
			// pagamentoWeb.setId(null);
			// pagamentoChequeDAO.adicionarSemComitar(pagamentoWeb);
			// } else {
			// if (cadastrado) {
			// pagamentoWeb.setId(pagamentoWeb.getIdMovel());
			// pagamentoChequeDAO.atualizarSemComitar(
			// pagamentoWeb.getIdMovel(), pagamentoWeb);
			// } else {
			// pagamentoWeb.setId(null);
			// pagamentoChequeDAO.adicionarSemComitar(pagamentoWeb);
			// }
			// }
			Log.i("PagamentoCheque", pagamentoWeb.toString());
		}

		for (PagamentoEspecie pagamentoWeb : venda.getPagamentosEspecie()) {
			pagamentoWeb.setIdVenda(venda.getId());
			pagamentoWeb.setIdViagem(venda.getViagem().getId());

			// boolean cadastrado = pagamentoDinherioDAO.isCadastrado(
			// TabelaContaReceber.ID_WEB, pagamentoWeb.getIdWeb());

			PagamentoEspecie pagamento = pagamentoDinherioDAO.consultar(
					TabelaContaReceber.ID_WEB, pagamentoWeb.getIdWeb());

			if (pagamento != null) {
				pagamentoWeb.setId(pagamento.getId());
				pagamentoDinherioDAO.atualizarSemComitar(pagamentoWeb.getId(),
						pagamentoWeb);
			} else {
				pagamentoWeb.setId(null);
				pagamentoDinherioDAO.adicionarSemComitar(pagamentoWeb);
			}
			Log.i("PagamentoEspecie", pagamentoWeb.toString());
			// Set<PagamentoEspecie> pagamentos = pagamentoDinherioDAO
			// .buscarParaExportar(venda.getId());
			// if (pagamentos.contains(pagamento)) {
			// pagamentoDinherioDAO.atualizarSemComitar(pagamento.getId(),
			// pagamento);
			// } else {
			// pagamentoDinherioDAO.adicionarSemComitar(pagamento);
			// }
		}

	}

	private void adicionarPagamentos(Venda venda) {
		if (!venda.isSemPagamentoCheque()) {
			for (PagamentoCheque pagamento : venda.getPagamentosCheque()) {
				pagamento.setIdVenda(venda.getId());
				pagamento.setId(null);
			}
			pagamentoChequeDAO.adicionarSemComitar(venda.getPagamentosCheque());
		}

		if (!venda.isSemPagamentoEspecie()) {
			for (PagamentoEspecie pagamento : venda.getPagamentosEspecie()) {
				pagamento.setIdVenda(venda.getId());
				pagamento.setId(null);
			}
			pagamentoDinherioDAO.adicionarSemComitar(venda
					.getPagamentosEspecie());
		}
	}

	private void adicionarItensImportados(Venda venda) {
		if (!venda.isSemBrinde()) {
			for (ItemBrinde item : venda.getItensBrinde()) {
				item.setVenda(venda);
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);
				item.setId(null);
			}
			itemBrindeDAO.adicionarSemComitar(venda.getItensBrinde());
		}

		if (!venda.isSemBrindeExtra()) {
			for (ItemBrindeExtra item : venda.getItensBrindeExtra()) {
				item.setVenda(venda);
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);
				item.setId(null);
			}
			itemBrindeExtraDAO.adicionarSemComitar(venda.getItensBrindeExtra());
		}

		if (!venda.isSemDevolucao()) {
			for (ItemDevolucao item : venda.getItensDevolucao()) {
				item.setVenda(venda);
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);
				item.setId(null);
			}
			itemDevolucaoDAO.adicionarSemComitar(venda.getItensDevolucao());
		}

		if (!venda.isSemTroca()) {
			for (ItemTroca item : venda.getItensTroca()) {
				item.setVenda(venda);
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);
				item.setId(null);
			}
			itemTrocaDAO.adicionarSemComitar(venda.getItensTroca());
		}

	}

	private void atualizarItensImportados(Venda venda) {
		if (!venda.isSemBrinde()) {

			for (ItemBrinde item : venda.getItensBrinde()) {
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);

				if (!itemBrindeDAO
						.isCadastrado(TabelaItem.ID_WEB, item.getId())) {
					item.setVenda(venda);
					item.setId(null);
					itemBrindeDAO.adicionarSemComitar(item);
				} else {
					itemBrindeDAO.atualizarSemComitar(item.getId(), item);
				}
			}
			// itemBrindeDAO.adicionarSemComitar(venda.getItensBrinde());
		}

		if (!venda.isSemBrindeExtra()) {
			for (ItemBrindeExtra item : venda.getItensBrindeExtra()) {
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);

				if (!itemBrindeExtraDAO.isCadastrado(TabelaItem.ID_WEB,
						item.getId())) {
					item.setVenda(venda);
					item.setId(null);
					itemBrindeExtraDAO.adicionarSemComitar(item);
				} else {
					itemBrindeExtraDAO.atualizarSemComitar(item.getId(), item);
				}
			}
			// itemBrindeExtraDAO.adicionarSemComitar(venda.getItensBrindeExtra());
		}

		if (!venda.isSemDevolucao()) {
			for (ItemDevolucao item : venda.getItensDevolucao()) {
				Long idWeb = item.getId();
				item.setIdWeb(idWeb);

				if (!itemDevolucaoDAO.isCadastrado(TabelaItem.ID_WEB,
						item.getId())) {
					item.setVenda(venda);
					item.setId(null);
					itemDevolucaoDAO.adicionarSemComitar(item);
				} else {
					itemDevolucaoDAO.atualizarSemComitar(item.getId(), item);
				}
			}
			// itemDevolucaoDAO.adicionarSemComitar(venda.getItensDevolucao());
		}

		if (!venda.isSemTroca()) {
			for (ItemTroca item : venda.getItensTroca()) {
				item.setVenda(venda);
				Long idWeb = item.getId();

				if (!itemTrocaDAO.isCadastrado(TabelaItem.ID_WEB, item.getId())) {
					item.setIdWeb(idWeb);
					item.setId(null);
					itemTrocaDAO.adicionarSemComitar(item);
				} else {
					itemTrocaDAO.atualizarSemComitar(item.getId(), item);
				}
			}
			// itemTrocaDAO.adicionarSemComitar(venda.getItensTroca());
		}

	}

	private void inserirCliente(Venda venda) {
		if (!venda.isNovo()) {
			Cliente cliente = null;
			try {
				cliente = clienteDAO.consultarPorId(venda.getCliente().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (cliente == null || cliente.isNovo()) {
					clienteDAO.adicionarCompleto(venda.getCliente());
				}
			} catch (Exception e) {
				e.printStackTrace();
				String msg = "Erro ao inserir cliente:" + venda.getCliente();
				throw new RuntimeException(msg);
			}

		}
	}

	@Override
	public void adicionarImportacao(Venda venda) {
		try {
			// venda.setStatusModificacao(EnumStatusModificacao.NAO_MODIFICADA);
			// TODO: mudar o tipo de transação
			super.adicionarImportacao(venda);

			Long idAtual = null;
			if (venda.isNovo()) {
				try {
					idAtual = buscarMaxID();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (ItemVenda item : venda.getItensVenda()) {
					item.getVenda().setId(idAtual);
				}
			}

			abrirBanco();
			iniciarTransacao();
			adicionarItensSemComitar(venda.getItensVenda());

			adicionarItensImportados(venda);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(venda);
			System.out.println(venda.getItensVenda());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	@Override
	public void adicionar(Venda venda) {
		try {
			venda.setStatusModificacao(EnumStatusModificacao.MODIFICADA);
			// TODO: mudar o tipo de transação
			super.adicionar(venda);

			Long idAtual = null;
			if (venda.isNovo()) {
				try {
					idAtual = buscarMaxID();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (ItemVenda item : venda.getItensVenda()) {
					item.getVenda().setId(idAtual);
				}
			}

			abrirBanco();
			iniciarTransacao();
			adicionarItensSemComitar(venda.getItensVenda());

			adicionarItensImportados(venda);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(venda);
			System.out.println(venda.getItensVenda());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	@Override
	public boolean atulizar(List<Venda> vendas) {
		for (Venda venda : vendas) {
			venda.setStatusModificacao(EnumStatusModificacao.MODIFICADA);
		}
		return super.atulizar(vendas);
	}

	@Override
	public boolean atualizar(long id, Venda venda) {
		venda.setStatusModificacao(EnumStatusModificacao.MODIFICADA);
		return super.atualizar(id, venda);
	}

	/**
	 * Não seta o status da venda como modificada
	 * 
	 * @param id
	 * @param venda
	 * @return
	 */
	public boolean atualizarVenda(long id, Venda venda) {
		return super.atualizar(id, venda);
	}

	@Override
	public boolean atualizarSemComitar(long id, Venda entidade) {
		entidade.setStatusModificacao(EnumStatusModificacao.MODIFICADA);
		return super.atualizarSemComitar(id, entidade);
	}

	public void adicionarItensSemComitar(Set<ItemVenda> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (ItemVenda item : dados) {
			Long idWeb = item.getId();
			item.setIdWeb(idWeb);
			item.setId(null);
			ContentValues values = criarContentValuesItens(item);
			listValues.add(values);
		}
		getDbAdapter().adicionarSemComitar(TabelaItemVenda.TABELA_ITEM_VENDA,
				listValues);
	}

	public void atualizarItensSemComitar(Set<ItemVenda> dados) {
		for (ItemVenda item : dados) {
			Long idWeb = item.getId();
			item.setIdWeb(idWeb);

			boolean cadastrado = isCadastrado(
					TabelaItemVenda.TABELA_ITEM_VENDA, TabelaItem.ID_WEB,
					item.getId());
			if (cadastrado) {
				atualizarSemComitar(TabelaItemVenda.TABELA_ITEM_VENDA,
						item.getId(), criarContentValuesItens(item));
			} else {
				item.setId(null);
				getDbAdapter().adicionarSemComitar(
						TabelaItemVenda.TABELA_ITEM_VENDA,
						criarContentValuesItens(item));
			}
		}
		// getDbAdapter().adicionarSemComitar(TabelaItemVenda.TABELA_ITEM_VENDA,
		// listValues);
	}

	/**
	 * Espera-se que toda venda tenha o id da viagem
	 */
	@Override
	public ContentValues criarContentValues(Venda venda) {
		ContentValues values = new ContentValues();
		values.put(ID, venda.getId());
		values.put(DATA_VENDA, UtilDates.formatarData(venda.getDataVenda()));

		values.put(DATA_VENCIMENTO,
				UtilDates.formatarData(venda.getDataVencimento()));

		values.put(VALOR_TOTAL, venda.getValorTotal().getValor());

		if (venda.getComissaoFuncinario() != null)
			values.put(COMISSAO_FUNCIONARIO, venda.getComissaoFuncinario()
					.getValor());

		if (venda.getComissaoClienteEspecie() != null)
			values.put(COMISSAO_CLIENTE, venda.getComissaoClienteEspecie()
					.getValor());

		values.put(ID_SATUS_VENDA, venda.getIdStatusVenda().toString());
		values.put(TabelaVenda.STATUS_SINCRONIZAR, venda.getStatusSincronizar()
				.ordinal());

		values.put(ID_VENDA_WEB, venda.getIdVendaWeb());

		values.put(FK_CLIENTE, venda.getCliente().getId());
		Long idViagem = venda.getViagem().getId();
		values.put(FK_VIAGEM, idViagem);
		values.put(TabelaVenda.FK_ROTA, venda.getRota().getId());

		values.put(TabelaVenda.STATUS_MODIFICACAO, venda.getStatusModificacao()
				.ordinal());
		return values;
	}

	public ContentValues criarContentValuesItens(ItemVenda item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemVenda.ID, item.getId());
		values.put(TabelaItemVenda.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemVenda.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemVenda.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemVenda.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemVenda.FK_VENDA, item.getVenda().getId());
		Long idWeb = item.getIdWeb();
		values.put(TabelaItemVenda.ID_WEB, idWeb);
		return values;
	}

	@Override
	public Venda consultarPorId(long id) {
		Venda venda = null;
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_VENDA, id);

			if (cs.moveToFirst()) {
				venda = montarEntidade(cs);
				venda.setItensVenda(buscarItens(venda));
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return venda;
	}

	public Venda buscarParaEnvioServidor(long idVenda) {
		Venda venda = null;
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_VENDA, idVenda);

			if (cs.moveToFirst()) {
				venda = montarEntidade(cs);
				venda.setItensVenda(buscarItensExportar(venda, idVenda));
				venda.setItensBrinde(itemBrindeDAO.buscarItensExportar(venda,
						idVenda));
				venda.setItensBrindeExtra(itemBrindeExtraDAO
						.buscarItensExportar(venda, idVenda));
				venda.setItensDevolucao(itemDevolucaoDAO.buscarItensExportar(
						venda, idVenda));
				venda.setItensTroca(itemTrocaDAO.buscarItensExportar(venda,
						idVenda));
				venda.setPagamentosCheque(pagamentoChequeDAO
						.buscarParaExportar(idVenda));
				venda.setPagamentosEspecie(pagamentoDinherioDAO
						.buscarParaExportar(idVenda));
			}
		} finally {
			cs.close();
			fecharBanco();
		}

		return venda;
	}

	/**
	 * Seta o idMovel com o valor do id Verifica se a venda é web, ou seja, foi
	 * importada: - se sim: seta o id com o idWeb, o servidor o id para
	 * atualizar a venda - se não: seta o id como null, o servidor inseri a
	 * venda
	 */
	public void carregarVendaCompletaExportar(Venda venda) {
		venda.setIdMovel(venda.getId());
		if (venda.isVendaWeb()) {
			venda.setId(venda.getIdVendaWeb());
		} else {
			venda.setId(null);
		}

		try {
			abrirBancoSomenteLeitura();

			venda.setItensVenda(buscarItensExportar(venda, venda.getIdMovel()));

			venda.setItensBrinde(itemBrindeDAO.buscarItensExportar(venda,
					venda.getIdMovel()));
			venda.setItensBrindeExtra(itemBrindeExtraDAO.buscarItensExportar(
					venda, venda.getIdMovel()));
			venda.setItensDevolucao(itemDevolucaoDAO.buscarItensExportar(venda,
					venda.getIdMovel()));
			venda.setItensTroca(itemTrocaDAO.buscarItensExportar(venda,
					venda.getIdMovel()));

			venda.setPagamentosCheque(pagamentoChequeDAO
					.buscarParaExportar(venda.getIdMovel()));

			venda.setPagamentosEspecie(pagamentoDinherioDAO
					.buscarParaExportar(venda.getIdMovel()));

			if (venda.getCliente().getTelefones() != null
					&& !venda.getCliente().getTelefones().isEmpty()) {
				for (Telefone tel : venda.getCliente().getTelefones()) {
					tel.setId(null);
				}
			}
		} finally {
			try {
				fecharBanco();
			} catch (Throwable e) {
				UtilMensagem.mostarMensagemLonga(
						contexto,
						"carregarVendaCompletaExportar:fecharBanco:"
								+ e.getMessage());
			}
		}
	}

	public List<Venda> carregarVendaSemItens(EnumStatusSincronizar status,
			EnumStatusModificacao statusModificacao) {
		List<Venda> vendas = new ArrayList<Venda>();
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();

			String[] arrayAtributos = new String[] {
					TabelaVenda.STATUS_SINCRONIZAR,
					TabelaVenda.STATUS_MODIFICACAO };

			cs = listar(COLUNAS_VENDA, arrayAtributos,
					String.valueOf(status.ordinal()),
					String.valueOf(statusModificacao.ordinal()));

			while (cs.moveToNext()) {
				Venda cidade = montarEntidade(cs);
				vendas.add(cidade);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;

	}

	public List<Venda> carregarVendaSemItens(EnumStatusVenda status) {
		List<Venda> vendas = new ArrayList<Venda>();
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = listar(COLUNAS_VENDA, TabelaVenda.ID_SATUS_VENDA, status
					.getId().toString());
			while (cs.moveToNext()) {
				Venda cidade = montarEntidade(cs);
				vendas.add(cidade);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;

	}

	/**
	 * Banco nï¿½o precisa ser aberto
	 * 
	 * @param venda
	 * @return
	 */
	public Set<ItemVenda> buscarItens(final Venda venda) {
		Cursor c = null;
		Set<ItemVenda> itens = new HashSet<ItemVenda>();
		try {
			c = listar(TabelaItemVenda.TABELA_ITEM_VENDA,
					TabelaItemVenda.COLUNAS_ITEM_VENDA,
					TabelaItemVenda.FK_VENDA, String.valueOf(venda.getId()));

			while (c.moveToNext()) {
				itens.add(montarItem(c, venda));
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	public Set<ItemVenda> buscarItensExportar(final Venda venda, long idVenda) {
		Cursor c = null;
		Set<ItemVenda> itens = new HashSet<ItemVenda>();
		try {
			c = listar(TabelaItemVenda.TABELA_ITEM_VENDA,
					TabelaItemVenda.COLUNAS_ITEM_VENDA,
					TabelaItemVenda.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				itens.add(montarItemExportar(c, venda));
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

//	COLUNAS_VENDA = { ID0, DATA_VENDA1,
//		DATA_VENCIMENTO2, VALOR_TOTAL3, COMISSAO_FUNCIONARIO4,
//		COMISSAO_CLIENTE5, ID_SATUS_VENDA6, STATUS_SINCRONIZAR7, ID_VENDA_WEB8,
//		FK_CLIENTE9, FK_VIAGEM10, FK_ROTA11, STATUS_MODIFICACAO12 };
	@Override
	public Venda montarEntidade(Cursor cs) {
		Venda venda = new Venda();

		venda.setId(cs.getLong(0));
		venda.setIdMovel(venda.getId());

		String dtVenda = cs.getString(1);
		if (dtVenda != null) {
			venda.setDataVenda(UtilDates.formatarData(dtVenda));
			// venda.setDataVenda(new Date(dtVenda));
		}

		String dtVencimento = cs.getString(2);
		if (dtVencimento != null) {
			venda.setDataVencimento(UtilDates.formatarData(dtVencimento));
			// venda.setDataVencimento(new Date(dtVencimento));
		}

		venda.setValorTotal(Dinheiro.valueOf(cs.getLong(3)));

		Long comissaoFuncionario = cs.getLong(4);
		if (comissaoFuncionario != null)
			venda.setComissaoFuncinario(Dinheiro.valueOf(comissaoFuncionario));

		Long comissaoCliente = cs.getLong(5);
		if (comissaoFuncionario != null)
			venda.setComissaoClienteEspecie(Dinheiro.valueOf(comissaoCliente));

		venda.setIdStatusVenda(cs.getString(6).charAt(0));

		venda.setStatusSincronizar(EnumStatusSincronizar
				.getEnumStatusSincronizar(cs.getInt(7)));

		venda.setIdVendaWeb(cs.getLong(8));
		venda.setCliente(clienteDAO.consultarPorId(cs.getLong(9)));
		venda.setViagem(new Viagem(cs.getLong(10)));
		venda.setRota(new Rota(cs.getLong(11)));

		venda.setStatusModificacao(EnumStatusModificacao
				.getEnumStatusModificacao(cs.getInt(12)));

		return venda;
	}

	public ItemVenda montarItem(Cursor cs, Venda venda) {
		ItemVenda item = new ItemVenda();
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setVenda(venda);// 5
		item.setIdWeb(cs.getLong(6));
		return item;
	}

	public ItemVenda montarItemExportar(Cursor cs, Venda venda) {
		ItemVenda item = new ItemVenda();
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setVenda(venda);// 5
		item.setIdWeb(cs.getLong(6));
		return item;
	}

	public List<Venda> pesquisarLike(String nomeCliente) {
		Cursor cs = null;
		List<Venda> vendas = new ArrayList<Venda>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(TabelaVenda.SQL_SELECT_CAMPOS);
			sql.append(" INNER JOIN cliente c on c.id = v.fk_cliente ");
			sql.append(" INNER JOIN pessoa p on p.id = c.id ");
			sql.append(" WHERE p.nome LIKE ?");
			cs = listarJoin(sql.toString(), "%" + nomeCliente + "%");

			while (cs.moveToNext()) {
				Venda c = montarEntidade(cs);
				vendas.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;
	}

	public List<Venda> carregarVendaSemItens(Long idCidade, Long idRota,
			EnumStatusVenda statusVenda) {
		List<Venda> vendas = new ArrayList<Venda>();
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();

			StringBuilder sql = new StringBuilder(TabelaVenda.SQL_SELECT_CAMPOS);
			sql.append(" INNER JOIN cliente c  ON c.id = v.fk_cliente ");
			sql.append(" INNER JOIN endereco e ON e.id = c.fk_endereco ");
			sql.append(" INNER JOIN cidade cid ON cid.id = e.fk_cidade ");
			sql.append(" INNER JOIN rota r     ON r.id = v.fk_rota ");
//			sql.append(" INNER JOIN viagem vg  ON vg.id = v.fk_viagem ");
			sql.append(" WHERE cid.id = ? AND r.id = ? AND v.id_status_venda = ? ");

			cs = listarJoin(sql.toString(), String.valueOf(idCidade),
					String.valueOf(idRota), statusVenda.getId().toString());

			while (cs.moveToNext()) {
				Venda cidade = montarEntidade(cs);
				vendas.add(cidade);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;
	}

	public List<Venda> carregarVendasSemItens() {
		List<Venda> vendas = new ArrayList<Venda>(0);
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();

			cs = listarTodos(COLUNAS_VENDA);
			if (cs != null) {
				int count = cs.getCount();
				while (cs.moveToNext()) {
					Venda cidade = montarEntidade(cs);
					vendas.add(cidade);
				}
				int size = vendas.size();
				System.out.println(count + "=" + size);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;
	}

	/**
	 * Apaga todas as pendencias da venda.
	 * 
	 * @param venda
	 * @return
	 */
	public boolean delete(Venda venda) {

		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			Long idVenda = venda.getId();

			// remover itens da venda
			for (ItemVenda item : venda.getItensVenda()) {
				deleteSemComitar(TabelaItemVenda.TABELA_ITEM_VENDA,
						item.getId());
			}

			for (ItemBrinde item : venda.getItensBrinde()) {
				itemBrindeDAO.deleteSemComitar(
						TabelaItemBrinde.TABELA_ITEM_BRINDE, item.getId());
			}

			for (ItemBrindeExtra item : venda.getItensBrindeExtra()) {
				itemBrindeExtraDAO.deleteSemComitar(
						TabelaItemBrindeExtra.TABELA_ITEM_BRINDE_EXTRA,
						item.getId());
			}

			for (ItemDevolucao item : venda.getItensDevolucao()) {
				itemDevolucaoDAO
						.deleteSemComitar(
								TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO,
								item.getId());
			}

			for (ItemTroca item : venda.getItensTroca()) {
				itemTrocaDAO.deleteSemComitar(
						TabelaItemTroca.TABELA_ITEM_TROCA, item.getId());
			}

			for (PagamentoCheque pagamento : venda.getPagamentosCheque()) {
				pagamentoChequeDAO.deleteSemComitar(
						TabelaPagamentoCheque.TABELA_PAG_CHEQUE,
						pagamento.getId());
			}

			for (PagamentoEspecie pagamento : venda.getPagamentosEspecie()) {
				pagamentoDinherioDAO.deleteSemComitar(
						TabelaPagamentoEspecie.TABELA_PAG_ESPECIE,
						pagamento.getId());
			}

			deleteSemComitar(TabelaVenda.TABELA_VENDA, idVenda);

			comitarTransacao();
			resposta = true;
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		if (resposta)
			venda = new Venda();
		return resposta;
	}

	public Venda carregarVendaCompleta(long idVenda) {
		Venda venda = consultarPorId(idVenda);
		try {
			abrirBancoSomenteLeitura();

			venda.setItensVenda(buscarItens(venda));
			venda.setItensBrinde(itemBrindeDAO.buscarItens(venda.getId()));
			venda.setItensBrindeExtra(itemBrindeExtraDAO.buscarItens(venda
					.getId()));
			venda.setItensDevolucao(itemDevolucaoDAO.buscarItens(venda.getId()));
			venda.setItensTroca(itemTrocaDAO.buscarItens(venda.getId()));
			venda.setPagamentosCheque(pagamentoChequeDAO.buscarPor(venda
					.getId()));
			venda.setPagamentosEspecie(pagamentoDinherioDAO.buscarPor(venda
					.getId()));
		} finally {
			fecharBanco();
		}
		return venda;
	}

	/**
	 * Carrega a devolução, a troca e os recebimentos
	 * 
	 * @param idVenda
	 * @return
	 */
	public Venda carregarVendaReceber(long idVenda) {
		Venda venda = consultarPorId(idVenda);
		try {
			abrirBancoSomenteLeitura();

			venda.setItensVenda(buscarItens(venda));
			venda.setItensDevolucao(itemDevolucaoDAO.buscarItens(venda.getId()));
			venda.setItensTroca(itemTrocaDAO.buscarItens(venda.getId()));
			venda.setPagamentosCheque(pagamentoChequeDAO.buscarPor(venda
					.getId()));
			venda.setPagamentosEspecie(pagamentoDinherioDAO.buscarPor(venda
					.getId()));
		} finally {
			fecharBanco();
		}
		return venda;
	}

	public List<Venda> pesquisarLike(String nomeCliente, Date dataVenda) {
		Cursor cs = null;
		List<Venda> vendas = new ArrayList<Venda>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(TabelaVenda.SQL_SELECT_CAMPOS);
			sql.append(" INNER JOIN cliente c on c.id = v.fk_cliente ");
			sql.append(" INNER JOIN pessoa p on p.id = c.id ");
			sql.append(" WHERE p.nome LIKE ?");
			sql.append(" AND data_venda = ?");
			cs = listarJoin(sql.toString(), "%" + nomeCliente + "%",
					UtilDates.formatarData(dataVenda));

			while (cs.moveToNext()) {
				Venda c = montarEntidade(cs);
				vendas.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return vendas;
	}
}
