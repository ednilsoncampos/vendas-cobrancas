package br.com.actusrota.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.IEntidade;
import br.com.actusrota.tabela.TabTabelaDePreco;
import br.com.actusrota.tabela.TabelaAcertoCliente;
import br.com.actusrota.tabela.TabelaCidade;
import br.com.actusrota.tabela.TabelaCliente;
import br.com.actusrota.tabela.TabelaConexaoBancoWeb;
import br.com.actusrota.tabela.TabelaEndereco;
import br.com.actusrota.tabela.TabelaFuncionario;
import br.com.actusrota.tabela.TabelaItemBrinde;
import br.com.actusrota.tabela.TabelaItemBrindeExtra;
import br.com.actusrota.tabela.TabelaItemDevolucao;
import br.com.actusrota.tabela.TabelaItemTabPreco;
import br.com.actusrota.tabela.TabelaItemTroca;
import br.com.actusrota.tabela.TabelaItemVenda;
import br.com.actusrota.tabela.TabelaItemViagem;
import br.com.actusrota.tabela.TabelaPagamentoCheque;
import br.com.actusrota.tabela.TabelaPagamentoEspecie;
import br.com.actusrota.tabela.TabelaPessoa;
import br.com.actusrota.tabela.TabelaProduto;
import br.com.actusrota.tabela.TabelaRota;
import br.com.actusrota.tabela.TabelaTelefone;
import br.com.actusrota.tabela.TabelaVenda;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.UtilMensagem;

public abstract class GenericoDAO<T extends IEntidade> implements IDAO<T> {
	// data/data/br.com.actusrota/databases/actus

	private DBAdapter dbAdapter;
	private final String tabela;
	private static String[] TODAS_TABELAS;
	protected boolean bancoNaoCompartilhado = true;
	protected final Context contexto;

	/**
	 * Utilizado para compartilhar o DBAdapter se for criadao usando DBAdapter,
	 * quer dizer que o banco será gerenciado pelo DAO principal. Ex. ViagemDAO
	 * gerencia o banco de TabelaDePrecoDAO
	 * 
	 * @param dbAdapter
	 * @param tabela
	 */
	public GenericoDAO(Context contexto, DBAdapter dbAdapter, String tabela) {
		this.contexto = contexto;
		this.dbAdapter = dbAdapter;
		this.tabela = tabela;
		bancoNaoCompartilhado = false;
	}

	public GenericoDAO(Context context, String tabela) {
		this.tabela = tabela;
		TODAS_TABELAS = new String[23];

		// mailbox_id INTEGER REFERENCES mailboxes ON DELETE CASCADE
		// db.execSQL("PRAGMA foreign_keys=ON;"); // chamar no open
		TODAS_TABELAS[0] = TabelaProduto.CREATE_TABLE_PRODUTO;
		TODAS_TABELAS[1] = TabelaRota.CREATE_TABLE_ROTA;
		TODAS_TABELAS[2] = TabelaCidade.CREATE_TABLE_CIDADE;
		TODAS_TABELAS[3] = TabelaEndereco.CREATE_TABLE_ENDERECO;
		TODAS_TABELAS[4] = TabelaPessoa.CREATE_TABLE_PESSOA;
		TODAS_TABELAS[5] = TabelaFuncionario.CREATE_TABLE_FUNCIONARIO;
		TODAS_TABELAS[6] = TabelaCliente.CREATE_TABLE_CLIENTE;
		TODAS_TABELAS[7] = TabTabelaDePreco.CREATE_TABLE_TABELA_PRECO;
		TODAS_TABELAS[8] = TabelaItemTabPreco.CREATE_TABLE_ITEM_TAB_PRECO;
		TODAS_TABELAS[9] = TabelaViagem.CREATE_TABLE_VIAGEM;
		TODAS_TABELAS[10] = TabelaItemViagem.CREATE_TABLE_ITEM_VIAGEM;
		TODAS_TABELAS[11] = TabelaVenda.CREATE_TABLE_VENDA;
		TODAS_TABELAS[12] = TabelaItemVenda.CREATE_TABLE_ITEM_VENDA;
		TODAS_TABELAS[13] = TabelaItemDevolucao.CREATE_TABLE_ITEM_DEV;
		TODAS_TABELAS[14] = TabelaItemBrinde.CREATE_TABLE_ITEM_BRINDE;
		TODAS_TABELAS[15] = TabelaItemBrindeExtra.CREATE_TABLE_ITEM_BRINDE_EXTRA;
		TODAS_TABELAS[16] = TabelaItemTroca.CREATE_TABLE_ITEM_TROCA;

		TODAS_TABELAS[17] = TabelaPagamentoCheque.CREATE_TABLE_PAG_CHEQUE;
		TODAS_TABELAS[18] = TabelaPagamentoEspecie.CREATE_TABLE_PAG_ESPECIE;
		TODAS_TABELAS[19] = TabelaTelefone.CREATE_TABLE_TELEFONE;
		TODAS_TABELAS[20] = TabelaConexaoBancoWeb.CREATE_TABLE_CONEXAO;
		TODAS_TABELAS[21] = TabelaAcertoCliente.CREATE_TABLE_ACERTO_CLIENTE;
		TODAS_TABELAS[22] = TabelaCidade.CREATE_TABLE_CIDADE_ROTA;

		this.contexto = context;

		dbAdapter = new DBAdapter(context, TODAS_TABELAS);
	}

