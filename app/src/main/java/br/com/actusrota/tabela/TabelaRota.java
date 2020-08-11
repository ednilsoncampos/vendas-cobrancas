package br.com.actusrota.tabela;

public class TabelaRota {
	public static final String TABELA_ROTA = "rota";
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";
//	public static final String RESPONSAVEL = "responsavel";
	public static final String FK_FUNCIONARIO = "funcionario";
//	public static final String[] COLUNAS_ROTA = { ID, DESCRICAO, RESPONSAVEL };
	public static final String[] COLUNAS_ROTA = { ID, DESCRICAO };
	public static final String CREATE_TABLE_ROTA;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ROTA);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" descricao TEXT);");
		
//		sql.append(" responsavel INTEGER,");
//		sql.append(" FOREIGN KEY(responsavel) REFERENCES "+FK_FUNCIONARIO+"(id)); ");
		
		CREATE_TABLE_ROTA = sql.toString();
	}
}
