package br.com.actusrota.enumerador;

public enum EnumStatusModificacao {
	/**
	 * Venda modificada pode ser sincronizada
	 * Venda n�o modificada n�o pode ser sincronizada
	 */
	MODIFICADA("Modificada"), NAO_MODIFICADA("N�o Modificada");

	private final String descricao;

	private EnumStatusModificacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumStatusModificacao getEnumStatusModificacao(int codigo) {
		switch (codigo) {
		case 0:
			return MODIFICADA;
		case 1:
			return NAO_MODIFICADA;
		default:
			return null;
		}
	}
}
