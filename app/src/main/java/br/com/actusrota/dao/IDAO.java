package br.com.actusrota.dao;

import java.util.Collection;
import java.util.List;

import br.com.actusrota.entidade.IEntidade;

public interface IDAO<T extends IEntidade> {
	public void adicionar(Collection<T> dados);
	public void adicionarImportacao(Collection<T> dados);
	public void adicionarImportacao(T dados);
	public boolean atulizar(List<T> listaAtualizar);
	public void adicionar(T entidade);
}
