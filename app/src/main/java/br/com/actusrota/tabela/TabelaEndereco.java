package br.com.actusrota.tabela;

public class TabelaEndereco {
	public static final String TABELA_ENDERECO = "endereco";
	public static final String ID = "id";
	public static final String LOGRADOURO = "logradouro";
	public static final String BAIRRO = "bairro";
	public static final String NUMERO = "numero";
	public static final String CEP = "cep";
	public static final String COMPLEMENTO = "complemento";
	public static final String PONTO_REFERENCIA = "ponto_referencia";
	public static final String CIDADE = "fk_cidade";
	public static final String[] COLUNAS_ENDERECO = { ID, LOGRADOURO, BAIRRO,
			NUMERO, CEP, COMPLEMENTO, PONTO_REFERENCIA, CIDADE };
	public static String CREATE_TABLE_ENDERECO;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_ENDERECO);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		sql.append(LOGRADOURO);
		sql.append(" TEXT,");
		sql.append(BAIRRO);
		sql.append(" TEXT,");
		sql.append(NUMERO);
		sql.append(" TEXT,");
		sql.append(CEP);
		sql.append(" TEXT,");
		sql.append(COMPLEMENTO);
		sql.append(" TEXT,");
		sql.append(PONTO_REFERENCIA);
		sql.append(" TEXT,");
		sql.append(CIDADE);
		sql.append(" INTEGER,");
		sql.append(" FOREIGN KEY (fk_cidade) REFERENCES cidade (id)); ");
		CREATE_TABLE_ENDERECO = sql.toString();
	}
}
