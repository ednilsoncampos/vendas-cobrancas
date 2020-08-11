package br.com.actusrota.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.com.actusrota.R;
import br.com.actusrota.dao.AcertoClienteParametroDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.negocio.AcertoClienteNegocio;
import br.com.actusrota.util.ObterValor;

public class BrindeDinheiroActivity extends SuperActivity<Venda> {

	private VendaDAO vendaDAO;
	private Venda venda;
	private ObterValor obterValor;
	private AcertoClienteParametroDAO acertoClienteParametroDAO;

	// private Long idVenda;

	public BrindeDinheiroActivity() {
		vendaDAO = new VendaDAO(this);
		obterValor = new ObterValor(this);
		acertoClienteParametroDAO = new AcertoClienteParametroDAO(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brinde_dinheiro_layout);
		obterParametrosIntent();
		// carregarVenda();
		criarComponentes();
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.brd_btnSalvar:
			salvarBrindeDinheiro();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_brinde_dinheiro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mbd_acerto_salvar:
			salvar();
			return true;
		case R.id.mbd_acerto_voltar:
			finish();
			return true;
		case R.id.mbd_detalhamento_venda:
			criarDetalhamentoDeVenda();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void criarDetalhamentoDeVenda() {
		try {
			venda = vendaDAO.carregarVendaCompleta(venda.getId());
			venda.setComissaoClienteEspecie(obterValor
					.getValorMonetarioEdit(R.id.din_edtValor));
			venda.somarTodosItens();
			criarActivityComParametro(DetalhamentoVenda.class, venda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void salvarBrindeDinheiro() {
		if (venda == null) {
			mostarMensagem("Venda não carregada.");
			return;
		}
		if (obterValor.getValorMonetarioEdit(R.id.brd_edtBrindeRS).getValor() >= venda
				.getValorTotal().getValor()) {
			mostarMensagem("O valor do brinde dever ser menor que o valor da venda.");
			return;
		}

		AcertoClienteParametro acertoCliente = acertoClienteParametroDAO
				.buscar(EnumOperacao.COMISSAO_CLIENTE);
		if (acertoCliente != null) {

			AcertoClienteNegocio acertoNegocio = new AcertoClienteNegocio(
					acertoCliente);

			Dinheiro comissaoCliente = obterValor
					.getValorMonetarioEdit(R.id.brd_edtBrindeRS);

			if (!acertoNegocio.isMargemValida(venda.getValorTotalRecebido(),
					comissaoCliente)) {
				mostarMensagem("Porcentagem do "
						+ EnumOperacao.COMISSAO_CLIENTE.getDescricao()
						+ " excede o valor permitido:"
						+ acertoCliente.getMargemTotal());
				return;
			}
		}

		criarDialogSalvar();
	}

	@Override
	protected void salvar() {
		try {
			venda.setComissaoClienteEspecie(obterValor
					.getValorMonetarioEdit(R.id.brd_edtBrindeRS));
			
			venda.fecharVenda();
			
			vendaDAO.atualizar(venda.getId(), venda);
			mostarMensagem("Brinde em dinheiro salvo com sucesso!");
			Thread.sleep(1000);
			finish();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// private void carregarVenda() {
	// try {
	// if (venda == null || venda.isNovo()) {
	// mostarMensagem("Parametro para venda não foi recebido.");
	// return;
	// }
	// venda = vendaDAO.consultarPorId(idVenda);
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	private void obterParametrosIntent() {
		try {
			// Bundle b = getIntent().getExtras();
			// Long idVenda = b.getLong("idVenda");
			// if (idVenda == null)
			// mostarMensagem("Parametro para venda não foi recebido.");
			// else
			// this.idVenda = idVenda;

			Bundle b = getIntent().getExtras();
			Object objVenda = b.get("parametro");
			if (objVenda == null)
				mostarMensagem("Parametro para venda não foi recebido.");
			else
				this.venda = (Venda) objVenda;

		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		if (venda == null) {
			mostarMensagem("Venda não pode ser carregada.");
			return;
		}
		try {
			obterValor.setTextoTextView("Cliente: "
					+ venda.getCliente().getNome(), R.id.brd_txvCliente);
			obterValor.setTextoTextView(
					"Nº Venda: " + venda.getId().toString(),
					R.id.brd_txvNumVenda);
			obterValor.setTextoTextView("Nº Viagem: "
					+ venda.getId().toString(), R.id.brd_txvNumViagem);

			obterValor.setTextoTextView(venda.getValorTotal()
					.getValorFormatado(), R.id.brd_txvTotalVenda);

			obterValor.setTextoEditText(venda.getComissaoClienteEspecie()
					.getValorFormatado(), R.id.brd_edtBrindeRS);

			obterValor.setTextoTextView(venda.getCliente().getCidade()
					.getNome(), R.id.brd_txvCidade);

			obterValor.setTextoTextView("Brinde em Dinheiro",
					R.id.brd_txvTitulo);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

}
