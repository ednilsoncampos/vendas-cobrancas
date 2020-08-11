package br.com.actusrota.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.client.ClientProtocolException;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.actusrota.AcaoDialog;
import br.com.actusrota.ConexaoHttp;
import br.com.actusrota.R;
import br.com.actusrota.dao.FuncionarioDAO;
import br.com.actusrota.entidade.Usuario;
import br.com.actusrota.enumerador.EnumMetodoDeEnvio;
import br.com.actusrota.util.EntidadeCastJson;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilArquivo;
import br.com.actusrota.util.UtilMensagem;

public class LoginActivity extends SuperActivity<Usuario> implements AcaoDialog {

	public static String NOME_EMPRESA;

	private String USUARIO_SERVLET = "/actusrota/UsuarioServlet?";
	private String LOGIN_SERVLET = "/actusrota/LoginServlet";
	private final String CPF = "&cpf=";

	private FuncionarioDAO funcionarioDAO;

	public LoginActivity() {
		funcionarioDAO = new FuncionarioDAO(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		USUARIO_SERVLET += usuarioConexao + CPF;
//		LOGIN_SERVLET += usuarioConexao;
		setContentView(R.layout.activity_login);

		final EditText editEmpresa = (EditText) findViewById(R.id.editEmpresa);
		final EditText editUsuario = (EditText) findViewById(R.id.editUsuario);
		final EditText editSenha = (EditText) findViewById(R.id.editSenha);
		final CheckBox checkPrimeiroAcesso = (CheckBox) findViewById(R.id.checkPrimeiroAcesso);
		checkPrimeiroAcesso.setChecked(true);

		Button botaoEntrar = (Button) findViewById(R.id.btnEntrar);

		Usuario usuarioArquivoSalvo = null;
		try {
			usuarioArquivoSalvo = recuperarArquivo();
		} catch (IOException e1) {
			// tratarExibirErro("Erro ao recuperar arquivo:", e1);
		}

		if (usuarioArquivoSalvo != null) {
			editEmpresa.setText(usuarioArquivoSalvo.getEmpresa());
			editUsuario.setText(usuarioArquivoSalvo.getNomeUsuario());

			editEmpresa.setEnabled(false);
			editUsuario.setEnabled(false);
			checkPrimeiroAcesso.setChecked(false);
			checkPrimeiroAcesso.setEnabled(false);
		}

		botaoEntrar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String nomeEmpresa = editEmpresa.getEditableText().toString();
				String nomeUsuario = editUsuario.getEditableText().toString();
				String senha = editSenha.getEditableText().toString();

				if (nomeEmpresa.trim().equals(nomeUsuario.trim())) {
					mostarMensagem("Nome da Empresa e Usuário devem ser diferentes.");
					return;
				}

				Usuario usuario = new Usuario();
				usuario.setEmpresa(nomeEmpresa);
				usuario.setNomeUsuario(nomeUsuario);
				usuario.setSenha(senha.trim());

				Usuario retorno = null;
				Usuario usuarioArquivo = null;
				try {
					usuarioArquivo = recuperarArquivo();

					if (usuarioArquivo != null
							&& !usuarioArquivo.getNomeUsuario()
									.equalsIgnoreCase(usuario.getNomeUsuario())) {
						// TODO Mostrar mensagem de confirmação de
						// alteração de
						// usuário
						// Todos os dados serão apagados
					}
				} catch (Exception e) {
					tratarExibirErro("Erro ao recuperar arquivo no aparelho.",
							e);
				}

				// autenticação
				try {
					// login no server
					if (checkPrimeiroAcesso.isChecked()
							|| usuarioArquivo == null) {

						retorno = autenticarServer(usuario);
						if (retorno == null) {
							mostarMensagem("Usuário não encontrado: "
									+ usuario.getNomeUsuario());
							return;
						}
						retorno.setEmpresa(usuario.getEmpresa());
						gravarArquivo(retorno);
						System.out.println("navegar para tela inicial");
						NOME_EMPRESA = retorno.getEmpresa();
						criarActivityGenerica(ActusActivity.class);
						finish();
						return;

						// if ("true".equals(retorno)) {
						// gravarArquivo(usuario);
						// }

					}
					// login no dispositivo movel
					if (usuarioArquivo.getNomeUsuario().equalsIgnoreCase(
							usuario.getNomeUsuario())
							&& usuarioArquivo.getSenhaHash().equals(
									usuario.getSenhaHash())) {
						System.out.println("navegar para tela inicial");
						NOME_EMPRESA = usuario.getEmpresa();
						criarActivityGenerica(ActusActivity.class);
						finish();
					} else {
						mostarMensagem("Usuário e/ou senha incorreto(s)");
					}

				} catch (Throwable e) {
					tratarExibirErro("Erro ao autenticar com o servidor.", e);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Usuario autenticarServer(Usuario usuario)
			throws ClientProtocolException, IOException {
		String reposta = "";
		LoginActivity.NOME_EMPRESA = usuario.getEmpresa();
		try {
			ConexaoHttp con = new ConexaoHttp(EnumMetodoDeEnvio.POST, true,
					false, null);
			reposta = con
					.enviarDados(LOGIN_SERVLET, usuario, false);
			if (reposta.length() > 5) {
				Usuario jsonToEntidade = EntidadeCastJson.jsonToEntidade(reposta, Usuario.class);
				return jsonToEntidade;
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	private void gravarArquivo(Usuario usuario) throws IOException {
		UtilArquivo.gravarArquivo(usuario, UtilArquivo.NOME_ARQUIVO, this);
		// FileOutputStream fos = openFileOutput(NOME_ARQUIVO,
		// Context.MODE_PRIVATE);
		// String dadosArquivo = usuario.getEmpresa() + "/"
		// + usuario.getNomeUsuario() + "/" + usuario.getSenha() + "/";
		// fos.write(dadosArquivo.getBytes());
		// fos.flush();
		// fos.close();
	}

	private Usuario recuperarArquivo() throws IOException {

		try {
			return UtilArquivo.recuperarUsuario(this, UtilArquivo.NOME_ARQUIVO);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ml_sincronizar:
			// if(new ObterValor(this).isInValidoValorEdit(R.id.editEmpresa)) {
			// mostarMensagem("A empresa deve ser informada");
			// return false;
			// }
			criarDialog(this, "Usu?rio", "CPF");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void execute(Object... parametro) {
		sincronizarUsuario((String) parametro[1]);
	}

	@SuppressWarnings("unchecked")
	// private void sincronizarUsuario(String cpf) {
	// try {
	// if (cpf == null) {
	// UtilMensagem.mostarMensagemCurta(this,
	// "Informe o CPF para prosseguir.");
	// return;
	// }
	//
	// String urlFinal = USUARIO_SERVLET + cpf;
	// Log.i("URL Usuario: ", urlFinal);
	//
	// ConexaoHttp conexao = new ConexaoHttp(EnumMetodoDeEnvio.GET, false,
	// false);
	//
	// String resposta = conexao.importarDadosJson(urlFinal);
	// if (resposta == null) {
	// mostarMensagem("Registro não encontrado. Verifique seu cadastro com o Administrador.");
	// return;
	// }
	//
	// String[] array = resposta.split("##");
	//
	// if (array == null) {
	// mostarMensagem("Registro não encontrado. Verifique seu cadastro com o Administrador.");
	// return;
	// }
	//
	// if (array.length != 3) {
	// mostarMensagem(array[0]);
	// return;
	// }
	//
	// // Usuario usuario = EntidadeCastJson.jsonToEntidade(resposta,
	// // Usuario.class);
	// //
	// // if (usuario == null || usuario.isNovo()) {
	// // mostarMensagem("Registro não pode ser salvo.");
	// // return;
	// // }
	//
	// ObterValor obterValor = new ObterValor(this);
	// Usuario usuario = new Usuario();
	// // usuario.setCpf(resposta);
	// // usuario.setEmpresa(obterValor.getValorEdit(R.id.editEmpresa));
	// // usuario.setNomeUsuario(obterValor.getValorEdit(R.id.editUsuario));
	// // usuario.setSenha(obterValor.getValorEdit(R.id.editSenha));
	//
	// usuario.setCpf(array[0]);
	// usuario.setNomeUsuario(array[1]);
	// usuario.setSenhaHash(array[2]);
	// usuario.setEmpresa(obterValor.getValorEdit(R.id.editEmpresa));
	//
	// UtilArquivo.gravarArquivo(usuario, NOME_ARQUIVO, this);
	// mostarMensagem("Usuario salvo com sucesso.");
	// setarDados(usuario);
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }
	private void sincronizarUsuario(String cpf) {
		try {
			if (cpf == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Informe o CPF para prosseguir.");
				return;
			}

			String urlFinal = USUARIO_SERVLET + cpf;
			Log.i("URL Usuario: ", urlFinal);

			ConexaoHttp conexao = new ConexaoHttp(EnumMetodoDeEnvio.GET, false,
					false);

			String resposta = conexao.importarDadosJson(urlFinal);
			if (resposta == null) {
				mostarMensagem("Registro não encontrado. Verifique seu cadastro com o Administrador.");
				return;
			}

			Usuario usuario = EntidadeCastJson.jsonToEntidade(resposta,
					Usuario.class);

			if (usuario == null || usuario.isNovo() || usuario.getCpf() == null) {
				mostarMensagem("Registro não pode ser salvo.");
				return;
			}

			usuario.setEmpresa(new ObterValor(this)
					.getValorEdit(R.id.editEmpresa));

			UtilArquivo.gravarArquivo(usuario, UtilArquivo.NOME_ARQUIVO, this);

			funcionarioDAO.adicionarPesquisarPessoa(usuario.getFuncionario());

			mostarMensagem("Usuário salvo com sucesso.");
			setarDados(usuario);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void setarDados(Usuario usuario) {
		try {
			final EditText editEmpresa = (EditText) findViewById(R.id.editEmpresa);
			final EditText editUsuario = (EditText) findViewById(R.id.editUsuario);
			final CheckBox checkPrimeiroAcesso = (CheckBox) findViewById(R.id.checkPrimeiroAcesso);
			checkPrimeiroAcesso.setChecked(true);

			if (usuario != null) {
				editEmpresa.setText(usuario.getEmpresa());
				editUsuario.setText(usuario.getNomeUsuario());

				editEmpresa.setEnabled(false);
				editUsuario.setEnabled(false);
				checkPrimeiroAcesso.setChecked(false);
				checkPrimeiroAcesso.setEnabled(false);
			}
		} catch (Exception e) {
			mostarMensagem("Erro:" + e.getMessage());
		}
	}

}
