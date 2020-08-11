package br.com.actusrota.enumerador;

import java.util.ArrayList;
import java.util.List;

public enum EnumStatusCheque {
	EM_COMPENSACAO('E',"A Compensar"),
	COMPENSADO('C',"Compensado"),
	DEVOLVIDO('D',"Devolvido");
	
	private final Character id;
	private final String descricao;
	
	private EnumStatusCheque(Character id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Character getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static EnumStatusCheque consultarPorId(Character id){
		for(EnumStatusCheque status : values()){
			if(status.getId().equals(id))
				return status;
		}
		return null;
	}
	
	public static List<EnumStatusCheque> chequesRecebidos() {
		List<EnumStatusCheque> lista = new ArrayList<EnumStatusCheque>();
		lista.add(DEVOLVIDO);
		lista.add(EM_COMPENSACAO);
		return lista;
	}
}
