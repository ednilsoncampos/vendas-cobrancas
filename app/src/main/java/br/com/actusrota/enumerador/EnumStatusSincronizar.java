package br.com.actusrota.enumerador;

public enum EnumStatusSincronizar {

	ENVIADA("Enviada"), NAO_ENVIADA("Não enviada");

	private final String descricao;

	private EnumStatusSincronizar(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumStatusSincronizar getEnumStatusSincronizar(int codigo) {
		switch (codigo) {
		case 0:
			return ENVIADA;
		case 1:
			return NAO_ENVIADA;
		default:
			return null;
		}
	}
}
