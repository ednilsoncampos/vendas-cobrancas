package br.com.actusrota.enumerador;

public enum EnumStatusModificacao {
	/**
	 * Venda modificada pode ser sincronizada
	 * Venda não modificada não pode ser sincronizada
	 */
	MODIFICADA("Modificada"), NAO_MODIFICADA("Não Modificada");

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
