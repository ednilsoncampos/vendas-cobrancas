package br.com.actusrota.tabela;

public class TabelaViagem {
	public static final String TABELA_VIAGEM = "viagem";
	public static final String ID = "id";
	public static final String DATA_SAIDA = "data_saida";
	public static final String DATA_RETORNO = "data_retorno";
	public static final String VALOR_TOTAL_SAIDA = "valor_total_saida";
	public static final String ADIANTAMENTO = "adiantamento";
	public static final String VEICULO = "veiculo";
//	public static final String RESPONSAVEL = "fk_responsavel";

	// public static final String ROTA = "fk_rota";
	// public static final String TABELA_PRECO = "fk_tabela_preco";
	// public static final String ABASTECIMENTO = "fk_abastecimento";

	public static final String[] COLUNAS_VIAGEM = { ID, DATA_SAIDA,
			DATA_RETORNO, VALOR_TOTAL_SAIDA, ADIANTAMENTO, VEICULO };

	public static String CREATE_TABLE_VIAGEM;

	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_VIAGEM);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		sql.append(DATA_SAIDA);
		sql.append(" DATE,");
		sql.append(DATA_RETORNO);
		sql.append(" DATE,");
		sql.append(VALOR_TOTAL_SAIDA);
		sql.append(" INTEGER,");
		sql.append(ADIANTAMENTO);
		sql.append(" INTEGER,");
		sql.append(VEICULO);
		sql.append(" TEXT);");
		// sql.append(RESPONSAVEL);sql.append(" INTEGER,");
		// sql.append(ROTA);sql.append(" INTEGER,");
		// sql.append(TABELA_PRECO);sql.append(" INTEGER,");
		// sql.append(ABASTECIMENTO);sql.append(" INTEGER,");
		// sql.append(" FOREIGN KEY (responsavel)     REFERENCES funcionario (id), ");
		// sql.append(" FOREIGN KEY (fk_rota)         REFERENCES rota (id)); ");
		// sql.append(" FOREIGN KEY (fk_tabela_preco) REFERENCES tabela_de_preco (id)); ");
		// sql.append(" FOREIGN KEY(fk_abastecimento) REFERENCES abastecimento (id)); ");
		CREATE_TABLE_VIAGEM = sql.toString();
	}
}
