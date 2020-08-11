package br.com.actusrota.entidade;

import java.util.Set;

/**
 * Projeto android
 * 
 * @author EDNILSON
 * 
 */
public class Rota implements IEntidade, Comparable<Rota> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1326565264609420403L;

	private Long id;
	private String descricao = "";
//	private Funcionario responsavel;

	private Set<Cidade> cidades;
	
	public Rota() {
		// TODO Auto-generated constructor stub
	}
	
	public Rota(Long id) {
		this();
		this.id = id;
	}	

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((descricao == null) ? 0 : descricao.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result
//				+ ((responsavel == null) ? 0 : responsavel.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Rota other = (Rota) obj;
//		if (descricao == null) {
//			if (other.descricao != null)
//				return false;
//		} else if (!descricao.equals(other.descricao))
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (responsavel == null) {
//			if (other.responsavel != null)
//				return false;
//		} else if (!responsavel.equals(other.responsavel))
//			return false;
//		return true;
//	}

	public boolean isNovo() {
		return id == null || id <= 0;
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

//	public Funcionario getResponsavel() {
//		return responsavel;
//	}
	
//	public void setResponsavel(Funcionario responsavel) {
//		this.responsavel = responsavel;
//	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Set<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(Set<Cidade> cidades) {
		this.cidades = cidades;
	}

	@Override
	public String toString() {
		return descricao;
	}

	@Override
	public int compareTo(Rota rota) {
		return this.descricao.compareToIgnoreCase(rota.getDescricao());
	}
}
