package br.com.actusrota;

import java.util.List;

import org.apache.http.HttpStatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import br.com.actusrota.dao.IDAO;
import br.com.actusrota.entidade.IEntidade;
import br.com.actusrota.enumerador.EnumMetodoDeEnvio;

public class Importacao<T extends IEntidade> extends
		AsyncTask<EnumMetodoDeEnvio, Void, List<T>> {

	private final Context contexto;
	private final ProgressDialog progressDialog;
	private final Class<T> classe;
	private final String servlet;
	private final IDAO<T> dao;
	private Class<? extends IEntidade>[] tipoAdapter;
	private final boolean usarAdapter;
	private String erroResposta = "";
	private Integer httpStatus = 0;
	private boolean esperaLisa;
	private boolean excluirCamposSerializacao;
	private CharSequence erro;

	public Importacao(Context contexto, Class<T> classe, IDAO<T> dao,
			String servlet, boolean esperaLisa, boolean usarAdapter,
			boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		this.contexto = contexto;
		this.classe = classe;
		this.servlet = servlet;
		this.dao = dao;
		this.progressDialog = new ProgressDialog(contexto);
		this.tipoAdapter = tipoAdapter;
		this.usarAdapter = usarAdapter;
		this.esperaLisa = esperaLisa;
		this.excluirCamposSerializacao = excluirCamposSerializacao;
	}

	@Override
	protected void onPreExecute() {
		try {
			this.progressDialog.setTitle("Aguarde...");
			this.progressDialog
					.setMessage("Sincronizando dados com o servidor.");
			this.progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(contexto, "Detalhe do erro:" + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected List<T> doInBackground(EnumMetodoDeEnvio... parametro) {
		List<T> dados = null;
		try {
			ConexaoHttp conexaoHttp = null;
			if (parametro == null || parametro.length == 0) {
				conexaoHttp = new ConexaoHttp(esperaLisa, usarAdapter,
						tipoAdapter);
			} else {
				conexaoHttp = new ConexaoHttp(parametro[0], usarAdapter,
						esperaLisa, tipoAdapter);
			}
			String json = conexaoHttp.importarDadosJson(servlet);
			
			if("[]".equals(json)) {
				erro = "Nenhum registro foi importado";
				return null;
			}
			
			httpStatus = conexaoHttp.getHttpStatus();
			if (HttpStatus.SC_OK != httpStatus) {
				erroResposta = json;
				return null;
			}

			dados = conexaoHttp.converterJson(classe, json,
					excluirCamposSerializacao);
			Log.i("Retorno:", "Total de " + classe.getSimpleName()
					+ " importado(s):" + dados.size());
		} catch (Throwable e) {// java.net.SocketException
			e.printStackTrace();
			String msg = "Erro ao sincronizar " + classe.getSimpleName();
			Log.e(msg, e.getMessage());

			if (e instanceof java.net.UnknownHostException) {
				erro = "Operação não realizada.Verifique a conexão de internet.";
			} else if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
				erro = "Operação não realizada. Verifique se houve perda de conexão com a internet.";
			} else {
				erro = e.getMessage();
			}
		}
		return dados;
	}

	@Override
	protected void onPostExecute(List<T> result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		if (erro != null) {
			Toast.makeText(contexto, erro, Toast.LENGTH_LONG).show();
			erro = null;
			return;
		}

		if (erroResposta.trim().length() > 0) {
			new TratarResposta(contexto).tratarResposta(erroResposta);
			return;
		}

		if (HttpStatus.SC_OK == httpStatus) {
//			Toast.makeText(contexto, "Dados sincronizados com sucesso",
//					Toast.LENGTH_LONG).show();
			Log.i("Importação sucesso", "Dados sincronizados com sucesso");
		}
		httpStatus = 0;
		if (result == null || result.isEmpty() || result.get(0) == null) {
			Toast.makeText(
					contexto,
					"Dados não inseridos. Falha na conversão de:"
							+ classe.getSimpleName(), Toast.LENGTH_LONG).show();
			return;
		}
		inserirDados(result);

	}

	private void inserirDados(List<T> result) {
		try {
			dao.adicionarImportacao(result);
			System.out.println(result);
			String msg = classe.getSimpleName()
					+ "(s) salvo(a)(s) com sucesso.";// + result.size() +
														// " registro(s) no total.";
			Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
			Log.i("Sucesso!", msg);
			// dialog(result);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Erro ao salvar:" + classe.getSimpleName() + "(s). ";
			Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
			Toast.makeText(contexto, "Detalhe do erro:" + e.getMessage(),
					Toast.LENGTH_LONG).show();
			dialog(result);// como obter os nao inseridos
			Log.e("Erro!", msg);
		}
	}

	private void dialog(List<T> result) {

		final String[] array = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			array[i] = result.get(i).toString();
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

		builder.setTitle("Dados não inseridos");

		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						// OK, go back to Main menu
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		builder.setItems(array, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int indice) {
				// Toast.makeText(contexto, array[indice],
				// Toast.LENGTH_SHORT).show();
			}
		});
		// builder.create();

		builder.show();
	}
}
