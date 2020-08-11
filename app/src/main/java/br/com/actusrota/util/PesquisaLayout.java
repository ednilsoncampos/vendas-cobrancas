package br.com.actusrota.util;

import br.com.actusrota.R;

/**
 * Encapsula a chamada de R.id, caso haja renomeacao dos componentes
 * Criada para facilitar o acesso aos componentes de pesquisa_layout
 * utilizada em ProdutoActivity e CidadeActivity
 * 
 * @author EDNILSON
 * 
 */
public class PesquisaLayout {
	
	public static int textViewTitulo = R.id.pes_txvTitulo;
	
	public static int textViewNome = R.id.pes_txvNome;
	
	public static int editNome = R.id.pes_edtNome;

	public static int btnPesquisar = R.id.pes_btnPesquisar;
	
	public static int labelNome = R.id.pes_txvLabelNome;
	
	public static int listViewDados = R.id.pes_ltv_dados;

}
