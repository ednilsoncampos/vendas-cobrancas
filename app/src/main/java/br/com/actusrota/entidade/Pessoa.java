package br.com.actusrota.entidade;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Pessoa implements IEntidade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Long id;
	protected String nome;
	protected String cpf;
	protected String rg;
	protected Date dataNascimento;
	// protected String telefone;
	protected Set<Telefone> telefones = new HashSet<Telefone>(0);
	protected String email;

	public Pessoa() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
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
		return true;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public String getRg() {
		return rg;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public Set<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getEmail() {
		return email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isNovo() {
		return id == null || id == 0;
	}
	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder("Pessoa:");
//		sb.append(nome);
//		return sb.toString();
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Pessoa:");
		sb.append(nome);
		sb.append(" - ");
		sb.append(this.cpf);
		sb.append(" - ");
		sb.append(this.email);
		sb.append(" - ");
		sb.append(this.rg);
		sb.append(" - ");
		sb.append(this.dataNascimento);
		sb.append(" - ");
		sb.append(this.telefones);
		sb.append(" ");
		return sb.toString();
	}
}
