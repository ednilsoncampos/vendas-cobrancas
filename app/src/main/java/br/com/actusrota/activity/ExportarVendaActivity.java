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
import android.widget.RadioButton;
import br.com.actusrota.EnviarDadosServidor;
import br.com.actusrota.R;
import br.com.actusrota.adapter.VendaAdapter;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.PagamentoEspecie;
import br.com.actusrota.entidade.Telefone;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.enumerador.EnumMetodoDeEnvio;
import br.com.actusrota.enumerador.EnumStatusModificacao;
import br.com.actusrota.enumerador.EnumStatusSincronizar;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ExportarVendaActivity extends SuperActivity<Venda> {
	// private final static String VENDA_SERVLET =
	// "/actusrota/VendaServlet?jsonVenda=";
	// private final static String VENDA_SERVLET =
	// "/actusrota/VendaServlet?"+usuarioConexao;
	private final String VENDA_SERVLET = "/actusrota/VendaServlet";

	private VendaDAO vendaDAO;

	private ObterValor obterValor;
	private VendaAdapter vendaAdapter;
	private EnumStatusSincronizar statusSincronizar;
	private EnumStatusModificacao statusModificacao;

	public ExportarVendaActivity() {
		vendaDAO = new VendaDAO(this);
		obterValor = new ObterValor(this);
		statusSincronizar = EnumStatusSincronizar.NAO_ENVIADA;
		statusModificacao = EnumStatusModificacao.MODIFICADA;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sincronizar_venda_layout);
		try {
			criarComponentes();
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// criarAdapter(carregarVendaSemItens(statusSincronizar));
			// }
			// }).start();
			pesquisarPorStatusSincronizado();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		// new AdicionarListener(obterValor).adicionarListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// pesquisarPorCodigo();
		// }
		// }, R.id.snv_btnPesquisar);

		new AdicionarListener(obterValor).adicionarListener(
				new EventoEditText(), R.id.snv_edtNome);
	}

	private List<Venda> carregarVendaSemItens() {
		List<Venda> vendas = vendaDAO.carregarVendaSemItens(statusSincronizar,
				statusModificacao);
		return vendas;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void execute(Object... parametros) {
		try {
			if (parametros != null && parametros.length > 0
					&& parametros[0] != null) {

				Venda venda = (Venda) parametros[0];
				
//				if(EnumStatusModificacao.NAO_MODIFICADA.equals(venda.getStatusModificacao())) {
//					mostarMensagem("Venda não modificada não pode ser sincronizada.");
//					return;
//				}
				
				vendaDAO.carregarVendaCompletaExportar(venda);

//				Telefone tel = new Telefone();
//				tel.setDdd("62");
//				tel.setNumero("85141978");
//				tel.setTipoTelefone(EnumTipoTelefone.CELULAR);
//				Set<Telefone> telefones = new HashSet<Telefone>(0);
//				telefones.add(tel);
//				venda.getCliente().setTelefones(telefones);
				
				boolean usarAdapter = true;
				boolean excluirCamposSerializacao = true;
				
				for(PagamentoEspecie pagamento : venda.getPagamentosEspecie()) {
					System.out.println(pagamento);
				}

				// Apos enviar a venda este método removerVendaCompleta(Venda venda) será excutado
				new EnviarDadosServidor<Venda>(this, venda, VENDA_SERVLET,
						false, usarAdapter, excluirCamposSerializacao,
						Viagem.class, Telefone.class).execute(EnumMetodoDeEnvio.POST);
				
//				venda.getCliente().setTelefones(null);
				
//				String json = EntidadeCastJson.entidadeToJsonAdapter(venda,
//						excluirCamposSerializacao, enviar.getTipoAdapter());
//				System.out.println(json);
//				enviar.execute(EnumMetodoDeEnvio.POST);
			} else {
				UtilMensagem.mostarMensagemLonga(this,
						"Parametro não encontrado.");
			}
		} catch (Throwable e) {
			tratarExibirErro(e);
		}
	}

//	public void atualizarVendaParaEnvidada(Venda venda, long idVendaWeb) {
	public void removerVendaCompleta(Venda venda) {
		try {
//			if (EnumStatusVenda.FECHADA.equals(venda.getStatusVenda())) {
//				vendaAdapter.remove(venda);
//				venda = vendaDAO.carregarVendaCompleta(venda.getIdMovel());
//				vendaDAO.delete(venda);
//				mostarMensagem("Venda FECHADA excluída com sucesso.");
//				return;
//			}
//
//			if (EnumStatusSincronizar.NAO_ENVIADA.equals(venda
//					.getStatusSincronizar())) {
//				venda.setStatusSincronizar(EnumStatusSincronizar.ENVIADA);
//			}
//
//			if (idVendaWeb > 0) {
//				venda.setIdVendaWeb(idVendaWeb);
//			}
//			venda.setId(venda.getIdMovel());
//			// Após sincronizar a venda ela não pode ser reenviada sem que haja modificação
//			venda.setStatusModificacao(EnumStatusModificacao.NAO_MODIFICADA);
//			vendaDAO.atualizarVenda(venda.getIdMovel(), venda);
			vendaAdapter.remove(venda);
			venda = vendaDAO.carregarVendaCompleta(venda.getIdMovel());
			vendaDAO.delete(venda);
			vendaAdapter.atualizar();
			mostarMensagem("Venda envida / excluída com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			tratarExibirErro(e);
		}
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
				if (textoDigitado.length() == 0) {
					criarAdapter(new ArrayList<Venda>());
				}
				return;
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
		try {
			List<Venda> vendas = vendaDAO.pesquisarLike(nomeCliente);
			if (vendas == null) {
				vendas = new ArrayList<Venda>();
				Log.w("pesquisarLike:", "lista de vendas null");
			}
			criarAdapter(vendas);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void pesquisarPorCodigo() {
		try {
			String valorEdit = obterValor.getValorEdit(R.id.snv_edtNome);

			if (UtilString.isValorInvalido(valorEdit)) {
				mostarMensagem("Informe o código para prosseguir.");
				return;
			}

			int idVenda = Integer.parseInt(valorEdit);
			List<Venda> vendas = new ArrayList<Venda>();
			Venda venda = vendaDAO.consultarPorId(idVenda);

			if (venda == null) {
				UtilMensagem.mostarMensagemLonga(this, "Venda não encontrada.");
			} else {
				vendas.add(venda);
			}

			criarAdapter(vendas);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarAdapter(List<Venda> vendas) {
		if (vendas == null)
			vendas = new ArrayList<Venda>();
		vendaAdapter = new VendaAdapter(ExportarVendaActivity.this, vendas,
				"Deseja realmente enviar a venda?");
		criarAdapter(vendaAdapter, R.id.snv_ltv_dados);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_cliente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mc_voltar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void eventoCliqueRadioButton(View view) {
		boolean selecionadio = ((RadioButton) view).isChecked();
		RadioButton rdbEnviado = (RadioButton) findViewById(R.id.snv_radioEnviado);
		RadioButton rdbNaoEnviado = (RadioButton) findViewById(R.id.snv_radioNaoEnviado);

		switch (view.getId()) {
		case R.id.snv_radioEnviado:
			if (selecionadio) {
				statusSincronizar = EnumStatusSincronizar.ENVIADA;
				rdbEnviado.setChecked(true);
				rdbNaoEnviado.setChecked(false);
			}
			break;
		case R.id.snv_radioNaoEnviado:
			if (selecionadio) {
				statusSincronizar = EnumStatusSincronizar.NAO_ENVIADA;
				rdbEnviado.setChecked(false);
				rdbNaoEnviado.setChecked(true);
			}
			break;
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.snv_btnPesquisar:
			pesquisarPorCodigo();
			break;
		case R.id.snv_btnPesquisarFiltro:
			pesquisarPorStatusSincronizado();
			break;
		}
	}

	private void pesquisarPorStatusSincronizado() {
		try {
			List<Venda> vendaSemItens = carregarVendaSemItens();
			if (vendaSemItens == null
					|| vendaSemItens.isEmpty()) {
				mostarMensagem("Venda(s) não encontrada(s).");
			}
			criarAdapter(vendaSemItens);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
}
