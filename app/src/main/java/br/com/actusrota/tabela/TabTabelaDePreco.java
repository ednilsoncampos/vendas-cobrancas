package br.com.actusrota.tabela;

public class TabTabelaDePreco {
	public static final String TABELA_PRECO = "tabela_de_preco";
	public static final String ID = "id";
//	public static final String DATA_CADASTRO = "data_cadastro";
	public static final String ROTA = "fk_rota";

	public static final String[] COLUNAS_TABELA_PRECO = { ID, ROTA };

	public static String CREATE_TABLE_TABELA_PRECO;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_PRECO);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
//		sql.append(DATA_CADASTRO);
//		sql.append(" DATE,");
		sql.append(ROTA);
		sql.append(" INTEGER,");
		sql.append(" FOREIGN KEY (fk_rota) REFERENCES rota (id));");
		CREATE_TABLE_TABELA_PRECO = sql.toString();
	}
}
