package br.com.actusrota.tabela;

public class TabelaProduto {
	public static final String TABELA_PRODUTO = "produto";
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";
	public static final String CODIGO = "codigo";
	public static final String ID_GRUPO = "id_grupo";
	public static final String[] COLUNAS_PRODUTO = { ID, DESCRICAO, CODIGO, ID_GRUPO };
	public static final String CREATE_TABLE_PRODUTO;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_PRODUTO);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" descricao TEXT,");
		sql.append(" id_grupo INTEGER,");
		sql.append(" codigo TEXT UNIQUE NOT NULL);");
		//CRIANDO INDICE
		sql.append("CREATE INDEX descricao_index ON ");
		sql.append(TABELA_PRODUTO);
		sql.append(" (");
		sql.append(DESCRICAO);
		sql.append(");");
		CREATE_TABLE_PRODUTO = sql.toString();
	}

}
