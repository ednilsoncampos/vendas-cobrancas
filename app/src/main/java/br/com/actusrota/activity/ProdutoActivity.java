package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.dao.ProdutoDAO;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.PesquisaLayout;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ProdutoActivity extends SuperActivity<Produto> {

	private String PRODUTO_SERVLET = "/actusrota/ExportadorProdutoServlet?";
	private final ProdutoDAO produtoDAO;
	private ObterValor obterValor;

	public ProdutoActivity() {
		produtoDAO = new ProdutoDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PRODUTO_SERVLET += usuarioConexao;
		setContentView(R.layout.pesquisa_layout);

		criarComponentes();
		try {
			List<Produto> produtos = produtoDAO.getProdutos();
			if (produtos == null || produtos.isEmpty()) {
//				mostarMensagem("Produtos não encontrados. Sincronize os produtos.");
				return;
			}
			criarAdapter(produtos, PesquisaLayout.listViewDados);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_produto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnp_sincronizar:
			sincronizarProduto();
			return true;
		case R.id.mnp_voltar:
			finish();
			return true;
		case R.id.mnp_listarTodos:
			listarTodos();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("unchecked")
	private void sincronizarProduto() {
		try {
			boolean usarAdapter = false;
			new Importacao<Produto>(this, Produto.class, produtoDAO,
					PRODUTO_SERVLET, true, usarAdapter, false).execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
			new AdicionarListener(obterValor).adicionarListener(
					new EventoEditText(), PesquisaLayout.editNome);

			obterValor.desabilitarTecladoVirtual(PesquisaLayout.editNome);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.pes_btnPesquisar:
			pesquisarPorCodigo();
			break;
		}
	}

	class EventoEditText implements TextWatcher {

		@Override
		public void afterTextChanged(Editable editable) {
			if (editable == null || editable.toString().length() > 0) {
				return;
			}
			listarTodos();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		/**
		 * Pesquisa a cada 3 caracteres digitados
		 */
		@Override
		public void onTextChanged(CharSequence textoDigitado, int arg1,
				int arg2, int arg3) {
			if (textoDigitado.length() < 3) {
				return;
			}
			if (textoDigitado.length() == 3) {
				pesquisarLike();
				return;
			}
			if (textoDigitado.length() > 3) {
				if (textoDigitado.length() % 3 == 0)
					pesquisarLike();
				return;
			}
		}

	}

	/**
	 * Caso o texo digitado seja um número com mais
	 * de 3 caracteres a pesquisa deve ser feita por código
	 */
	public void pesquisarLike() {
		String valorEdit = obterValor.getValorEdit(PesquisaLayout.editNome);
		try {
			Integer.parseInt(valorEdit);
			pesquisarPorCodigo(valorEdit);
			return;
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		try {
			List<Produto> produtos = produtoDAO.pesquisarLike(valorEdit);
			if (produtos == null) {
				produtos = new ArrayList<Produto>();
				Log.w("pesquisarLike:", "lista de produtos null");
			}
			criarAdapter(produtos, PesquisaLayout.listViewDados);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void pesquisarPorCodigo() {
		String codigo = obterValor.getValorEdit(PesquisaLayout.editNome);
		if (UtilString.isValorInvalido(codigo)) {
			mostarMensagem("Informe o código para prosseguir");
			return;
		}
		try {
			Integer.parseInt(codigo);// valida se é numero ou letra
			pesquisarPorCodigo(codigo);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numerico.");
		} 
	}
	
	private void pesquisarPorCodigo(String codigo) {
		try {
			List<Produto> produtos = new ArrayList<Produto>();
			Produto produto = produtoDAO.pesquisarPorCodigo(codigo);

			if (produto == null) {
				UtilMensagem.mostarMensagemLonga(this,
						"Produto não encontrado.");
			} else {
				produtos.add(produto);
			}

			criarAdapter(produtos, PesquisaLayout.listViewDados);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numerico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void listarTodos() {
		try {
			List<Produto> produtos = produtoDAO.getProdutos();
			if (produtos == null || produtos.isEmpty()) {
				mostarMensagem("Produtos não encontrados. Sincronize os produtos.");
				return;
			}
			criarAdapter(produtos, PesquisaLayout.listViewDados);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

}
