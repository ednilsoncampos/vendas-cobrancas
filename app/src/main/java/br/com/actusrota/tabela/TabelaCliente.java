package br.com.actusrota.tabela;

public class TabelaCliente {
	public static final int INDICE_ID = 0;
	public static final int INDICE_NUMERO_CONTROLE = 1;
	public static final int INDICE_ENDERECO = 2;

	public static final String TABELA_CLIENTE = "cliente";
	public static final String ID = "id";
	public static final String NUMERO_CONTROLE = "numero_controle";
	public static final String ENDERECO = "fk_endereco";
	public static final String SELECT = " select c.id, c.numero_controle, c.fk_endereco ";
	public static final String[] COLUNAS_CLIENTE = { ID, NUMERO_CONTROLE,
			ENDERECO };
	public static String CREATE_TABLE_CLIENTE;
	static {
		StringBuilder sql = new StringBuilder(" CREATE TABLE ");
		sql.append(TABELA_CLIENTE);
		sql.append("(");
		sql.append(ID);
		sql.append(" INTEGER PRIMARY KEY,");
		sql.append(NUMERO_CONTROLE);
		sql.append(" TEXT,");

		sql.append(ENDERECO);
		sql.append(" INTEGER,");

		sql.append(" FOREIGN KEY(fk_endereco) REFERENCES endereco (id)); ");
		CREATE_TABLE_CLIENTE = sql.toString();
	}
}
