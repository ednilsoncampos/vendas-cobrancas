package br.com.actusrota.entidade;

import java.io.Serializable;
import java.util.Date;

import br.com.actusrota.enumerador.EnumTipoDespesa;

public class Despesa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String descricao;
	private Date dataDespesa;
	private Dinheiro valorDespesa;
	private EnumTipoDespesa tipoDespesa;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataDespesa == null) ? 0 : dataDespesa.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((valorDespesa == null) ? 0 : valorDespesa.hashCode());
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
		Despesa other = (Despesa) obj;
		if (dataDespesa == null) {
			if (other.dataDespesa != null)
				return false;
		} else if (!dataDespesa.equals(other.dataDespesa))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valorDespesa == null) {
			if (other.valorDespesa != null)
				return false;
		} else if (!valorDespesa.equals(other.valorDespesa))
			return false;
		return true;
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
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Date getDataDespesa() {
		return dataDespesa;
	}
	public void setDataDespesa(Date dataDespesa) {
		this.dataDespesa = dataDespesa;
	}
	
	public Dinheiro getValorDespesa() {
		return valorDespesa;
	}
	public void setValorDespesa(Dinheiro valorDespesa) {
		this.valorDespesa = valorDespesa;
	}
	
	public boolean isNovo() {
		return id == null || id == 0;
	}
	
	public EnumTipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}
	public void setTipoDespesa(EnumTipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}	

}
