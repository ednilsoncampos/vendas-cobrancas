package br.com.actusrota.entidade;

import br.com.actusrota.util.UtilCriptografia;

public class Usuario implements IEntidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nomeUsuario;
	private String senha;
	private String senhaHash;
	private boolean principal;
	private String empresa;

	private Funcionario funcionario;
	private boolean podeSerAlterado;
	private String cpf;

	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		if (senha != null && senha.length() > 0 && !senha.equals(senhaHash))
			this.senhaHash = UtilCriptografia.toHash(senha);
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@Override
	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public boolean isPodeSerAlterado() {
		return podeSerAlterado;
	}

	public void setPodeSerAlterado(boolean podeSerAlterado) {
		this.podeSerAlterado = podeSerAlterado;
	}
}
