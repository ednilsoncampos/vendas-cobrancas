package br.com.actusrota.enumerador;

public enum EnumTipoTelefone {
	CELULAR("Celular"), 
	COMERCIAL("Comercial"), 
	RESIDENCIAL("Residêncial"), 
	RECADO("Recado");

	private final String descricao;

	private EnumTipoTelefone(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static EnumTipoTelefone buscarEnum(int indice) {
		switch (indice) {
		case 0:
			return CELULAR;
		case 1:
			return COMERCIAL;
		case 2:
			return RESIDENCIAL;	
		case 3:
			return RECADO;				
		default:
			return null;
		}
	}
}
