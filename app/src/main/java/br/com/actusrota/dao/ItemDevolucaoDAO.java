package br.com.actusrota.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemDevolucao;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemBrinde;
import br.com.actusrota.tabela.TabelaItemDevolucao;

public class ItemDevolucaoDAO extends GenericoDAO<ItemDevolucao> implements
		IDAO<ItemDevolucao> {

	private ProdutoDAO produtoDAO;

	public ItemDevolucaoDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO);
		produtoDAO = new ProdutoDAO(context, dbAdapter);
	}

	public ItemDevolucaoDAO(Context context) {
		super(context, TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO);
		produtoDAO = new ProdutoDAO(context);
	}

	public List<ItemDevolucao> buscarItens(Viagem viagem) {
		Cursor c = null;
		List<ItemDevolucao> itens = new ArrayList<ItemDevolucao>();
		try {
			abrirBancoSomenteLeitura();
			String id = String.valueOf(viagem.getId());
			c = listar(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO,
					TabelaItemDevolucao.COLUNAS_ITEM_DEV,
					TabelaItemDevolucao.FK_VIAGEM, id);

			if (c != null) {
				while (c.moveToNext()) {
					ItemDevolucao item = montarEntidade(c);
					itens.add(item);
				}
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return itens;
	}

	@Override
	public ContentValues criarContentValues(ItemDevolucao item) {
		ContentValues values = new ContentValues();
		values.put(TabelaItemDevolucao.ID, item.getId());
		values.put(TabelaItemDevolucao.VALOR_UNITARIO, item.getValorUnitario()
				.getValor());
		values.put(TabelaItemDevolucao.TOTAL_UNITARIO, item.getTotalUnitario()
				.getValor());
		values.put(TabelaItemDevolucao.QUANTIDADE, item.getQuantidade());
		values.put(TabelaItemDevolucao.FK_PRODUTO, item.getProduto().getId());
		values.put(TabelaItemDevolucao.FK_VENDA, item.getVenda().getId());
		values.put(TabelaItemDevolucao.FK_VIAGEM, item.getViagem().getId());
		values.put(TabelaItemBrinde.ID_WEB, item.getIdWeb());
		return values;
	}

	@Override
	public ItemDevolucao montarEntidade(Cursor cs) {
		ItemDevolucao item = new ItemDevolucao();
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setVenda(new Venda(cs.getLong(5)));
		// item.setViagem(cs.getLong(6));
		item.setViagem(new Viagem(cs.getLong(6)));
		item.setIdWeb(cs.getLong(7));
		return item;
	}

	public ItemDevolucao montarEntidadeExportar(Cursor cs, Venda venda) {
		ItemDevolucao item = new ItemDevolucao();
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
	public ItemDevolucao consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<ItemDevolucao> buscarItens(final long idVenda) {
		Cursor c = null;
		Set<ItemDevolucao> itens = new HashSet<ItemDevolucao>();
		try {
			c = listar(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO,
					TabelaItemDevolucao.COLUNAS_ITEM_DEV,
					TabelaItemDevolucao.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemDevolucao item = montarEntidade(c);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	public Set<ItemDevolucao> buscarItensExportar(Venda venda,
			final long idVenda) {
		Cursor c = null;
		Set<ItemDevolucao> itens = new HashSet<ItemDevolucao>();
		try {
			c = listar(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO,
					TabelaItemDevolucao.COLUNAS_ITEM_DEV,
					TabelaItemDevolucao.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				ItemDevolucao item = montarEntidadeExportar(c, venda);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return itens;
	}

	public Set<ItemDevolucao> buscarItensViagem(final long idViagem) {
		Cursor c = null;
		Set<ItemDevolucao> itens = new HashSet<ItemDevolucao>();
		try {
			abrirBancoSomenteLeitura();
			c = listar(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO,
					TabelaItemDevolucao.COLUNAS_ITEM_DEV,
					TabelaItemDevolucao.FK_VIAGEM, String.valueOf(idViagem));

			while (c.moveToNext()) {
				ItemDevolucao item = montarEntidade(c);
				itens.add(item);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return itens;
	}

	public ItemDevolucao pesquisarPor(Produto produto) {
		Cursor cs = null;
		ItemDevolucao item = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(
					" select id, valor_unitario, total_unitario, quantidade, fk_produto, fk_venda, fk_viagem ");
			sql.append(" from ");
			sql.append(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO);
			sql.append(" where fk_produto = ? ");
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

	// public List<ItemDevolucao> getItens() {
	// Cursor c = null;
	// List<ItemDevolucao> itens = new ArrayList<ItemDevolucao>();
	// try {
	// abrirBancoSomenteLeitura();
	// c = listarTodos(TabelaItemDevolucao.COLUNAS_ITEM_DEV);
	//
	// while (c.moveToNext()) {
	// ItemDevolucao a = montarEntidade(c);
	// itens.add(a);
	// }
	// } finally {
	// if (c != null)
	// c.close();
	// fecharBanco();
	// }
	//
	// return itens;
	// }

}
