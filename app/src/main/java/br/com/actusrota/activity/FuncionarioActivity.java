package br.com.actusrota.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import br.com.actusrota.R;
import br.com.actusrota.dao.ConexaoBancoWebDAO;
import br.com.actusrota.dao.FuncionarioDAO;
import br.com.actusrota.entidade.ConexaoBanco;
import br.com.actusrota.entidade.Funcionario;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilString;

public class FuncionarioActivity extends SuperActivity<Funcionario> {

	private FuncionarioDAO funcionarioDAO;
	private ObterValor obterValor;
	private Funcionario funcionario;
	private ConexaoBancoWebDAO conexaoBancoWebDAO;
	private ConexaoBanco conexaoBanco;
	private boolean usuarioCadastrado = false;
	private String cpf;

	public FuncionarioActivity() {
		funcionarioDAO = new FuncionarioDAO(this);
		obterValor = new ObterValor(this);
		conexaoBancoWebDAO = new ConexaoBancoWebDAO(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.funcionario_layout);
		buscarFuncionario();
		buscarUsuarioCadastrado();
		criarComponentes();
	}

	private void buscarUsuarioCadastrado() {
		try {
			conexaoBanco = conexaoBancoWebDAO.buscarUsuarioConexaoCadastrado();
			if (conexaoBanco == null || conexaoBanco.isNovo()) {
				usuarioCadastrado = false;
			} else {
				usuarioCadastrado = true;
				obterValor.setTextoEditText(conexaoBanco.getUsuarioConexao(),
						R.id.fun_edtUsuario);
			}
		} catch (Exception e) {
			mostarMensagem(e.getMessage());
		}
	}

	private void buscarFuncionario() {
		try {
			funcionario = funcionarioDAO.buscarFuncionarioCadastrado();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
			if (funcionario != null && !funcionario.isNovo()) {
				obterValor.setTextoEditText(funcionario.getNome(),
						R.id.fun_edtNome);
			}
			obterValor.desabilitarTecladoVirtual(R.id.fun_edtUsuario);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_usuario_conexao, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuc_voltar:
			return validarUsuarioCadastrado();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(!validarUsuarioCadastrado());
	}

	boolean validarUsuarioCadastrado() {
		if (usuarioCadastrado) {
			finish();
			return true;
		} else {
			mostarMensagem("O usuario de conexão deve ser cadastrado.");
			return false;
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.fun_btnSalvar:
			salvarFuncionario();
			break;
		case R.id.fun_btnSalvarUsuario:
			salvarUsuario();
			break;
		}
	}

	private void salvarUsuario() {
		if (obterValor.isInValidoValorEdit(R.id.fun_edtUsuario)) {
			mostarMensagem("O usuario de conexão deve ser informado.");
			return;
		}
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Salvando registro")
				.setMessage("Deseja realmente salvar?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									conexaoBanco = new ConexaoBanco(obterValor
											.getValorEdit(R.id.fun_edtUsuario));
									conexaoBancoWebDAO.adicionar(conexaoBanco);
									buscarUsuarioConexao();
									usuarioCadastrado = true;
									mostarMensagem("Usuario salvo com sucesso.");
								} catch (Exception e) {
									tratarExibirErro(e);
								}
							}

						}).setNegativeButton("Não", null).show();

	}

	private void salvarFuncionario() {
		if (funcionario == null || funcionario.isNovo()) {
			mostarMensagem("Funcionario não cadastrado. Verifique se as rotas foram importadas.");
			return;
		}
		String cpf = obterValor.getValorEdit(R.id.fun_edtCpf);
		if (UtilString.isValorInvalido(cpf)) {
			mostarMensagem("Informe o cpf para prosseguir.");
			return;
		}

		if (cpf.length() < 11) {
			mostarMensagem("O CPF deve conter 11 dígitos.");
			return;
		}

		if (!UtilString.validarCPF(cpf)) {
			mostarMensagem("CPF informado é inválido.");
			return;
		}
		this.cpf = cpf;
		criarDialogSalvar();
	}

	@Override
	protected void salvar() {
		funcionario.setCpf(UtilString.removerMaskCPF(cpf));
		funcionario.setNome(obterValor.getValorEdit(R.id.fun_edtNome));

		try {
			funcionarioDAO.atualizar(funcionario.getId(), funcionario);
			mostarMensagem("Dados do funcionario atualizado com sucesso.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
}
