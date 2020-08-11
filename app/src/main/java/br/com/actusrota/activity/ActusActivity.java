package br.com.actusrota.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.dao.AcertoClienteParametroDAO;
import br.com.actusrota.dao.ConexaoBancoWebDAO;
import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.entidade.ConexaoBanco;
import br.com.actusrota.entidade.Usuario;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilArquivo;
import br.com.actusrota.util.UtilMensagem;

public class ActusActivity extends SuperActivity {
	private ConexaoBancoWebDAO conexaoBancoWebDAO;

	private String ACERTO_CLIENTE_SERVLET = "/actusrota/AcertoClienteServlet?";
	private AcertoClienteParametroDAO acertoClienteDAO;

	public ActusActivity() {
		conexaoBancoWebDAO = new ConexaoBancoWebDAO(this);
		acertoClienteDAO = new AcertoClienteParametroDAO(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ACERTO_CLIENTE_SERVLET += usuarioConexao;
		setContentView(R.layout.activity_actus);
		try {
			Usuario usuario = UtilArquivo.recuperarUsuario(this,
					UtilArquivo.NOME_ARQUIVO);
			if (usuario == null || usuario.getNomeUsuario() == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Usuário não registrado.");
				return;
			}
			new ObterValor(this).setTextoTextView(
					"Usuário: " + usuario.getNomeUsuario(),
					R.id.act_txvResponsavel);
		} catch (Exception e) {
			e.printStackTrace();
			UtilMensagem.mostarMensagemCurta(this, "Erro:" + e.getMessage());
		}
		// if (isUsuarioNaoCadastrado())
		// criarTelas(FuncionarioActivity.class);
	}

	private void sincronizarAcertoCliente() {
		try {
			boolean usarAdapter = false;
			boolean esperaLisa = true;
			boolean excluirCamposSerializacao = false;
			new Importacao<AcertoClienteParametro>(this,
					AcertoClienteParametro.class, acertoClienteDAO,
					ACERTO_CLIENTE_SERVLET, esperaLisa, usarAdapter,
					excluirCamposSerializacao).execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	protected boolean isUsuarioNaoCadastrado() {
		ConexaoBanco conexao = conexaoBancoWebDAO
				.buscarUsuarioConexaoCadastrado();
		return conexao == null || conexao.isNovo();
	}

	// private void criarTelaFuncionario() {
	// try {
	// Intent intent = new Intent(this,
	// FuncionarioActivity.class);
	// // startActivityForResult(intent, 0);
	// } catch (Exception e) {
	// mostarMensagem(e.getMessage());
	// }
	// }

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == Activity.RESULT_OK) {
	// Bundle bundle = data.getExtras();
	// Object obj = bundle.get("usuarioCadastrado");
	// if ( obj == null || ((Boolean)obj == false) ) {
	// mostarMensagem("O usuario de conexï¿½o deve ser cadastrado.");
	// // criarTelaFuncionario();
	// }
	// }
	// }

	protected void mostarMensagem(String mensagem) {
		UtilMensagem.mostarMensagemLonga(this, mensagem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mp_acerto_cliente:
			sincronizarAcertoCliente();
			return true;
			// case R.id.mp_rota:
			// criarTelas(RotaActivity.class);
			// return true;
			// case R.id.mp_cidade:
			// criarTelas(CidadeActivity.class);
			// return true;
			// case R.id.mp_cliente:
			// criarTelas(ClienteActivity.class);
			// return true;
			// case R.id.mp_viagem:
			// criarTelas(ViagemActivity.class);
			// return true;
			// case R.id.mp_venda:
			// criarTelas(VendaActivity.class);
			// return true;
			// case R.id.mp_enviar_venda:
			// criarTelas(ExportarVendaActivity.class);
			// return true;
			// case R.id.mp_importar_venda:// o menu importar venda dever abrir
			// cidades por rota
			// criarTelas(CidadeRotaActivity.class);
			// return true;
			// case R.id.mp_funcionario:
			// criarTelas(FuncionarioActivity.class);
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.act_imgProduto:
			criarTelas(ProdutoActivity.class);
			break;
		case R.id.act_imgRota:
			criarTelas(RotaActivity.class);
			break;
		// case R.id.act_imgCidade:
		// criarTelas(CidadeActivity.class);
		// break;
		// case R.id.act_imgCliente:
		// criarTelas(ClienteActivity.class);
		// break;
		case R.id.act_imgViagem:
			criarTelas(ViagemActivity.class);
			break;
		// case R.id.act_imgVenda:
		// criarTelas(VendaActivity.class);
		// break;
		case R.id.act_imgSincronizarVenda:
			criarTelas(ExportarVendaActivity.class);
			break;
		// case R.id.act_imgFuncionario:
		// criarTelas(FuncionarioActivity.class);
		// break;
		case R.id.act_imgCidadesRota:
			// criarTelas(CidadeRotaActivity.class);
			criarTelas(RotaActivity.class);
			break;
		case R.id.act_pesquisaVenda:
			criarTelas(PesquisarVendaActivity.class);
			break;
		}

	}

	private void criarTelas(Class<? extends Activity> classeActivity) {
		try {
			startActivity(new Intent(this, classeActivity));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro:" + classeActivity.getSimpleName(),
					e.getMessage());
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Encerrar")
				.setMessage("Deseja realmente sair?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).setNegativeButton("Não", null).show();
	}
}
