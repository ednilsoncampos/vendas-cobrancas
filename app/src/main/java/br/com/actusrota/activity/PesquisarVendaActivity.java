package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import br.com.actusrota.R;
import br.com.actusrota.adapter.PesquisarVendaAdapter;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class PesquisarVendaActivity extends SuperActivity<Venda> {

	private VendaDAO vendaDAO;
	private ObterValor obterValor;
	private PesquisarVendaAdapter vendaAdapter;
	private Venda venda;
	private Long idViagem;

	public PesquisarVendaActivity() {
		vendaDAO = new VendaDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pesquisar_venda_layout);
		try {
			buscarUltimaViagem();
			criarComponentes();
			obterValor.getEditText(R.id.pvl_edtNome).requestFocus();
			carregarTela();
			// criarAdapterCidade();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void carregarTela() {
		criarAdapterPesquisarVenda(vendaDAO.carregarVendasSemItens());		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_pesquisar_venda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (vendaAdapter.getVenda() == null || vendaAdapter.getVenda().isNovo()) {
			mostarMensagem("Selecione uma venda para prosseguir.");
			return false;
		}
		try {
			venda = vendaDAO.carregarVendaCompleta(vendaAdapter.getVenda()
					.getId());
			venda.somarTodosItens();
		} catch (Exception e) {
			tratarExibirErro(e);
			return false;
		}
		switch (item.getItemId()) {
		case R.id.mnpv_editar:
			editarVenda();
			return true;
		case R.id.mnpv_devolucao:
			criarTelaItemAcerto(EnumOperacao.DEVOLUCAO);
			return true;
		case R.id.mnpv_brinde:
			criarTelaItemAcerto(EnumOperacao.BRINDE);
			return true;
		case R.id.mnpv_brinde_extra:
			criarTelaItemAcerto(EnumOperacao.BRINDE_EXTRA);
			return true;
		case R.id.mnpv_troca:
			criarTelaItemAcerto(EnumOperacao.TROCA);
			return true;
		case R.id.mnpv_brinde_dinheiro:
			criarActivityComParametro(BrindeDinheiroActivity.class, venda);
			return true;
		case R.id.mnpv_receb_cheque:
			criarActivityComParametro(PagChequeActivity.class, venda);
			return true;
		case R.id.mnpv_receb_dinheiro:
			criarActivityComParametro(PagDinherioActivity.class, venda);
			return true;
		case R.id.mnpv_detalhamento_venda:
			criarDetalhamentoDeVenda();
			return true;
		case R.id.mnpv_delete_venda:
			deleteVenda();
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void deleteVenda() {
		try {
			Venda venda = vendaDAO.carregarVendaCompleta(vendaAdapter.getVenda().getId());
			boolean delete = vendaDAO.delete(venda);
			if (delete) {
				mostarMensagem("Venda excluída com sucesso!");
				vendaAdapter.atualizar();
			} else {
				mostarMensagem("Venda não pode ser excluída!");
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarDetalhamentoDeVenda() {
		try {
			criarActivityComParametro(DetalhamentoVenda.class, venda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarTelaItemAcerto(EnumOperacao operacao) {
		try {
			Intent intent = new Intent(this, ItemAcertoActivity.class);
			intent.putExtra("operacao", operacao);
			intent.putExtra("venda", venda);
			intent.putExtra("idViagem", idViagem);
			startActivityForResult(intent, 0);
		} catch (NumberFormatException e) {
			mostarMensagemCurta("Apenas valor numérico é permitido.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void buscarUltimaViagem() {
		try {
			idViagem = vendaDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void editarVenda() {
		if (venda.isVendaWeb()) {
			mostarMensagem("Venda Web não pode ser alterada.");
			return;
		}
		try {
			criarActivityComParametro(VendaActivity.class, venda);
		} catch (Throwable e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		obterValor.adicionarMascara(
				obterValor.getEditText(R.id.pvl_edtDtVencimento), "##/##/####");

		new AdicionarListener(obterValor).adicionarListener(
				new EventoEditText(), R.id.pvl_edtNome);
	}

	class EventoEditText implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

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
				// if (textoDigitado.length() == 0) {
				// criarAdapterPesquisarVenda(new ArrayList<Venda>());
				// }
				// return;
			}
			if (textoDigitado.length() == 3) {
				pesquisarLike(textoDigitado.toString());
				return;
			}
			if (textoDigitado.length() > 3) {
				if (textoDigitado.length() % 3 == 0)
					pesquisarLike(textoDigitado.toString());
				return;
			}
		}

	}

	public void pesquisarLike(String nomeCliente) {
		List<Venda> vendas = null;
		try {
			if (!obterValor.isInValidoValorEdit(R.id.pvl_edtDtVencimento)) {
				vendas = vendaDAO.pesquisarLike(nomeCliente,
						obterValor.getDataEditText(R.id.pvl_edtDtVencimento));
			} else {
				vendas = vendaDAO.pesquisarLike(nomeCliente);
			}

			if (vendas == null) {
				vendas = new ArrayList<Venda>();
				Log.w("pesquisarLike:", "lista de vendas null");
			}
			criarAdapterPesquisarVenda(vendas);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void pesquisarPorCodigo() {
		try {
			String valorEdit = obterValor.getValorEdit(R.id.pvl_edtNome);

			if (UtilString.isValorInvalido(valorEdit)) {
				mostarMensagem("Informe o código para prosseguir.");
				return;
			}

			int idVenda = Integer.parseInt(valorEdit);
			pesquisarPorCodigo(idVenda);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void pesquisarPorCodigo(int idVenda) {
		try {
			List<Venda> vendas = new ArrayList<Venda>();
			Venda venda = vendaDAO.consultarPorId(idVenda);
			if (venda == null) {
				UtilMensagem.mostarMensagemLonga(this, "Venda não encontrada.");
			} else {
				vendas.add(venda);
			}
			criarAdapterPesquisarVenda(vendas);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarAdapterPesquisarVenda(List<Venda> vendas) {
		try {
			if (vendas == null)
				vendas = new ArrayList<Venda>(0);
			final ListView listView = (ListView) findViewById(R.id.pvl_ltv_dados);

			// ArrayAdapter adapter = new ArrayAdapter(this,
			// R.layout.listview_fonte_menor, null);
			vendaAdapter = new PesquisarVendaAdapter(this, vendas);
			listView.setAdapter(vendaAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.pvl_btnPesquisar:
			pesquisarPorCodigo();
			break;
		}
	}

	// private void criarAdapterCidade() {
	// Spinner spinner = (Spinner) findViewById(R.id.pvl_cidade);
	// // Create an ArrayAdapter using the string array and a default spinner
	// layout
	// // ArrayAdapter<CharSequence> adapter =
	// ArrayAdapter.createFromResource(this,
	// // R.array.planets_array, android.R.layout.simple_spinner_item);
	//
	// List<Cidade> cidades = new ArrayList<Cidade>();
	// Cidade cidade = new Cidade(1l);
	// cidade.setNome("Inhumas");
	// CidadeAdapter adapter = new CidadeAdapter(this, cidades);
	//
	// // Specify the layout to use when the list of choices appears
	// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// // Apply the adapter to the spinner
	// spinner.setAdapter(adapter);
	// }

}
