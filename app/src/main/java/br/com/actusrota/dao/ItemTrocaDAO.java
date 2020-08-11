package br.com.actusrota.dao;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemTroca;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemBrinde;
import br.com.actusrota.tabela.TabelaItemTroca;

public class ItemTrocaDAO extends GenericoDAO<ItemTroca> implements
		IDAO<ItemTroca> {

	private ProdutoDAO produtoDAO;

	public ItemTrocaDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaItemTroca.TABELA_ITEM_TROCA);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}

	public ItemTrocaDAO(Context context) {
		super(context, TabelaItemTroca.TABELA_ITEM_TROCA);
		produtoDAO = new ProdutoDAO(context);
	}

	@Override
	public ContentValues criarContentValues(ItemTroca item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemTroca.ID, item.getId());
		values.put(TabelaItemTroca.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemTroca.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemTroca.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemTroca.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemTroca.FK_VENDA, item.getVenda().getId());
		values.put(TabelaItemTroca.FK_VIAGEM, item.getViagem().getId());
		values.put(TabelaItemBrinde.ID_WEB, item.getIdWeb());
		return values;
	}

	@Override
	public ItemTroca montarEntidade(Cursor cs) {
		ItemTroca item = new ItemTroca();
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setVenda(new Venda(cs.getLong(5)));
		item.setViagem(new Viagem(cs.getLong(6)));
		item.setIdWeb(cs.getLong(7));
		return item;
	}

	public ItemTroca montarEntidadeExportar(Cursor cs, Venda venda) {
		ItemTroca item = new ItemTroca();
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setVenda(venda);
		item.setViagem(new Viagem(cs.getLong(6)));
		item.setIdWeb(cs.getLong(7));
		return item;
	}

	@Override
	public ItemTroca consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<ItemTroca> buscarItens(final long idVenda) {
		Cursor c = null;
		Set<ItemTroca> itens = new HashSet<ItemTroca>();
		try {
			c = listar(TabelaItemTroca.TABELA_ITEM_TROCA,
					TabelaItemTroca.COLUNAS_ITEM_TROCA,
					TabelaItemTroca.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemTroca item = montarEntidade(c);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	public Set<ItemTroca> buscarItensExportar(Venda venda, final long idVenda) {
		Cursor c = null;
		Set<ItemTroca> itens = new HashSet<ItemTroca>();
		try {
			c = listar(TabelaItemTroca.TABELA_ITEM_TROCA,
					TabelaItemTroca.COLUNAS_ITEM_TROCA,
					TabelaItemTroca.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemTroca item = montarEntidadeExportar(c, venda);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}
}
