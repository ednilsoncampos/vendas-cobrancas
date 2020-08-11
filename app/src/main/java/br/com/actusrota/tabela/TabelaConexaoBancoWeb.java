package br.com.actusrota.tabela;

public class TabelaConexaoBancoWeb {
	
	public static final String TABELA_CONEXAO = "conexao";
	public static final String ID = "id";
	public static final String USUARIO = "usuario_conexao";
	
	public static final String[] COLUNAS_CONEXAO = { ID, USUARIO };
	
	public static String CREATE_TABLE_CONEXAO;
	
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_CONEXAO);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		sql.append(USUARIO);
		sql.append(" TEXT,");
		sql.append("UNIQUE ("+USUARIO+") ON CONFLICT REPLACE);");
		CREATE_TABLE_CONEXAO = sql.toString();
	}
}
