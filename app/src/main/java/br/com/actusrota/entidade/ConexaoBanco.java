package br.com.actusrota.entidade;

public class ConexaoBanco implements IEntidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConexaoBanco() {
		// TODO Auto-generated constructor stub
	}
	
	

	public ConexaoBanco(String usuarioConexao) {
		this();
		this.usuarioConexao = usuarioConexao;
	}

	private Long id;
	private String usuarioConexao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuarioConexao() {
		return usuarioConexao;
	}

	public void setUsuarioConexao(String usuarioConexao) {
		this.usuarioConexao = usuarioConexao;
	}

	public boolean isNovo() {
		return id == null || id == 0;
	}
	
	@Override
	public String toString() {
		return usuarioConexao;
	}
}
