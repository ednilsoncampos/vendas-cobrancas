package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaItemTabPreco.COLUNAS_ITEM_TAB_PRECO;
import static br.com.actusrota.tabela.TabelaItemTabPreco.FK_TABELA_PRECO;
import static br.com.actusrota.tabela.TabelaItemTabPreco.ID;
import static br.com.actusrota.tabela.TabelaItemTabPreco.PRECO_MINIMO_VENDA;
import static br.com.actusrota.tabela.TabelaItemTabPreco.PRECO_VENDA;
import static br.com.actusrota.tabela.TabelaItemTabPreco.PRODUTO;
import static br.com.actusrota.tabela.TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.TabelaDePreco;
import br.com.actusrota.tabela.TabelaItemTabPreco;

public class ItemTabelaPrecoDAO extends GenericoDAO<ItemTabelaPreco> {

	private ProdutoDAO produtoDAO;

	public ItemTabelaPrecoDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_ITEM_TAB_PRECO);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}
	
	public ItemTabelaPrecoDAO(Context context) {
		super(context, TABELA_ITEM_TAB_PRECO);
		produtoDAO = new ProdutoDAO(context);
	}

	public List<ItemTabelaPreco> getItens() {
		Cursor c = null;
		List<ItemTabelaPreco> itens = new ArrayList<ItemTabelaPreco>();
		try {
			abrirBancoSomenteLeitura();
			c = listarTodos(COLUNAS_ITEM_TAB_PRECO);

			while (c.moveToNext()) {
				ItemTabelaPreco a = montarEntidade(c);
				itens.add(a);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return itens;
	}

	@Override
	public ContentValues criarContentValues(ItemTabelaPreco item) {
		ContentValues values = new ContentValues();
		values.put(ID, item.getId());
		values.put(PRECO_VENDA, item.getPrecoVenda().getValor());
		values.put(PRECO_MINIMO_VENDA, item.getPrecoMinimoVenda().getValor());
		values.put(PRODUTO, item.getProduto().getId());
		values.put(FK_TABELA_PRECO, item.getTabelaDePreco().getId());
		return values;
	}
	
	@Override
	public ItemTabelaPreco montarEntidade(Cursor cs) {
		ItemTabelaPreco item = new ItemTabelaPreco();
		item.setId(cs.getLong(0));
		item.setPrecoVenda(Dinheiro.valueOf(cs.getLong(1)));
		item.setPrecoMinimoVenda(Dinheiro.valueOf(cs.getLong(2)));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(3)));
		// item.setTabelaDePreco(tabelaPreco);
		return item;
	}	

	@Override
	public ItemTabelaPreco consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ItemTabelaPreco pesquisarPor(Produto produto, TabelaDePreco tabelaDePreco) {
		Cursor cs = null;
		ItemTabelaPreco item = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(TabelaItemTabPreco.SELECT);
			sql.append(" from ");
			sql.append(TabelaItemTabPreco.TABELA_ITEM_TAB_PRECO);
			sql.append(" where fk_produto = ? ");
			sql.append(" and fk_tabela_preco = ");
			sql.append(tabelaDePreco.getId());
			cs = listarJoin(sql.toString(), String.valueOf(produto.getId()));

			if (cs.moveToFirst()) {
				item = montarEntidade(cs);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return item;
	}
}
