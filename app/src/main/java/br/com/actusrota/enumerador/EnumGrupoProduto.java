package br.com.actusrota.enumerador;

/**
 * 
 * Enumerador de grupo de produto
 * 
 * @author Ricardo
 * Data: 26/01/2012
 * @version $Rev:  $ $Author:  $ $Date: $
 * @category Enum
 */
public enum EnumGrupoProduto {
	
	CAMA(1,"Cama"),
	MESA(2,"Mesa"),
	BANHO(3,"Banho"),
	OUTROS(4,"Outros");
	
	private final Integer id;
	private final String descricao;
	
	EnumGrupoProduto(Integer id, String descricao){
		this.id = id;
		this.descricao = descricao;
	}
	
	/**
	 * Consulta pelo id do grupo
	 * @param id
	 * @return
	 */
	public static EnumGrupoProduto consultar(Integer id){
		for (EnumGrupoProduto grupo : values()) {
			if(grupo.getId().equals(id))
				return grupo;
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
