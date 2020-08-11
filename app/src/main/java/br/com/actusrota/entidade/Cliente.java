package br.com.actusrota.entidade;

public class Cliente extends Pessoa implements Comparable<Cliente> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String numeroControle;

	private Endereco endereco;
	private Telefone telefone;
	private Cidade cidadeRota;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	public Cliente(Pessoa pessoa) {
		this.id = pessoa.id;
		this.nome = pessoa.nome;
		this.cpf = pessoa.cpf;
		this.rg = pessoa.rg;
		this.dataNascimento = pessoa.dataNascimento;
		// this.telefone = pessoa.telefone;
		this.telefones = pessoa.telefones;
		this.email = pessoa.email;
	}

	public void setPessoa(Pessoa pessoa) {
		this.id = pessoa.id;
		this.nome = pessoa.nome;
		this.cpf = pessoa.cpf;
		this.rg = pessoa.rg;
		this.dataNascimento = pessoa.dataNascimento == null ? null
				: pessoa.dataNascimento;
		// this.telefone = pessoa.telefone;
		this.telefones = pessoa.telefones;
		this.email = pessoa.email;
	}

	public String getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(String numeroControle) {
		this.numeroControle = numeroControle;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Cidade getCidade() {
		Cidade cidade = endereco == null || endereco.isNovo()
				|| endereco.getCidade() == null
				|| endereco.getCidade().isNovo() ? new Cidade() : endereco
				.getCidade();
		return cidade;
	}

	public void setCidade(Cidade cidade) {

	}

	/**
	 * Mantém o alinhamento na tela de pesquisa
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t");
		sb.append(id);
		if (id != null) {
			if (id < 10) {
				sb.append("\t\t\t\t\t");
			} else if (id < 100) {
				sb.append("\t\t\t\t");
			} else if (id < 1000) {
				sb.append("\t\t\t");
			} else if (id < 10000) {
				sb.append("\t\t");
			} else if (id < 100000) {
				sb.append("\t");
			}
		}
		sb.append(nome);
		sb.append(" / ");
		sb.append(getCidade());
		return sb.toString();
	}

	@Override
	public int compareTo(Cliente cliente) {
		return this.nome.compareToIgnoreCase(cliente.getNome());
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Cidade getCidadeRota() {
		return cidadeRota;
	}

	public void setCidadeRota(Cidade cidadeRota) {
		this.cidadeRota = cidadeRota;
	}

}
