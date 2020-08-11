package br.com.actusrota.enumerador;

import java.util.Arrays;
import java.util.List;

public enum EnumTipoFuncionario {
	COBRADOR('C', "Cobrador"),
	VENDEDOR('V', "Vendedor");
	
	private final Character id;
	private final String descricao;
	
	private EnumTipoFuncionario(Character id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	/**
	 * Consulta um tipo de funcionario pelo id
	 * @param id
	 * @return
	 */
	public static EnumTipoFuncionario consultarPorId(Character id){
		for(EnumTipoFuncionario tipoFuncionario: values()){
			if(tipoFuncionario.getId().equals(id))
				return tipoFuncionario;
		}
		return null;
	}
	
	/**
	 * Lista todos os tipos de funcionarios
	 * @return
	 */
	public static List<EnumTipoFuncionario> listarTodos(){
		return Arrays.asList(values());
	}
	
	public Character getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
}
