package br.com.actusrota.tabela;

public class TabelaItemViagem {
	public static final String TABELA_ITEM_VIAGEM = "item_viagem";
	public static final String ID = "id";
	public static final String VALOR_UNITARIO = "valor_unitario";
	public static final String TOTAL_UNITARIO = "total_unitario";
	public static final String QUANTIDADE = "quantidade";
	public static final String FK_PRODUTO = "fk_produto";
	public static final String FK_VIAGEM = "fk_viagem";
	
	public static final String[] COLUNAS_ITEM_VIAGEM = { ID, VALOR_UNITARIO,
			TOTAL_UNITARIO, QUANTIDADE, FK_PRODUTO, FK_VIAGEM };
	public static final String CREATE_TABLE_ITEM_VIAGEM;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ITEM_VIAGEM);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" valor_unitario INTEGER,");
		sql.append(" total_unitario INTEGER,");
		sql.append(" quantidade INTEGER,");
		sql.append(" fk_produto INTEGER,");
		sql.append(" fk_viagem INTEGER,");
		sql.append(" FOREIGN KEY (fk_produto) REFERENCES produto (id), ");
		sql.append(" FOREIGN KEY (fk_viagem) REFERENCES viagem (id)); ");
		CREATE_TABLE_ITEM_VIAGEM = sql.toString();
	}
}
