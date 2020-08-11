package br.com.actusrota.tabela;

public class TabelaPessoa {
	public static String TABELA_PESSOA = "pessoa";
	public static String ID = "id";
	public static String CPF = "cpf";
	public static String NOME = "nome";
	public static String RG = "rg";
	public static String DATA_NASC = "data_nascimento";
	public static String EMAIL = "email";
	public static String[] COLUNAS_PESSOA = { ID, CPF, NOME, RG, DATA_NASC,
			EMAIL };
	public static String CREATE_TABLE_PESSOA;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_PESSOA);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" cpf TEXT,");
		sql.append(" nome TEXT,");
		sql.append(" rg TEXT,");
		sql.append(" data_nascimento DATE,");
		sql.append(" email TEXT);");
//		sql.append(" UNIQUE (cpf) ON CONFLICT REPLACE);");
		
		//CRIANDO IDICES
		sql.append("CREATE INDEX ");
		sql.append(NOME);
		sql.append("_index ON ");
		sql.append(TABELA_PESSOA);
		sql.append(" (");
		sql.append(NOME);
		sql.append(");");
		
//		sql.append("CREATE INDEX cpf_index ON pessoa (cpf);");

		CREATE_TABLE_PESSOA = sql.toString();
	}	
}
