package br.com.actusrota.tabela;

public class TabelaVenda {
	public static final String TABELA_VENDA = "venda";
	public static final String ID = "id";
	public static final String DATA_VENDA = "data_venda";
	public static final String DATA_VENCIMENTO = "data_vencimento";
	public static final String VALOR_TOTAL = "valor_total";
	public static final String COMISSAO_FUNCIONARIO = "comissao_funcionario";
	public static final String COMISSAO_CLIENTE = "comissao_cliente_especie";
	public static final String ID_SATUS_VENDA = "id_status_venda";
	public static final String STATUS_SINCRONIZAR = "status_sincronizar";
	public static final String ID_VENDA_WEB = "id_venda_web";
	public static final String FK_CLIENTE = "fk_cliente";
	public static final String FK_VIAGEM = "fk_viagem";
	public static final String FK_ROTA = "fk_rota";
	public static final String STATUS_MODIFICACAO = "status_modificacao";
	public static final String SQL_SELECT_CAMPOS = "SELECT v.id, v.data_venda, v.data_vencimento, v.valor_total,"
			+ "v.comissao_funcionario, v.comissao_cliente_especie, v.id_status_venda, v.status_sincronizar, v.id_venda_web, v.fk_cliente, v.fk_viagem, v.fk_rota, v.status_modificacao FROM venda v ";

	public static final String[] COLUNAS_VENDA = { ID, DATA_VENDA,
			DATA_VENCIMENTO, VALOR_TOTAL, COMISSAO_FUNCIONARIO,
			COMISSAO_CLIENTE, ID_SATUS_VENDA, STATUS_SINCRONIZAR, ID_VENDA_WEB,
			FK_CLIENTE, FK_VIAGEM, FK_ROTA, STATUS_MODIFICACAO };

	public static String CREATE_TABLE_VENDA;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_VENDA);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		sql.append(DATA_VENDA);
		sql.append(" DATE,");
		sql.append(DATA_VENCIMENTO);
		sql.append(" DATE,");
		sql.append(VALOR_TOTAL);
		sql.append(" INTEGER,");
		sql.append(COMISSAO_FUNCIONARIO);
		sql.append(" INTEGER,");
		sql.append(COMISSAO_CLIENTE);
		sql.append(" INTEGER,");
		sql.append(ID_SATUS_VENDA);
		sql.append(" CHAR(1),");

		sql.append(STATUS_SINCRONIZAR);
		sql.append(" INTEGER,");

		sql.append(ID_VENDA_WEB);
		sql.append(" INTEGER,");
		sql.append(FK_CLIENTE);
		sql.append(" INTEGER,");
		
		sql.append(FK_VIAGEM);
		sql.append(" INTEGER,");
		
		sql.append(FK_ROTA);
		sql.append(" INTEGER,");

		sql.append(STATUS_MODIFICACAO);
		sql.append(" INTEGER,");

		sql.append(" FOREIGN KEY (fk_cliente) REFERENCES cliente (id), ");
		sql.append(" FOREIGN KEY (fk_viagem) REFERENCES viagem (id), ");
		sql.append(" FOREIGN KEY (fk_rota) REFERENCES rota (id)); ");
		CREATE_TABLE_VENDA = sql.toString();
	}
}
