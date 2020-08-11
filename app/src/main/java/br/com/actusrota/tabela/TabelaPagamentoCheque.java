package br.com.actusrota.tabela;

public class TabelaPagamentoCheque extends TabelaContaReceber {
	public static final String TABELA_PAG_CHEQUE = "pagamento_cheque";
	public static final String ID_SATATUS_CHEQUE = "id_status_cheque";
	public static final String DATA_PREVISTA_PAGAMENTO = "data_prevista_pagamento";
	public static final String ID_FORMA_PAGAMENTO = "id_forma_pagamento";
	public static final String NUMERO_CHEQUE = "numero_cheque";
	
	public static final String[] COLUNAS_PAG_CHEQUE = { ID, VALOR, DATA_PAGAMENTO, COMISSAO_PAGA,
		ID_SATATUS_CHEQUE, DATA_PREVISTA_PAGAMENTO, ID_FORMA_PAGAMENTO, NUMERO_CHEQUE, 
		ID_WEB, FK_VENDA, FK_VIAGEM, ID_STATUS_VENDA};
	
	public static final String CREATE_TABLE_PAG_CHEQUE;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_PAG_CHEQUE);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" valor INTEGER,");
		sql.append(" data_pagamento DATE,");
		sql.append(" comissao_paga INTEGER,");
		sql.append(" id_status_cheque CHAR(1),");
		sql.append(" data_prevista_pagamento DATE,");
		sql.append(" id_forma_pagamento CHAR(1),");
		sql.append(" numero_cheque TEXT,");
		sql.append(ID_WEB);
		sql.append(" INTEGER,");
		sql.append(" fk_venda INTEGER,");
		sql.append(" fk_viagem INTEGER,");
		sql.append(" id_status_venda CHAR(1), ");
		sql.append(" FOREIGN KEY (fk_venda) REFERENCES venda (id), ");
		sql.append(" FOREIGN KEY (fk_viagem) REFERENCES viagem (id) ); ");
		
		sql.append(" UNIQUE (numero_cheque) ON CONFLICT REPLACE;");
		
		CREATE_TABLE_PAG_CHEQUE = sql.toString();
	}
}
