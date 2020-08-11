package br.com.actusrota.entidade;

import java.io.Serializable;


public interface ItemManter extends Item, Serializable {
	void setVenda(Venda venda);
	void setViagem(Viagem viagem);
	Venda getVenda();
	Viagem getViagem();
}
