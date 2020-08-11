package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaViagem.ADIANTAMENTO;
import static br.com.actusrota.tabela.TabelaViagem.COLUNAS_VIAGEM;
import static br.com.actusrota.tabela.TabelaViagem.DATA_RETORNO;
import static br.com.actusrota.tabela.TabelaViagem.DATA_SAIDA;
import static br.com.actusrota.tabela.TabelaViagem.ID;
import static br.com.actusrota.tabela.TabelaViagem.TABELA_VIAGEM;
import static br.com.actusrota.tabela.TabelaViagem.VALOR_TOTAL_SAIDA;
import static br.com.actusrota.tabela.TabelaViagem.VEICULO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemViagem;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemViagem;
import br.com.actusrota.util.UtilDates;

public class ViagemDAO extends GenericoDAO<Viagem> implements IDAO<Viagem> {

//	private RotaDAO rotaDAO;
	private FuncionarioDAO funcionarioDAO;
	private ProdutoDAO produtoDAO;
//	private TabelaPrecoDAO tabelaPrecoDAO;
//	private ItemTabelaPrecoDAO itemTabelaPrecoDAO;

	public ViagemDAO(Context context) {
		super(context, TABELA_VIAGEM);
//		tabelaPrecoDAO = new TabelaPrecoDAO(context, getDbAdapter());
//		itemTabelaPrecoDAO = new ItemTabelaPrecoDAO(context, getDbAdapter());
//		rotaDAO = new RotaDAO(context, getDbAdapter());
		funcionarioDAO = new FuncionarioDAO(context, getDbAdapter());
		produtoDAO = new ProdutoDAO(context, getDbAdapter());
	}

	public List<Viagem> getViagens() {
		Cursor cs = null;
		List<Viagem> viagens = new ArrayList<Viagem>();
		try {
			abrirBancoSomenteLeitura();
			cs = listarTodos(COLUNAS_VIAGEM);

			while (cs.moveToNext()) {
				Viagem viagem = montarEntidade(cs);
				viagens.add(viagem);
			}
		} finally {
			cs.close();
			fecharBanco();
		}

		return viagens;
	}

	@Override
	public void adicionar(Collection<Viagem> dados) {
		for (Viagem viagem : dados) {
			adicionar(viagem);
		}
	}

	@Override
	public void adicionarImportacao(Collection<Viagem> dados) {
		for (Viagem viagem : dados) {
			adicionarImportacao(viagem);
		}
	}

