package br.com.actusrota.dao;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemBrinde;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemBrinde;

public class ItemBrindeDAO extends GenericoDAO<ItemBrinde> implements
		IDAO<ItemBrinde> {

	private ProdutoDAO produtoDAO;

	public ItemBrindeDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaItemBrinde.TABELA_ITEM_BRINDE);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}

	public ItemBrindeDAO(Context context) {
		super(context, TabelaItemBrinde.TABELA_ITEM_BRINDE);
		produtoDAO = new ProdutoDAO(context);
	}

	@Override
	public ContentValues criarContentValues(ItemBrinde item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemBrinde.ID, item.getId());
		values.put(TabelaItemBrinde.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemBrinde.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemBrinde.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemBrinde.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemBrinde.FK_VENDA, item.getVenda().getId());
		values.put(TabelaItemBrinde.FK_VIAGEM, item.getViagem().getId());
		Long idWeb = item.getIdWeb();
		values.put(TabelaItemBrinde.ID_WEB, idWeb);
		return values;
	}

	@Override
	public ItemBrinde montarEntidade(Cursor cs) {
		ItemBrinde item = new ItemBrinde();
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

	public ItemBrinde montarEntidadeExportar(Cursor cs, Venda venda) {
		ItemBrinde item = new ItemBrinde();
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
	public ItemBrinde consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<ItemBrinde> buscarItens(final long idVenda) {
		Cursor c = null;
		Set<ItemBrinde> itens = new HashSet<ItemBrinde>();
		try {
			c = listar(TabelaItemBrinde.TABELA_ITEM_BRINDE,
					TabelaItemBrinde.COLUNAS_ITEM_BRINDE,
					TabelaItemBrinde.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemBrinde item = montarEntidade(c);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	public Set<ItemBrinde> buscarItensExportar(Venda venda, final long idVenda) {
		Cursor c = null;
		Set<ItemBrinde> itens = new HashSet<ItemBrinde>();
		try {
			c = listar(TabelaItemBrinde.TABELA_ITEM_BRINDE,
					TabelaItemBrinde.COLUNAS_ITEM_BRINDE,
					TabelaItemBrinde.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemBrinde item = montarEntidadeExportar(c, venda);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

}
