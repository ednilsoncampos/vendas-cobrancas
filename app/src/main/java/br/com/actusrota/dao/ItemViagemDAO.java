package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaCliente.SELECT;
import static br.com.actusrota.tabela.TabelaItemViagem.COLUNAS_ITEM_VIAGEM;
import static br.com.actusrota.tabela.TabelaItemViagem.FK_PRODUTO;
import static br.com.actusrota.tabela.TabelaItemViagem.FK_VIAGEM;
import static br.com.actusrota.tabela.TabelaItemViagem.ID;
import static br.com.actusrota.tabela.TabelaItemViagem.QUANTIDADE;
import static br.com.actusrota.tabela.TabelaItemViagem.TABELA_ITEM_VIAGEM;
import static br.com.actusrota.tabela.TabelaItemViagem.TOTAL_UNITARIO;
import static br.com.actusrota.tabela.TabelaItemViagem.VALOR_UNITARIO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.Item;
import br.com.actusrota.entidade.ItemDevolucao;
import br.com.actusrota.entidade.ItemViagem;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaItemDevolucao;

public class ItemViagemDAO extends GenericoDAO<ItemViagem> {

	private ProdutoDAO produtoDAO;
	private ViagemDAO viagemDAO;
	private ItemDevolucaoDAO itemDevolucaoDAO;

	public ItemViagemDAO(Context context) {
		super(context, TABELA_ITEM_VIAGEM);
		produtoDAO = new ProdutoDAO(context);
		viagemDAO = new ViagemDAO(context);
		itemDevolucaoDAO = new ItemDevolucaoDAO(context);
	}

	public List<ItemViagem> getItens(Viagem viagem) {
		Cursor c = null;
		List<ItemViagem> itens = new ArrayList<ItemViagem>();
		try {
			abrirBancoSomenteLeitura();
			c = listarTodos(COLUNAS_ITEM_VIAGEM);

			while (c.moveToNext()) {
				ItemViagem a = montarEntidade(c, viagem);
				itens.add(a);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return itens;
	}
	
	public List<ItemViagem> buscarItensAcerto(Viagem viagem) {
		Cursor c = null;
		List<ItemViagem> itens = new ArrayList<ItemViagem>();
		try {
			abrirBancoSomenteLeitura();
			
			StringBuilder sql = new StringBuilder(
					" select iv.id, iv.valor_unitario, iv.total_unitario, iv.quantidade, iv.fk_produto, iv.fk_viagem ");
			sql.append(" from ");
			sql.append(TABELA_ITEM_VIAGEM);
			sql.append(" iv ");
			sql.append(" where iv.fk_viagem = ");
			
			sql.append(viagem.getId());
			
			sql.append(" union ");
			
			sql.append(" select idv.id, idv.valor_unitario, idv.total_unitario, idv.quantidade, idv.fk_produto, idv.fk_viagem ");
			sql.append(" from ");
			sql.append(TabelaItemDevolucao.TABELA_ITEM_DEVOLUCAO);
			sql.append(" idv ");
			sql.append(" where idv.fk_viagem = ");
			
			sql.append(String.valueOf(viagem.getId()));
			
			c = listarJoin(sql.toString());

			while (c.moveToNext()) {
				ItemViagem a = montarEntidade(c, viagem);
				itens.add(a);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return itens;
	}

	@SuppressWarnings("unchecked")
	public <E extends Item> List<E> getItensViagemDevolucao(Viagem viagem) {
		List<E> itensViagem = (List<E>) getItens(viagem);
		List<E> itensDevolucao = (List<E>) itemDevolucaoDAO
				.buscarItens(viagem);
		
		if (itensDevolucao != null && !itensDevolucao.isEmpty()) {
			itensViagem.addAll(itensDevolucao);
		}
		return itensViagem;
	}

	@Override
	public ContentValues criarContentValues(ItemViagem item) {
		ContentValues values = new ContentValues();
		values.put(ID, item.getId());
		values.put(VALOR_UNITARIO, item.getValorUnitario().getValor());
		values.put(TOTAL_UNITARIO, item.getTotalUnitario().getValor());
		values.put(QUANTIDADE, item.getQuantidade());
		values.put(FK_PRODUTO, item.getProduto().getId());
		values.put(FK_VIAGEM, item.getViagem().getId());
		return values;
	}

	@Override
	public ItemViagem consultarPorId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	// { ID, VALOR_UNITARIO,
	// TOTAL_UNITARIO, QUANTIDADE, FK_PRODUTO, FK_VIAGEM };
	@Override
	public ItemViagem montarEntidade(Cursor cs) {
		ItemViagem item = new ItemViagem();
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		item.setProduto(produtoDAO.consultarPorId(cs.getLong(4)));
		item.setViagem(viagemDAO.consultarPorId(cs.getLong(5)));
		return item;
	}

	public ItemViagem montarEntidade(Cursor cs, Viagem viagem) {
		ItemViagem item = new ItemViagem();
		item.setId(cs.getLong(0));
		item.setValorUnitario(Dinheiro.valueOf(cs.getLong(1)));
		item.setTotalUnitario(Dinheiro.valueOf(cs.getLong(2)));
		item.setQuantidade(cs.getInt(3));
		Produto produto = produtoDAO.consultarPorId(cs.getLong(4));
		if(produto.getId() == 12 || produto.getId() == 13) {
			System.out.println(produto);
		}
		item.setProduto(produto);
		// item.setViagem(viagemDAO.consultarPorId(cs.getLong(5)));
		item.setViagem(viagem);
		return item;
	}

	// { ID, VALOR_UNITARIO,
	// TOTAL_UNITARIO, QUANTIDADE, FK_PRODUTO, FK_VIAGEM };
	public ItemViagem pesquisarPor(Produto produto) {
		Cursor cs = null;
		ItemViagem item = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(
					" select id, valor_unitario, total_unitario, quantidade, fk_produto, fk_viagem ");
			sql.append(" from ");
			sql.append(TABELA_ITEM_VIAGEM);
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

}
