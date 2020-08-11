package br.com.actusrota.tabela;

public class TabelaItemTabPreco {
	public static final String TABELA_ITEM_TAB_PRECO = "item_tabela_preco";
	public static final String ID = "id";
	public static final String PRECO_VENDA = "preco_venda";
	public static final String PRECO_MINIMO_VENDA = "preco_minimo_venda";
	public static final String PRODUTO = "fk_produto";
	public static final String FK_TABELA_PRECO = "fk_tabela_preco";
	public static final String SELECT = " select id, preco_venda, preco_minimo_venda, fk_produto, fk_tabela_preco ";

	public static final String[] COLUNAS_ITEM_TAB_PRECO = { ID, PRECO_VENDA, PRECO_MINIMO_VENDA, PRODUTO,
			FK_TABELA_PRECO };
	public static final String CREATE_TABLE_ITEM_TAB_PRECO;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ITEM_TAB_PRECO);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" preco_venda INTEGER,");
		sql.append(" preco_minimo_venda INTEGER,");
		sql.append(" fk_produto INTEGER,");
		sql.append(" fk_tabela_preco INTEGER,");
		sql.append(" FOREIGN KEY (fk_produto) REFERENCES produto (id), ");
		sql.append(" FOREIGN KEY (fk_tabela_preco) REFERENCES tabela_de_preco (id)); ");
		CREATE_TABLE_ITEM_TAB_PRECO = sql.toString();
	}
}
