package br.com.actusrota;

import java.util.List;

import br.com.actusrota.util.UtilMensagem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "actus";
	private static final int DATABASE_VERSION = 1;

	private final Context context;

	private DatabaseHelper dBHelper;
	private SQLiteDatabase db;

	private static final String MAX_ID = "SELECT MAX(id) AS max_id FROM ";

	public DBAdapter(Context ctx, String... createTable) {
		this.context = ctx;
		dBHelper = new DatabaseHelper(context, createTable);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private String[] createTable;

		DatabaseHelper(Context context, String... createTable) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.createTable = createTable;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			int aux = 0;
			try {
				for (int i = 0; i < createTable.length; i++) {
					aux = i;
					db.execSQL(createTable[i]);
				}
			} catch (Exception e) {
				Log.e("Erro sql", createTable[aux]);
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Atualizando banco de dados da versão " + oldVersion
					+ " para " + newVersion
					+ ", que apagará todos os registros anteriores.");
			// db.execSQL("DROP TABLE IF EXISTS "+createTable);

			for (int i = 0; i < createTable.length; i++) {
				db.execSQL("DROP TABLE IF EXISTS " + createTable[i]);
			}
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter abrirBanco() throws SQLException {
		db = dBHelper.getWritableDatabase();
		return this;
	}

	public DBAdapter abrirBancoSomenteLeitura() throws SQLException {
		db = dBHelper.getReadableDatabase();
		return this;
	}

	public boolean isAberto() {
		// if (db == null) {
		// db = dBHelper.getReadableDatabase();
		// }
		UtilMensagem.mostarMensagemLonga(context, "aberto????");
		boolean aberto = (db != null && db.isOpen());
		UtilMensagem.mostarMensagemLonga(context, aberto ? "sim" : "não");
		return aberto;
	}

	public DatabaseHelper getdBHelper() {
		return dBHelper;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	// ---closes the database---
	public void fecharBanco() {
		// if (db == null) {
		// return;
		// }

		// if (isAberto())
		dBHelper.close();
	}

	public void iniciarTransacao() {
		db.beginTransaction();
	}

	public void comitarTransacao() {
		db.setTransactionSuccessful();
	}

	public void fecharTransacao() {
		db.endTransaction();
	}

	/**
	 * Gerencia a transação
	 * 
	 * @param tabela
	 * @param dados
	 */
	public void adicionar(String tabela, List<ContentValues> dados) {
		try {
			abrirBanco();
			iniciarTransacao();
			for (ContentValues produto : dados) {
				adicionar(tabela, produto);
			}
			comitarTransacao();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public void adicionarSemComitar(String tabela, ContentValues value) {
		adicionar(tabela, value);
	}

	public void adicionarSemComitar(String tabela, List<ContentValues> produtos) {
		for (ContentValues produto : produtos) {
			adicionar(tabela, produto);
		}
	}

	/**
	 * A classe que utilizar dever abrir e fechar o banco
	 * 
	 * @param tabela
	 * @param values
	 */
	public void adicionar(String tabela, ContentValues values) {
		db.insert(tabela, null, values);
		// try {
		// db.insert(tabela, null, values);
		// } catch (SQLiteConstraintException e) {
		// String msg = "Registro já adicionado na tabela:"+tabela+". "
		// + e.getMessage();
		// Log.w("Erro!", msg);
		// }
	}

	public boolean atualizar(String tabela, long id, ContentValues values) {
		return db.update(tabela, values, " id = " + id, null) > 0;
	}

	public boolean deleteTodos(String tabela) {
		return db.delete(tabela, null, null) > 0;
	}

	public boolean delete(String tabela, long id) {
		return db.delete(tabela, " id =" + id, null) > 0;
	}

	public boolean deleteComParametro(String tabela, String parametro) {
		return db.delete(tabela, parametro, null) > 0;
	}

	public void delete(String sql) {
		db.execSQL(sql);
	}

	public Cursor buscarMaxID(String tabela) {
		return db.rawQuery(MAX_ID + tabela, null);
	}

	public Cursor buscarIdAtual(String tabela) {
		String query = "SELECT seq FROM sqlite_sequence WHERE name = " + tabela;
		return db.rawQuery(query, null);
	}

	public Cursor buscarUltimoIdInserido() {
		String query = "select last_insert_rowid();";
		return db.rawQuery(query, null);
	}

	public Cursor listarTodos(String tabela, String[] colunas) {
		return db.query(tabela, colunas, null, null, null, null, null);
	}

	public Cursor listar(String tabela, String[] colunas, String atributo,
			String parametro) {
		String sql = atributo + " = ? ";
		return db.query(tabela, colunas, sql, new String[] { parametro }, null,
				null, null, null);
	}

	public Cursor listar(String tabela, String[] colunas, String[] atributos,
			String[] parametros) {
		StringBuilder sql = new StringBuilder();
		sql.append(atributos[0] + " = ? ");
		if (atributos.length > 1) {
			for (int i = 1; i < atributos.length; i++) {
				sql.append(" AND " + atributos[i] + " = ? ");
			}
		}
		return db.query(tabela, colunas, sql.toString(), parametros, null,
				null, null, null);
	}

	public Cursor listarJoin(String sql, String parametro) {
		return db.rawQuery(sql, new String[] { parametro });
	}

	public Cursor listarJoin(String sql, String... parametro) {
		return db.rawQuery(sql, parametro);
	}

	public Cursor consultarPorId(String tabela, String[] colunas, long id)
			throws SQLException {
		String sql = " id = ? ";
		Cursor cs = db.query(tabela, colunas, sql,
				new String[] { String.valueOf(id) }, null, null, null, null);
		return cs;
	}

	public Cursor consultar(String tabela, String[] colunas, String coluna,
			Object valor) throws SQLException {
		String sql = coluna + " = ? ";
		Cursor cs = db.query(tabela, colunas, sql,
				new String[] { String.valueOf(valor) }, null, null, null, null);
		return cs;
	}

	public boolean isCadastrado(String tabela, long id) throws SQLException {
		boolean cadastrado = false;
		Cursor cs = null;
		try {
			String sql = " id = ? ";
			cs = db.query(tabela, null, sql,
					new String[] { String.valueOf(id) }, null, null, null, null);
			cadastrado = cs.moveToFirst();
		} finally {
			if (cs != null)
				cs.close();
		}
		return cadastrado;
	}

	public boolean isCadastrado(String tabela, String atributo, Object valor)
			throws SQLException {
		boolean cadastrado = false;
		Cursor cs = null;
		try {
			String sql = atributo + "  = ? ";
			cs = db.query(tabela, null, sql,
					new String[] { String.valueOf(valor) }, null, null, null,
					null);
			cadastrado = cs.moveToFirst();
		} finally {
			if (cs != null)
				cs.close();
		}
		return cadastrado;
	}

	public Cursor pesquisarLike(String tabela, String[] colunas,
			String atributo, String parametro) throws SQLException {
		String sql = "UPPER(" + atributo + ") like UPPER(?) ";
		Cursor c = db.query(tabela, colunas, sql, new String[] { "%"
				+ parametro.toUpperCase() + "%" }, null, null, null, null);
		return c;
	}

}
