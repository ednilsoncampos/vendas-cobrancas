package br.com.actusrota.tabela;

public class TabelaCidade {
	public static final String TABELA_CIDADE = "cidade";
	public static final String ID = "id";
	public static final String NOME = "nome";
	public static final String SIGLA_UF = "sigla_uf";
//	public static final String ROTA = "fk_rota";

	public static final String[] COLUNAS_CIDADE = { ID, NOME, SIGLA_UF };
	public static String CREATE_TABLE_CIDADE;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_CIDADE);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		
		sql.append(NOME);
		sql.append(" TEXT,");
		
		sql.append(SIGLA_UF);
		sql.append(" TEXT);");
		
//		sql.append(ROTA);
//		sql.append(" INTGER);");
		
		// sql.append(" FOREIGN KEY(fk_rota) REFERENCES rota (id)); ");
		CREATE_TABLE_CIDADE = sql.toString();
	}
	/**
	 * Tabela cidade rota
	 */
	public static final String TABELA_CIDADE_ROTA = "cidade_rota";
	public static final String ROTA_CIDADE = "fk_rota";
	public static final String CIDADE = "fk_cidade";
	public static String CREATE_TABLE_CIDADE_ROTA;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_CIDADE_ROTA);
		sql.append("(");
		sql.append(CIDADE);
		sql.append(" INTEGER,");
		sql.append(ROTA_CIDADE);
		sql.append(" INTEGER);");
//		sql.append(" FOREIGN KEY(fk_cidade) REFERENCES cidade (id), ");
//		sql.append(" FOREIGN KEY(fk_rota) REFERENCES rota (id)); ");
		CREATE_TABLE_CIDADE_ROTA = sql.toString();
	}

}
