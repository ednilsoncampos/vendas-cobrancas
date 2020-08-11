package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabTabelaDePreco.COLUNAS_TABELA_PRECO;
import static br.com.actusrota.tabela.TabTabelaDePreco.ID;
import static br.com.actusrota.tabela.TabTabelaDePreco.ROTA;
import static br.com.actusrota.tabela.TabTabelaDePreco.TABELA_PRECO;
import static br.com.actusrota.tabela.TabelaItemTabPreco.PRECO_MINIMO_VENDA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.TabelaDePreco;
import br.com.actusrota.tabela.TabelaItemTabPreco;

public class TabelaPrecoDAO extends GenericoDAO<TabelaDePreco> implements
		IDAO<TabelaDePreco> {

	private RotaDAO rotaDAO;
	private ProdutoDAO produtoDAO;

	public TabelaPrecoDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_PRECO);
		rotaDAO = new RotaDAO(context, dbAdapter);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}

	public TabelaPrecoDAO(Context context) {
		super(context, TABELA_PRECO);
		rotaDAO = new RotaDAO(context);
		produtoDAO = new ProdutoDAO(context);
	}

	@Override
	public ContentValues criarContentValues(TabelaDePreco tabelaDePreco) {
		ContentValues values = new ContentValues();
		values.put(ID, tabelaDePreco.getId());

		// if (tabelaDePreco.getDataCadastro() != null) {
		// values.put(DATA_CADASTRO,
		// UtilDates.formatarData(tabelaDePreco.getDataCadastro()));
		// }

		values.put(ROTA, tabelaDePreco.getRota().getId());
		return values;
	}

	public ContentValues criarContentValuesItens(ItemTabelaPreco item) {
		ContentValues values = new ContentValues();
		values.put(ID, item.getId());
		values.put(TabelaItemTabPreco.PRECO_VENDA, item.getPrecoVenda()
				.getValor());
		values.put(PRECO_MINIMO_VENDA, item.getPrecoMinimoVenda().getValor());
		values.put(TabelaItemTabPreco.PRODUTO, item.getProduto().getId());
		values.put(TabelaItemTabPreco.FK_TABELA_PRECO, item.getTabelaDePreco()
				.getId());
		return values;
	}

	public void adicionarItens(Set<ItemTabelaPreco> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (ItemTabelaPreco entidade : dados) {
			ContentValues values = criarContentValuesItens(entidade);
			listValues.add(values);
		}
		getDbAdapter().adicionar(TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO,
				listValues);
	}

	public void adicionarItensSemComitar(Set<ItemTabelaPreco> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (ItemTabelaPreco entidade : dados) {
			ContentValues values = criarContentValuesItens(entidade);
			listValues.add(values);
		}
		getDbAdapter().adicionarSemComitar(
				TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO, listValues);
	}

	@Override
	public TabelaDePreco consultarPorId(long id) {
		TabelaDePreco tabelaPreco = null;
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_TABELA_PRECO, id);

			if (cs.moveToFirst()) {
				tabelaPreco = montarEntidade(cs);
				tabelaPreco.setItensTabelaPreco(buscarItens(tabelaPreco));
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return tabelaPreco;
	}

	public TabelaDePreco pesquisarPor(Long idRota) {
		Cursor c = null;
		TabelaDePreco tabela = null;
		try {
			abrirBancoSomenteLeitura();
			c = listar(COLUNAS_TABELA_PRECO, ROTA, String.valueOf(idRota));
			if (c.moveToFirst())
				tabela = montarEntidade(c);
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return tabela;
	}

	public Set<ItemTabelaPreco> buscarItens(final TabelaDePreco tabelaPreco) {
		Cursor c = null;
		Set<ItemTabelaPreco> itens = new HashSet<ItemTabelaPreco>();
		try {
			c = listar(TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO,
					TabelaItemTabPreco.COLUNAS_ITEM_TAB_PRECO,
					TabelaItemTabPreco.FK_TABELA_PRECO,
					String.valueOf(tabelaPreco.getId()));

			while (c.moveToNext()) {
				ItemTabelaPreco item = montarItem(c, tabelaPreco);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	@Override
	public TabelaDePreco montarEntidade(Cursor cs) {
		TabelaDePreco tabelaPreco = new TabelaDePreco();
		tabelaPreco.setId(cs.getLong(0));
		// tabelaPreco.setDataCadastro(UtilDates.formatarData(cs.getString(1)));
		tabelaPreco.setRota(rotaDAO.consultarPorId(cs.getLong(1)));
		return tabelaPreco;
	}

	public ItemTabelaPreco montarItem(Cursor cs, TabelaDePreco tabelaPreco) {
		ItemTabelaPreco item = new ItemTabelaPreco();
		item.setTabelaDePreco(tabelaPreco);
		item.setId(cs.getLong(0));
		item.setPrecoVenda(Dinheiro.valueOf(cs.getLong(1)));
		item.setPrecoMinimoVenda(Dinheiro.valueOf(cs.getLong(2)));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(3)));
		return item;
	}

	@Override
	public void adicionarImportacao(Collection<TabelaDePreco> dados) {
		for (TabelaDePreco tabelaDePreco : dados) {
			adicionarImportacao(tabelaDePreco);
		}
	}

	@Override
	public void adicionarImportacao(TabelaDePreco tabelaDePreco) {
		try {
			delete(TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO,
					TabelaItemTabPreco.FK_TABELA_PRECO, tabelaDePreco.getId());
			delete(tabelaDePreco.getId());

			abrirBanco();
			iniciarTransacao();

			super.adicionarSemComitar(tabelaDePreco);

			adicionarItensSemComitar(tabelaDePreco.getItensTabelaPreco());

			comitarTransacao();
		} catch (Exception e) {
			System.out.println("Não inserido: " + tabelaDePreco);
			System.out.println("Não inserido: "
					+ tabelaDePreco.getItensTabelaPreco());
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

}
