package br.com.actusrota.tabela;

public class TabelaTelefone {
	public static final String TABELA_TELEFONE = "telefone";
	public static final String ID = "id";
	public static final String DDD = "ddd";
	public static final String RAMAL = "ramal";
	public static final String NUMERO = "numero";
	public static final String TIPO_TELEFONE = "tipo_telefone";
	public static final String OBSERVACAO = "observacao";
	public static final String CLIENTE = "fk_cliente";
	public static final String[] COLUNAS_TELEFONE = { ID, DDD, RAMAL, NUMERO,
			TIPO_TELEFONE, OBSERVACAO, CLIENTE };
	public static final String CREATE_TABLE_TELEFONE;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_TELEFONE);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" ddd CHAR(2),");
		sql.append(" ramal TEXT,");
		sql.append(" numero TEXT, ");
		sql.append(" tipo_telefone INTEGER, ");
		sql.append(" observacao TEXT, ");
		sql.append(CLIENTE);
		sql.append(" INTEGER,");
		sql.append(" FOREIGN KEY(fk_cliente) REFERENCES cliente (id)); ");
		CREATE_TABLE_TELEFONE = sql.toString();
	}
}
