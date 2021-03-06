package br.com.actusrota.enumerador;

public enum EnumStatusVenda {
	ABERTA('A',"Cobranša"),
	FECHADA('F',"Fechada"),
	EM_COMPENSACAO('C',"Recobranša"),
	CALOTE('D',"Atrasada");
	
	private final Character id;
	private final String descricao;
	
	private EnumStatusVenda(Character id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Character getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	public static EnumStatusVenda consultarPorId(Character id){
		for(EnumStatusVenda status : values()){
			if(status.getId().equals(id))
				return status;
		}
		return null;
	}
}
