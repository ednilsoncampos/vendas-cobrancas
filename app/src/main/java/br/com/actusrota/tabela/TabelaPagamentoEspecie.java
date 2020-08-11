package br.com.actusrota.tabela;

public class TabelaPagamentoEspecie extends TabelaContaReceber {
	public static final String TABELA_PAG_ESPECIE = "pagamento_especie";

	public static final String[] COLUNAS_PAG_ESPECIE = { ID, VALOR,
			DATA_PAGAMENTO, COMISSAO_PAGA, ID_WEB, FK_VENDA, FK_VIAGEM,
			ID_STATUS_VENDA };

	public static final String CREATE_TABLE_PAG_ESPECIE;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_PAG_ESPECIE);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" valor INTEGER,");
		sql.append(" data_pagamento DATE,");
		sql.append(" comissao_paga INTEGER,");
		sql.append(ID_WEB);
		sql.append(" INTEGER,");
		sql.append(" fk_venda INTEGER,");
		sql.append(" fk_viagem INTEGER,");
		sql.append(" id_status_venda CHAR(1), ");
		sql.append(" FOREIGN KEY (fk_venda) REFERENCES venda (id), ");
		sql.append(" FOREIGN KEY (fk_viagem) REFERENCES viagem (id) );");
		CREATE_TABLE_PAG_ESPECIE = sql.toString();
	}
}
