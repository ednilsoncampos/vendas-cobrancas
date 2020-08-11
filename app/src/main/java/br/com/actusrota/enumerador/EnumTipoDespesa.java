package br.com.actusrota.enumerador;

public enum EnumTipoDespesa {
	HOTEL("Hotel"), ALIMENTACAO("Alimentação"), OUTROS("Outros");
	
	private final String descricao;

	private EnumTipoDespesa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
