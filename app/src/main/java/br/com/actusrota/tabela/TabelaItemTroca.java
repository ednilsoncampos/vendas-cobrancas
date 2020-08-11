package br.com.actusrota.tabela;

public class TabelaItemTroca extends TabelaItem {
	public static final String TABELA_ITEM_TROCA = "item_troca";
	public static final String FK_VIAGEM = "fk_viagem";

	public static final String[] COLUNAS_ITEM_TROCA = { ID, VALOR_UNITARIO,
			TOTAL_UNITARIO, QUANTIDADE, FK_PRODUTO, FK_VENDA, FK_VIAGEM, ID_WEB };
	
	public static final String CREATE_TABLE_ITEM_TROCA;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ITEM_TROCA);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" valor_unitario INTEGER,");
		sql.append(" total_unitario INTEGER,");
		sql.append(" quantidade INTEGER,");
		sql.append(" fk_produto INTEGER,");
		sql.append(" fk_venda INTEGER,");
		sql.append(" fk_viagem INTEGER,");
		sql.append(" id_web INTEGER,");
		sql.append(" FOREIGN KEY (fk_produto) REFERENCES produto (id), ");
		sql.append(" FOREIGN KEY (fk_venda) REFERENCES venda (id), ");
		sql.append(" FOREIGN KEY (fk_viagem) REFERENCES viagem (id)); ");
		CREATE_TABLE_ITEM_TROCA = sql.toString();
	}
}
