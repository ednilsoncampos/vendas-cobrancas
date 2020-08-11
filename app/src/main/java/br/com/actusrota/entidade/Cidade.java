package br.com.actusrota.entidade;

import br.com.actusrota.enumerador.EnumUF;

public class Cidade implements IEntidade, Comparable<Cidade> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nome;
	private String siglaUF;
	private EnumUF uf;

	private Rota rota;

	public Cidade() {
		// TODO Auto-generated constructor stub
	}
	
	public Cidade(Long id) {
		this.id = id;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((siglaUF == null) ? 0 : siglaUF.hashCode());
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
		Cidade other = (Cidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (siglaUF == null) {
			if (other.siglaUF != null)
				return false;
		} else if (!siglaUF.equals(other.siglaUF))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSiglaUF() {
		return siglaUF;
	}

	public void setSiglaUF(String siglaUF) {
		this.siglaUF = siglaUF;
	}

	public EnumUF getUf() {
		return EnumUF.consultarPorSigla(siglaUF);
	}

	public void setUf(EnumUF uf) {
		if (uf != null)
			siglaUF = uf.getSigla();
		this.uf = uf;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public boolean isNovo() {
		return id == null || id <= 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(nome);
		sb.append(" - ");
		sb.append(siglaUF);
		return sb.toString();
	}

	@Override
	public int compareTo(Cidade cidade) {
		return this.nome.compareToIgnoreCase(cidade.getNome());
	}
}
