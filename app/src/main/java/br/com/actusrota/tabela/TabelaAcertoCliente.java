package br.com.actusrota.tabela;

public class TabelaAcertoCliente {
	
	public static final String TABELA_ACERTO_CLIENTE = "acerto_cliente";
	public static final String ID = "id";
	public static final String LIMITE_PERCENTUAL = "limite_percentual";
	public static final String MARGEM_PERCENTUAL = "margem_percentual";
	public static final String OPERACAO = "operacao";
	public static final String SELECT = " select c.id, c.limite_percentual, c.margem_percentual, c.operacao ";
	public static final String[] COLUNAS_ACERTO_CLIENTE = { ID, LIMITE_PERCENTUAL, MARGEM_PERCENTUAL, OPERACAO };
	public static final String CREATE_TABLE_ACERTO_CLIENTE;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ACERTO_CLIENTE);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" limite_percentual INTEGER,");
		sql.append(" margem_percentual INTEGER,");
		sql.append(" operacao INTEGER UNIQUE NOT NULL);");
		CREATE_TABLE_ACERTO_CLIENTE = sql.toString();
	}

}
