package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaConexaoBancoWeb.COLUNAS_CONEXAO;
import static br.com.actusrota.tabela.TabelaConexaoBancoWeb.ID;
import static br.com.actusrota.tabela.TabelaConexaoBancoWeb.USUARIO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.ConexaoBanco;
import br.com.actusrota.tabela.TabelaConexaoBancoWeb;

public class ConexaoBancoWebDAO extends GenericoDAO<ConexaoBanco> {

	public ConexaoBancoWebDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaConexaoBancoWeb.TABELA_CONEXAO);
	}

	public ConexaoBancoWebDAO(Context context) {
		super(context, TabelaConexaoBancoWeb.TABELA_CONEXAO);
	}

	public ConexaoBanco consultarPor(String usuario) {
		Cursor cs = null;
		ConexaoBanco cidade = null;
		try {
			abrirBancoSomenteLeitura();
			cs = listar(COLUNAS_CONEXAO, USUARIO, usuario);
			if (cs.moveToFirst())
				cidade = montarEntidade(cs);
		} finally {
			cs.close();
			fecharBanco();
		}
		return cidade;
	}

	public ConexaoBanco buscarUsuarioConexaoCadastrado() {
		try {
			abrirBancoSomenteLeitura();
			Long id = buscarMaxID();
			return consultarPorId(id);
		} finally {
			fecharBanco();
		}
	}

	/**
	 * Banco já está aberto para o DAO cidade, o DAO da rota deve abrir também
	 */
	public ConexaoBanco montarEntidade(Cursor cs) {
		ConexaoBanco cidade = new ConexaoBanco();
		cidade.setId(cs.getLong(0));
		cidade.setUsuarioConexao(cs.getString(1));
		return cidade;
	}

	@Override
	public ContentValues criarContentValues(ConexaoBanco cidade) {
		ContentValues values = new ContentValues();
		values.put(ID, cidade.getId());
		values.put(USUARIO, cidade.getUsuarioConexao());
		return values;
	}

	@Override
	public ConexaoBanco consultarPorId(long id) {
		Cursor cs = null;
		ConexaoBanco cidade = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_CONEXAO, id);
			if (cs.moveToFirst())
				cidade = montarEntidade(cs);
		} finally {
			cs.close();
			fecharBanco();
		}
		return cidade;
	}

}