	// public GenericoDAO(Context context, String tabela, String... createTable)
	// {
	// this.tabela = tabela;
	// dbAdapter = new DBAdapter(context, createTable);
	// }

	@Override
	public void adicionar(Collection<T> dados) {
		List<ContentValues> listaInserir = new ArrayList<ContentValues>();
		List<T> listaAtualizar = new ArrayList<T>(0);
		for (T entidade : dados) {
			if (entidade.isNovo()) {
				try {
					ContentValues values = criarContentValues(entidade);
					listaInserir.add(values);
				} catch (Exception e) {
					System.err.println("Não pode ser inserido:" + entidade);
					throw new RuntimeException(e);
				}
			} else {
				listaAtualizar.add(entidade);
			}
		}
		if (!listaAtualizar.isEmpty())
			atulizar(listaAtualizar);
		dbAdapter.adicionar(tabela, listaInserir);
	}

	@Override
	public void adicionarImportacao(Collection<T> dados) {
		List<ContentValues> listaInserir = new ArrayList<ContentValues>();
		for (T entidade : dados) {
			ContentValues values = criarContentValues(entidade);
			listaInserir.add(values);
		}
		dbAdapter.adicionar(tabela, listaInserir);
	}

	@Override
	public void adicionarImportacao(T entidade) {
		ContentValues values = criarContentValues(entidade);
		try {
			abrirBanco();
			iniciarTransacao();
			dbAdapter.adicionar(tabela, values);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	// @Override
	// public void adicionarImportacao(T dados) {
	// List<ContentValues> listaInserir = new ArrayList<ContentValues>();
	// for (T entidade : dados) {
	// ContentValues values = criarContentValues(entidade);
	// listaInserir.add(values);
	// }
	// dbAdapter.adicionar(tabela, listaInserir);
	// }

	public boolean atulizar(List<T> listaAtualizar) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();

			for (T entidade : listaAtualizar) {
				atualizarSemComitar(entidade.getId(), entidade);
			}

			comitarTransacao();
			resposta = true;
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}

	public void adicionar(T entidade) {
		ContentValues values = criarContentValues(entidade);
		try {
			abrirBanco();
			iniciarTransacao();
			dbAdapter.adicionar(tabela, values);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}
	
	public void adicionarSemComitar(String tabela, T entidade) {
		ContentValues values = criarContentValues(entidade);
		dbAdapter.adicionar(tabela, values);
	}

	/**
	 * Nï¿½o abre o banco, nï¿½o faz comit e nï¿½o encerra a transaï¿½ï¿½o
	 * 
	 * @param entidade
	 */
	public void adicionarSemComitar(T entidade) {
		ContentValues values = criarContentValues(entidade);
		dbAdapter.adicionar(tabela, values);
	}

	public void adicionarSemComitar(Collection<T> dados) {
		List<ContentValues> listValues = new ArrayList<ContentValues>();
		for (T entidade : dados) {
			ContentValues values = criarContentValues(entidade);
			listValues.add(values);
		}
		dbAdapter.adicionarSemComitar(tabela, listValues);
	}

	public boolean atualizar(long id, T entidade) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.atualizar(tabela, id,
					criarContentValues(entidade));
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}
	
	public boolean atualizarSemComitar(String tabela, long id, ContentValues value) {
		return dbAdapter.atualizar(tabela, id, value);
	}

	/**
	 * Nï¿½o abre nem fecha o banco, nï¿½o faz comit e nï¿½o encerra a
	 * transaï¿½ï¿½o
	 * 
	 * @param entidade
	 */
	public boolean atualizarSemComitar(long id, T entidade) {
		return dbAdapter.atualizar(tabela, id, criarContentValues(entidade));
	}

	public boolean delete(String tabela, long id) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.delete(tabela, id);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}