	@Override
	public void adicionarImportacao(Viagem viagem) {
		try {
			deleteTodos();
			deleteTodos(TabelaItemViagem.TABELA_ITEM_VIAGEM);
			
			abrirBanco();
			
//			itemTabelaPrecoDAO.deleteTodos();
//			tabelaPrecoDAO.deleteTodos();
			
//			funcionarioDAO.adicionarPesquisarPessoa(viagem.getResponsavel());
			
			iniciarTransacao();

//			tabelaPrecoDAO.adicionarSemComitar(viagem.getTabelaDePreco());
//			tabelaPrecoDAO.adicionarItensSemComitar(viagem.getTabelaDePreco()
//					.getItensTabelaPreco());
			
			super.adicionarSemComitar(viagem);
			
			adicionarItensSemComitar(viagem.getItensViagem());
			
			comitarTransacao();
		} catch (Exception e) {
			System.out.println("Não inserido: "+viagem);
			System.out.println("Não inserido: "+viagem.getItensViagem());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	/**
	 * O DBAdapter foi compartilhado no construtor, por isso tabelaPrecoDAO tem
	 * a mesma transação
	 */
	@Override
	public void adicionar(Viagem viagem) {
		try {
			abrirBanco();
			iniciarTransacao();

//			tabelaPrecoDAO.adicionarSemComitar(viagem.getTabelaDePreco());
//			tabelaPrecoDAO.adicionarItensSemComitar(viagem.getTabelaDePreco()
//					.getItensTabelaPreco());
			
			super.adicionarSemComitar(viagem);
			adicionarItensSemComitar(viagem.getItensViagem());

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(viagem);
			System.out.println(viagem.getItensViagem());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public void adicionarItens(Set<ItemViagem> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (ItemViagem entidade : dados) {
			ContentValues values = criarContentValuesItens(entidade);
			listValues.add(values);
		}
		getDbAdapter().adicionar(TabelaItemViagem.TABELA_ITEM_VIAGEM,
				listValues);
	}

	public void adicionarItensSemComitar(Set<ItemViagem> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (ItemViagem entidade : dados) {
			ContentValues values = criarContentValuesItens(entidade);
			listValues.add(values);
		}
		getDbAdapter().adicionarSemComitar(TabelaItemViagem.TABELA_ITEM_VIAGEM,
				listValues);
	}

	@Override
	public ContentValues criarContentValues(Viagem viagem) {
		ContentValues values = new ContentValues();
		values.put(ID, viagem.getId());
		
		
		values.put(DATA_SAIDA, UtilDates.formatarData(viagem.getDataSaida()));

		if (viagem.getDataRetorno() != null)
			values.put(DATA_RETORNO, UtilDates.formatarData(viagem.getDataRetorno()));

		values.put(VALOR_TOTAL_SAIDA, viagem.getValorTotalSaida().getValor());

		if (viagem.getAdiantamento() != null)
			values.put(ADIANTAMENTO, viagem.getAdiantamento().getValor());

		values.put(VEICULO, viagem.getVeiculo());
//		values.put(RESPONSAVEL, viagem.getResponsavel().getId());
//		values.put(ROTA, viagem.getRota().getId());
//		values.put(TABELA_PRECO, viagem.getTabelaDePreco().getId());
		return values;
	}

	public ContentValues criarContentValuesItens(ItemViagem item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemViagem.ID, item.getId());
		values.put(TabelaItemViagem.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemViagem.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemViagem.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemViagem.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemViagem.FK_VIAGEM, item.getViagem().getId());
		return values;
	}

	@Override
	public Viagem consultarPorId(long id) {
		Cursor c = null;
		Viagem viagem = null;
		try {
			abrirBancoSomenteLeitura();
			// nao deveria abrir, ja esta aberto
			// tabelaPrecoDAO.abrirBancoSomenteLeitura();
			c = consultarPorId(COLUNAS_VIAGEM, id);
			if (c!= null && c.moveToFirst()) {
				viagem = montarEntidade(c);
				viagem.setItensViagem(buscarItens(viagem));
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return viagem;
	}

	public Set<ItemViagem> buscarItens(final Viagem viagem) {
		Cursor c = null;
		Set<ItemViagem> itens = new HashSet<ItemViagem>();
		try {
			c = listar(TabelaItemViagem.TABELA_ITEM_VIAGEM,
					TabelaItemViagem.COLUNAS_ITEM_VIAGEM,
					TabelaItemViagem.FK_VIAGEM, String.valueOf(viagem.getId()));
			
			while (c.moveToNext()) {
				ItemViagem item = montarItem(c, viagem);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	@Override
	public Viagem montarEntidade(Cursor cs) {
		Viagem viagem = new Viagem();
		viagem.setId(cs.getLong(0));

		String dataSaida = cs.getString(1);

		if (dataSaida != null)
			viagem.setDataSaida(UtilDates.formatarData(dataSaida));

		String dataRetorno = cs.getString(2);
		if (dataRetorno != null)
			viagem.setDataRetorno(UtilDates.formatarData(dataRetorno));

		Long totalSaida = cs.getLong(3);
		if (totalSaida != null)
			viagem.setValorTotalSaida(Dinheiro.valueOf(totalSaida));

		Long adiantamento = cs.getLong(4);
		if (adiantamento != null)
			viagem.setAdiantamento(Dinheiro.valueOf(adiantamento));

		viagem.setVeiculo(cs.getString(5));
		
//		viagem.setResponsavel(funcionarioDAO.consultarPorId(cs.getLong(6)));
		
//		viagem.setRota(rotaDAO.consultarPorId(cs.getLong(6)));
		
//		viagem.setTabelaDePreco(tabelaPrecoDAO.consultarPorId(cs.getLong(8)));
		return viagem;
	}

	public ItemViagem montarItem(Cursor cs, Viagem viagem) {
		ItemViagem item = new ItemViagem();
		item.setViagem(viagem);
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		return item;
	}

	public FuncionarioDAO getFuncionarioDAO() {
		return funcionarioDAO;
	}

}
