package br.com.actusrota.entidade;

import java.io.Serializable;

public interface IEntidade extends Serializable {

	public boolean isNovo();
	
	public Long getId();
}
