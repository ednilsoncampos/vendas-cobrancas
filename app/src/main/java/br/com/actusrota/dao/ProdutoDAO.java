package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaProduto.CODIGO;
import static br.com.actusrota.tabela.TabelaProduto.COLUNAS_PRODUTO;
import static br.com.actusrota.tabela.TabelaProduto.DESCRICAO;
import static br.com.actusrota.tabela.TabelaProduto.ID;
import static br.com.actusrota.tabela.TabelaProduto.ID_GRUPO;
import static br.com.actusrota.tabela.TabelaProduto.TABELA_PRODUTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Produto;

public class ProdutoDAO extends GenericoDAO<Produto> {

	public ProdutoDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_PRODUTO);
	}

	public ProdutoDAO(Context context) {
		super(context, TABELA_PRODUTO);
	}

	@Override
	public ContentValues criarContentValues(Produto produto) {
		ContentValues values = new ContentValues();
		values.put(ID, produto.getId());
		values.put(DESCRICAO, produto.getDescricao());
		values.put(CODIGO, produto.getCodigo());
		values.put(ID_GRUPO, produto.getIdGrupo());
		return values;
	}

	public List<Produto> getProdutos() {
		List<Produto> produtos = new ArrayList<Produto>(0);
		Cursor c = null;
		try {
			abrirBancoSomenteLeitura();
			c = listarTodos(COLUNAS_PRODUTO);

			while (c.moveToNext()) {
				Produto produto = montarEntidade(c);
				produtos.add(produto);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return produtos;
	}

	public List<Produto> pesquisarLike(String parametro) {
		List<Produto> produtos = new ArrayList<Produto>();
		Cursor c = null;
		try {
			abrirBancoSomenteLeitura();
			c = pesquisarLike(COLUNAS_PRODUTO, DESCRICAO, parametro);

			while (c.moveToNext()) {
				Produto p = montarEntidade(c);
				produtos.add(p);
			}
		} finally {
			c.close();
			fecharBanco();
		}

		return produtos;
	}

	public Produto pesquisarPorCodigo(String codigo) {
		Produto produto = null;
		Cursor c = null;
		try {
			abrirBancoSomenteLeitura();
			c = listar(COLUNAS_PRODUTO, CODIGO, codigo);
			if (c.moveToFirst())
				produto = montarEntidade(c);
		} finally {
			c.close();
			fecharBanco();
		}
		return produto;
	}

	public Produto montarEntidade(Cursor c) {
		Produto produto = new Produto();
		produto.setId(c.getLong(0));
		produto.setDescricao(c.getString(1));
		produto.setCodigo(c.getString(2));
		produto.setIdGrupo(c.getInt(3));
		return produto;
	}

	@Override
	public Produto consultarPorId(long id) {
		Produto produto = null;
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_PRODUTO, id);
			if (cs.moveToFirst())
				produto = montarEntidade(cs);
		} finally {
			cs.close();
			fecharBanco();
		}
		return produto;
	}

	@Override
	public void adicionarImportacao(Collection<Produto> dados) {
		deleteTodos();
		super.adicionarImportacao(dados);
	}
}
