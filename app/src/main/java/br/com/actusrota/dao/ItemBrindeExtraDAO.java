package br.com.actusrota.dao;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemBrindeExtra;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemBrinde;
import br.com.actusrota.tabela.TabelaItemBrindeExtra;

public class ItemBrindeExtraDAO extends GenericoDAO<ItemBrindeExtra> implements IDAO<ItemBrindeExtra> {

	private ProdutoDAO produtoDAO;
	
	public ItemBrindeExtraDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaItemBrindeExtra.TABELA_ITEM_BRINDE_EXTRA);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}

	public ItemBrindeExtraDAO(Context context) {
		super(context, TabelaItemBrindeExtra.TABELA_ITEM_BRINDE_EXTRA);
		produtoDAO = new ProdutoDAO(context);
	}
	
	@Override
	public ContentValues criarContentValues(ItemBrindeExtra item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemBrindeExtra.ID, item.getId());
		values.put(TabelaItemBrindeExtra.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemBrindeExtra.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemBrindeExtra.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemBrindeExtra.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemBrindeExtra.FK_VENDA, item.getVenda().getId());
		values.put(TabelaItemBrindeExtra.FK_VIAGEM, item.getViagem().getId());
		values.put(TabelaItemBrinde.ID_WEB, item.getIdWeb());
		return values;
	}

	@Override
	public ItemBrindeExtra montarEntidade(Cursor cs) {
		ItemBrindeExtra item = new ItemBrindeExtra();
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
	
	public ItemBrindeExtra montarEntidadeExportar(Cursor cs,Venda venda) {
		ItemBrindeExtra item = new ItemBrindeExtra();
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
	public ItemBrindeExtra consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<ItemBrindeExtra> buscarItens(final long idVenda) {
		Cursor c = null;
		Set<ItemBrindeExtra> itens = new HashSet<ItemBrindeExtra>();
		try {
			c = listar(TabelaItemBrindeExtra.TABELA_ITEM_BRINDE_EXTRA,
					TabelaItemBrindeExtra.COLUNAS_ITEM_BRINDE_EXTRA,
					TabelaItemBrindeExtra.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemBrindeExtra item = montarEntidade(c);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}	
	
	public Set<ItemBrindeExtra> buscarItensExportar(Venda venda,final long idVenda) {
		Cursor c = null;
		Set<ItemBrindeExtra> itens = new HashSet<ItemBrindeExtra>();
		try {
			c = listar(TabelaItemBrindeExtra.TABELA_ITEM_BRINDE_EXTRA,
					TabelaItemBrindeExtra.COLUNAS_ITEM_BRINDE_EXTRA,
					TabelaItemBrindeExtra.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemBrindeExtra item = montarEntidadeExportar(c, venda);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

}
