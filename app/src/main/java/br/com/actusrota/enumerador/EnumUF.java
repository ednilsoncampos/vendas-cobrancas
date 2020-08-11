package br.com.actusrota.enumerador;

import java.util.Arrays;
import java.util.List;

public enum EnumUF {
	
	ACRE("Acre","AC"),
	ALAGOAS("Alagoas","AL"),
	AMAPA("Amapá","AP"),
	AMAZONAS("Amazonas","AM"),
	BAHIA("Bahia","BA"),
	CEARA("Ceará","CE"),
	DISTRITO_FEDERAL("Distrito Federal","DF"),
	ESPIRITO_SANTO("Espírito Santo","ES"),
	GOIAS("Goiás","GO"),
	MARANHAO("Maranhão","MA"),
	MATO_GROSSO("Mato Grosso","MT"),
	MATO_GROSSO_DO_SUL("Mato Grosso do Sul","MS"),
	MINAS_GERAIS("Minas Gerais","MG"),
	PARA("Pará","PA"),
	PARAIBA("Paraíba","PB"),
	PARANA("Paraná","PR"),
	PERNAMBUCO("Pernambuco","PE"),
	PIAUI("Piauí","PI"),
	RIO_DE_JANEIRO("Rio de Janeiro","RJ"),
	RIO_GRANDE_DO_NORTE("Rio Grande do Norte","RN"),
	RIO_GRANDE_DO_SUL("Rio Grande do Sul","RS"),
	RONDONIA("Rondônia","RO"),
	RORAIMA("Roraima","RR"),
	SANTA_CATARINA("Santa Catarina","SC"),
	SAO_PAULO("São Paulo","SP"),
	SERGIPE("Sergipe","SE"),
	TOCANTINS("Tocantins","TO");
	
	private final String nome;
	private final String sigla;

	EnumUF(String nome, String sigla){
		this.nome = nome;
		this.sigla = sigla;
	}
	
	/**
	 * Consulta pela sigla
	 * @param sigla
	 * @return
	 */
	public static EnumUF consultarPorSigla(String sigla){
		for (EnumUF uf : values()) {
			if(uf.getSigla().equals(sigla))
				return uf;
		}
		return null;
	}
	
	/**
	 * lista todas as UFs
	 * @return
	 */
	public static List<EnumUF> listarTodos(){
		return Arrays.asList(values());
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getSigla() {
		return sigla;
	}
}
