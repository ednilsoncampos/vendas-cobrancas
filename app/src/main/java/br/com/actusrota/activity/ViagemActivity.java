package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import br.com.actusrota.AcaoDialog;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ItemViagemAdapter;
import br.com.actusrota.dao.ViagemDAO;
import br.com.actusrota.entidade.Funcionario;
import br.com.actusrota.entidade.ItemViagem;
import br.com.actusrota.entidade.Usuario;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilArquivo;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ViagemActivity extends SuperActivity<Viagem> implements AcaoDialog {

	private static final String NOME_ARQUIVO = "login.txt";
	
	private String VIAGEM_SERVLET = "/actusrota/ViagemServlet?";
	private final String IMPORTAR = "&importarViagem=";
	private final String CPF = "&cpf=";
	// localhost:8080/actusrota/ViagemServlet?numViagem=3&importarViagem=true

	private ViagemDAO viagemDAO;
	private ObterValor obterValor;
	private Viagem viagem;

	public ViagemActivity() {
		viagemDAO = new ViagemDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		VIAGEM_SERVLET += usuarioConexao + "&numViagem=";
		setContentView(R.layout.viagem_layout);
		criarComponentes();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_viagem, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnvig_voltar:
			finish();
			return true;
		case R.id.mnvig_sincronizar:
			criarDialog(this, "Viagem", "Nº da viagem");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setarDados() {
		try {
			if (viagem == null) {
				UtilMensagem
						.mostarMensagemCurta(this, "Viagem não encontrada.");
				return;
			}
			obterValor.setTextoTextView(viagem.getAdiantamento().getValorFormatado(),
					R.id.vig_txvAdiantamento);

//			obterValor.setTextoTextView(viagem.getResponsavel().getNome(),
//					R.id.vig_txvResponsavel);
			
			if (viagem.getDataSaida() != null) {
				obterValor.setTextoTextView(
						UtilDates.formatarData(viagem.getDataSaida()),
						R.id.vig_txvDataSaida);
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
			// obterValor.desabilitarTecladoVirtual(R.id.vig_edtNumViagem);
			new AdicionarListener(obterValor).adicionarListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							buscarViagem();
						}

					}, R.id.vig_btnPesquisarViagem);
			obterValor.setFocusImagemButton(R.id.vig_btnPesquisarViagem);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void buscarViagem() {
		String valorEdit = obterValor.getValorEdit(R.id.vig_edtNumViagem);
		valorEdit = valorEdit.trim();
		if (UtilString.isValorInvalido(valorEdit)) {
			UtilMensagem.mostarMensagemCurta(this,
					"O Nº da viagem deve ser informado.");
			return;
		}
		try {
			viagem = viagemDAO.consultarPorId(Long.parseLong(valorEdit));
			if (viagem == null) {
				mostarMensagem("**Viagem não encontrada**.");
				return;
			}
			
//			if (viagem.getRota() == null) {
//				mostarMensagem("Rota da viagem não importada. Sincronize as rotas e tente novamente.");
//				return;
//			}			
			
			setarDados();

			List<ItemViagem> itens = new ArrayList<ItemViagem>(viagem.getItensViagem());
			Collections.sort(itens);
			
			criarAdapterItem(new ItemViagemAdapter(this,
					castItemManterToItemListView(itens)),
					R.id.vig_ltv_itens);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro("Verifique se há alterações nos produtos ou importe novamente.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void sincronizarViagem(String numViagem) {
		try {
			if (numViagem == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Informe o número da viagem para prosseguir.");
				return;
			}

//			String cpf = buscarFuncionarioCadastrado(Long.parseLong(numViagem));
			//if (cpf == null || cpf.trim().length() == 0) {
			Usuario usuario = UtilArquivo.recuperarUsuario(this, NOME_ARQUIVO);
			if (usuario == null) {
				mostarMensagem("Funcionario não cadastrado.");
//				mostarMensagem("Importe as rotas para efetuar cadastro.");
				return;
			}

			String urlFinal = VIAGEM_SERVLET + numViagem + IMPORTAR + true
					+ CPF + usuario.getCpf();
			Log.i("URL Viagem", urlFinal);
			// tabela de preço utilizada como adapter para viagem
			boolean usarAdapter = true;
			boolean esperaLisa = false;
			new Importacao<Viagem>(this, Viagem.class, viagemDAO, urlFinal,
					esperaLisa, usarAdapter, false)
					.execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public void execute(Object... parametro) {
		sincronizarViagem((String) parametro[1]);
	}

	public String buscarFuncionarioCadastrado(long idViagem) {
		String cpf = "";
		try {
			viagemDAO.abrirBancoSomenteLeitura();
			Long idFuncionario = viagemDAO.getFuncionarioDAO().buscarMaxID();
			Funcionario funcionario = viagemDAO.getFuncionarioDAO()
					.consultarPorId(idFuncionario);
			cpf = funcionario.getCpf();
		} catch (Exception e) {
			tratarExibirErro(e);
		} finally {
			viagemDAO.fecharBanco();
		}
		return cpf;
	}
}