	public boolean delete(long id) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.delete(tabela, id);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}

	public boolean deleteSemComitar(String tabela, long id) {
		return dbAdapter.delete(tabela, id);
	}
	
	public boolean deleteSemComitar(long id) {
		return dbAdapter.delete(tabela, id);
	}

	public boolean delete(String tabela, String atributo, Object valor) {
		boolean resposta = false;
		try {
			String sql = atributo + " = " + valor;

			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.deleteComParametro(tabela, sql);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}
	
	public boolean delete(String tabela, String sql) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.deleteComParametro(tabela, sql);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}
	
	public boolean delete(String sql) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			dbAdapter.delete(sql);
			comitarTransacao();
			resposta = true;
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}
	
	public void deleteAdapter(String sql) {
		dbAdapter.delete(sql);
	}

	public boolean deleteTodos() {
		return deleteTodos(tabela);
	}

	public boolean deleteTodos(String tabela) {
		boolean resposta = false;
		try {
			abrirBanco();
			iniciarTransacao();
			resposta = dbAdapter.deleteTodos(tabela);
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
		return resposta;
	}

	/**
	 * Funciona
	 * 
	 * @return
	 */
	public Long buscarMaxID(String tabela) {
		Cursor c = null;
		Long maxID = 0l;
		try {
			abrirBancoSomenteLeitura();
			c = dbAdapter.buscarMaxID(tabela);
			if (c.moveToFirst())
				maxID = c.getLong(0);
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}
		return maxID;
	}

	/**
	 * Funciona
	 * 
	 * @return
	 */
	public Long buscarMaxID() {
		return buscarMaxID(tabela);
	}

	public Long buscarIdAtual(String tabela) {
		Cursor c = null;
		Long idAtual = 0l;
		try {
			// abrirBancoSomenteLeitura();
			c = dbAdapter.buscarIdAtual(tabela);
			if (c.moveToFirst())
				idAtual = c.getLong(0);
		} finally {
			if (c != null)
				c.close();
			// fecharBanco();
		}
		return idAtual;
	}

	public Long buscarIdAtual() {
		return buscarIdAtual(tabela);
	}

	public Long buscarUltimoIdInserido() {
		Cursor c = null;
		Long idAtual = 0l;
		try {
			// abrirBanco();
			c = dbAdapter.buscarUltimoIdInserido();
			if (c.moveToFirst())
				idAtual = c.getLong(0);
		} finally {
			if (c != null)
				c.close();
			// fecharBanco();
		}
		return idAtual;
	}

	/**
	 * Quando o retorno for um Cursor o banco e o cursor devem continuar abertos
	 * 
	 * @param colunas
	 * @return
	 */
	public Cursor listarTodos(String[] colunas) {
		return dbAdapter.listarTodos(tabela, colunas);
	}

	public Cursor listar(String[] colunas, String atributo, String parametro) {
		return dbAdapter.listar(tabela, colunas, atributo, parametro);
	}

	public Cursor listar(String[] colunas, String[] atributos,
			String... parametros) {
		return dbAdapter.listar(tabela, colunas, atributos, parametros);
	}

	public Cursor listar(String tabela, String[] colunas, String atributo,
			String parametro) {
		return dbAdapter.listar(tabela, colunas, atributo, parametro);
	}

	public Cursor listarJoin(String sql, String parametro) {
		return dbAdapter.listarJoin(sql, parametro);
	}

	public Cursor listarJoin(String sql, String... parametro) {
		return dbAdapter.listarJoin(sql, parametro);
	}

	public Cursor pesquisarLike(String[] colunas, String atributo,
			String parametro) {
		return dbAdapter.pesquisarLike(tabela, colunas, atributo, parametro);
	}

	public Cursor consultarPorId(String[] colunas, long id) throws SQLException {
		return dbAdapter.consultarPorId(tabela, colunas, id);
	}
	
	public Cursor consultar(String tabela, String[] colunas, String coluna,
			Object valor) throws SQLException {
		return dbAdapter.consultar(tabela, colunas, coluna, valor);
	}
	
	public Cursor consultar(String[] colunas, String coluna,
			Object valor) throws SQLException {
		return dbAdapter.consultar(tabela, colunas, coluna, valor);
	}

	// public Cursor consultarPorId(String tabela,String[] colunas, long id)
	// throws SQLException {
	// return dbAdapter.consultarPorId(tabela, colunas, id);
	// }

	public void abrir() {
		dbAdapter.abrirBanco();
	}
	
	public void fechar() {
		dbAdapter.fecharBanco();
	}
	
	public void abrirBanco() {
		if (bancoNaoCompartilhado)
			dbAdapter.abrirBanco();
	}
	
	public void fecharBanco() {
		if (bancoNaoCompartilhado) {
			dbAdapter.fecharBanco();
		}
	}

	public void abrirBancoSomenteLeitura() {
		if (bancoNaoCompartilhado) {
			dbAdapter.abrirBancoSomenteLeitura();
		}
	}


	public boolean isAberto() {
		UtilMensagem.mostarMensagemLonga(contexto, "DB cheque..." + dbAdapter);
		boolean aberto = dbAdapter.isAberto();
		return aberto;
	}

	public DBAdapter getDbAdapter() {
		return dbAdapter;
	}

	public void iniciarTransacao() {
		dbAdapter.iniciarTransacao();
	}

	public void comitarTransacao() {
		dbAdapter.comitarTransacao();
	}

	/**
	 * Realiza o commit caso seja feita uma chamada a setTransactionSuccessful
	 * Realiza rollback se nï¿½o chamar setTransactionSuccessful
	 */
	public void fecharTransacao() {
		dbAdapter.fecharTransacao();// roolBack????
	}

	public boolean isCadastrado(long id) throws SQLException {
		boolean cadastrado = dbAdapter.isCadastrado(tabela, id);
		return cadastrado;
	}
	
	public boolean isCadastrado(String atributo, Object valor)
			throws SQLException {
		return dbAdapter.isCadastrado(tabela, atributo, valor);
	}
	
	public boolean isCadastrado(String tabela, String atributo, Object valor)
			throws SQLException {
		return dbAdapter.isCadastrado(tabela, atributo, valor);
	}

	public boolean isBancoNaoCompartilhado() {
		return bancoNaoCompartilhado;
	}

	public abstract ContentValues criarContentValues(T entidade);

	public abstract T montarEntidade(Cursor c);

	public abstract T consultarPorId(long id);

	public SQLiteOpenHelper getdBHelper() {
		return dbAdapter.getdBHelper();
	}

	public SQLiteDatabase getDb() {
		return dbAdapter.getDb();
	}
}
