/**
 * 
 * Descriï¿½ï¿½o
 * 
 * @author ACTUSROTA Team
 * Data: 07/02/2012
 * @version $Rev:  $ $Author: $ $Date:  $
 * @category br.com.actusrota.entidade
 */
package br.com.actusrota.entidade;

import java.io.Serializable;

import br.com.actusrota.enumerador.EnumGrupoProduto;

public class Produto implements Serializable, Comparable<Produto>, IEntidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -820835718430861677L;

	private Long id;
	private String descricao;
	private String codigo;
	private boolean ativo;

	private Integer idGrupo;
	private EnumGrupoProduto grupo;
	private String descricaoGrupo;

	public Produto() {
		super();
	}

	public Produto(Long id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}

	public Produto(String descricao) {
		super();
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public EnumGrupoProduto getGrupo() {
		return EnumGrupoProduto.consultar(idGrupo);
	}

	public void setGrupo(EnumGrupoProduto grupo) {
		if (grupo != null) {
			idGrupo = grupo.getId();
		}
	}

	/**
	 * Mantém o alinhamento na tela de pesquisa
	 * Tamanho para o código é 6
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t");
		sb.append(codigo);
		if (codigo != null) {
			if (codigo.length() == 1) {
				sb.append("\t\t\t\t");
			} else if (codigo.length() == 2){
				sb.append("\t\t\t\t");
			} else if (codigo.length() == 3){
				sb.append("\t\t\t");
			} else if (codigo.length() == 4){
				sb.append("\t\t");
			} else if (codigo.length() == 5){
				sb.append("\t");
			}
		}
		sb.append(descricao);
		return sb.toString();
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public String getCodigo() {
		return codigo;
	}

	public boolean isCodigoInvalido() {
		return codigo == null || codigo.trim().length() <= 0;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public int compareTo(Produto produto) {
		return codigo.compareToIgnoreCase(produto.getCodigo());
	}

	public String getDescricaoGrupo() {
		EnumGrupoProduto grupo = getGrupo();
		if (grupo != null)
			descricaoGrupo = grupo.getDescricao();
		else
			descricaoGrupo = "";
		return descricaoGrupo;
	}

}
