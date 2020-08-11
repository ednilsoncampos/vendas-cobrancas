package br.com.actusrota.tabela;

public class TabelaFuncionario {
	public static String TABELA_FUNCIONARIO = "funcionario";
	public static String ID = "id";
	public static String MATRICULA = "matricula";
	public static String ID_TIPO_FUNCIONARIO = "id_tipo_funcionario";
	public static String FK_PESSOA = "pessoa";
	public static String[] COLUNAS = { ID, MATRICULA, ID_TIPO_FUNCIONARIO };
	public static String CREATE_TABLE_FUNCIONARIO;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_FUNCIONARIO);
		sql.append(" (id INTEGER PRIMARY KEY,");
		sql.append(" matricula TEXT,");
		sql.append(" id_tipo_funcionario CHAR(1),");
		sql.append(" FOREIGN KEY(id) REFERENCES "+FK_PESSOA+"(id)); ");
		CREATE_TABLE_FUNCIONARIO = sql.toString();
	}	
}
