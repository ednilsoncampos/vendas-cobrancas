package br.com.actusrota.entidade;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import br.com.actusrota.util.UtilDinheiro;

public class Abastecimento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Dinheiro valorTotal;
	private int totalLitros;
	private Dinheiro precoMedioLitro;
	private int kilometragemSaidaViagem;
	private int kilometragemRetornoViagem;
	private Set<ItemAbastecimento> itensAbastecimento;
	
	
	public Abastecimento() {
//		valorTotal = Dinheiro.novo();
//		precoMedioLitro = Dinheiro.novo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + kilometragemSaidaViagem;
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
		Abastecimento other = (Abastecimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kilometragemSaidaViagem != other.kilometragemSaidaViagem)
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Dinheiro getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Dinheiro valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public int getTotalLitros() {
		return totalLitros;
	}
	public void setTotalLitros(int totalLitros) {
		this.totalLitros = totalLitros;
	}

	public Dinheiro getPrecoMedioLitro() {
		return precoMedioLitro;
	}

	public void setPrecoMedioLitro(Dinheiro precoMedioLitro) {
		this.precoMedioLitro = precoMedioLitro;
	}
	
	public int getKilometragemSaidaViagem() {
		return kilometragemSaidaViagem;
	}

	public void setKilometragemSaidaViagem(int kilometragemSaidaViagem) {
		this.kilometragemSaidaViagem = kilometragemSaidaViagem;
	}

	public int getKilometragemRetornoViagem() {
		return kilometragemRetornoViagem;
	}

	public void setKilometragemRetornoViagem(int kilometragemRetornoViagem) {
		this.kilometragemRetornoViagem = kilometragemRetornoViagem;
	}
	
	public int getKilometroRodado() {
		int total = 0;
		if(kilometragemRetornoViagem > kilometragemSaidaViagem)
			total = kilometragemRetornoViagem - kilometragemSaidaViagem;
		return total;
	}
	
	public BigDecimal getKilometroPorLitro() {
		if(getTotalLitros() > 0)
		return UtilDinheiro.dividirArredondar(getKilometroRodado(), getTotalLitros());
		return new BigDecimal(0);
	}
	
	public boolean isNovo() {
		return id == null || id <= 0;
	}

	public Set<ItemAbastecimento> getItensAbastecimento() {
		if(itensAbastecimento == null)
			itensAbastecimento = new HashSet<ItemAbastecimento>();
		return itensAbastecimento;
	}

	public void setItensAbastecimento(Set<ItemAbastecimento> itensAbastecimento) {
		this.itensAbastecimento = itensAbastecimento;
	}

}
