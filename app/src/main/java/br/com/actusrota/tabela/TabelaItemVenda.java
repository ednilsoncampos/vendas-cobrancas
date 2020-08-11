package br.com.actusrota.tabela;

public class TabelaItemVenda {
	public static final String TABELA_ITEM_VENDA = "item_venda";
	public static final String ID = "id";
	public static final String VALOR_UNITARIO = "valor_unitario";
	public static final String TOTAL_UNITARIO = "total_unitario";
	public static final String QUANTIDADE = "quantidade";
	public static final String FK_PRODUTO = "fk_produto";
	public static final String FK_VENDA = "fk_venda";
	public static final String ID_WEB = "id_web";

	public static final String[] COLUNAS_ITEM_VENDA = { ID, VALOR_UNITARIO,
			TOTAL_UNITARIO, QUANTIDADE, FK_PRODUTO, FK_VENDA, ID_WEB };
	public static final String CREATE_TABLE_ITEM_VENDA;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ITEM_VENDA);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" valor_unitario INTEGER,");
		sql.append(" total_unitario INTEGER,");
		sql.append(" quantidade INTEGER,");
		sql.append(" fk_produto INTEGER,");
		sql.append(" fk_venda INTEGER,");
		sql.append(" id_web INTEGER,");
		sql.append(" FOREIGN KEY (fk_produto) REFERENCES produto (id), ");
		sql.append(" FOREIGN KEY (fk_venda) REFERENCES venda (id)); ");
		CREATE_TABLE_ITEM_VENDA = sql.toString();
	}
}
