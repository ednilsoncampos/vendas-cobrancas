package br.com.actusrota.entidade;

import br.com.actusrota.enumerador.EnumTipoFuncionario;


public class Funcionario extends Pessoa {
	/**
	 * 
	 */
	private static final long serialVersionUID = -241502009032993513L;
	
	private String matricula;
	private Character idTipoFuncionario;
	private EnumTipoFuncionario tipoFuncionario;

	public Funcionario() {
		// TODO Auto-generated constructor stub
	}
	
	public Funcionario(Long id) {
		this.id = id;
	}
	
	public Funcionario(Pessoa pessoa) {
		super();
		this.id = pessoa.id;
		this.nome = pessoa.nome;
		this.cpf = pessoa.cpf;
		this.rg = pessoa.rg;
		this.dataNascimento = pessoa.dataNascimento;
		this.email = pessoa.email;
	}

	public String getMatricula() {
		return matricula;
	}

	public Character getIdTipoFuncionario() {
		return idTipoFuncionario;
	}

	public EnumTipoFuncionario getTipoFuncionario() {
		return EnumTipoFuncionario.consultarPorId(idTipoFuncionario);
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public void setIdTipoFuncionario(Character idTipoFuncionario) {
		this.idTipoFuncionario = idTipoFuncionario;
	}

	public void setTipoFuncionario(EnumTipoFuncionario tipoFuncionario) {
		if(tipoFuncionario != null)
			idTipoFuncionario = tipoFuncionario.getId();
		this.tipoFuncionario = tipoFuncionario;
	}

}
