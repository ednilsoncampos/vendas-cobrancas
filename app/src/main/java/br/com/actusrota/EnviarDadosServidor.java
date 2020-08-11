package br.com.actusrota;

import org.apache.http.HttpStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import br.com.actusrota.activity.ExportarVendaActivity;
import br.com.actusrota.entidade.IEntidade;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumMetodoDeEnvio;

public class EnviarDadosServidor<T extends IEntidade> extends
		AsyncTask<EnumMetodoDeEnvio, Void, String> {

	private final Context contexto;
	private final ProgressDialog progressDialog;
	private Class<? extends IEntidade>[] tipoAdapter;
	private final T entidade;
	private final String servlet;
	private final boolean usarAdapter;
	private boolean sucesso;
	private Integer httpStatus = 0;
	private boolean esperaLisa;
	private boolean excluirCamposSerializacao;
	private String erro;

	public EnviarDadosServidor(Context contexto, T entidade, String servlet,
			boolean esperaLisa, boolean usarAdapter,
			boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		this.contexto = contexto;
		this.entidade = entidade;
		this.servlet = servlet;
		this.usarAdapter = usarAdapter;
		this.progressDialog = new ProgressDialog(contexto);
		this.tipoAdapter = tipoAdapter;
		this.esperaLisa = esperaLisa;
		this.excluirCamposSerializacao = excluirCamposSerializacao;
	}

	@Override
	protected void onPreExecute() {
		try {
			this.progressDialog.setTitle("Aguarde...");
			this.progressDialog.setMessage("Enviando dados para o servidor.");
			this.progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(contexto, "Detalhe do erro:" + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected String doInBackground(EnumMetodoDeEnvio... parametro) {
		String resposta = "";
		try {
			ConexaoHttp conexaoHttp = null;
			if (parametro == null || parametro.length == 0) {
				conexaoHttp = new ConexaoHttp(esperaLisa, usarAdapter,
						tipoAdapter);
			} else {
				conexaoHttp = new ConexaoHttp(parametro[0], esperaLisa,
						usarAdapter, tipoAdapter);
			}
			
			resposta = conexaoHttp.enviarDados(servlet, entidade,
					excluirCamposSerializacao);

			httpStatus = conexaoHttp.getHttpStatus();
			if (HttpStatus.SC_OK != httpStatus) {
				return resposta;
			}

			Log.i("Retorno:", "Dados de "
					+ this.entidade.getClass().getSimpleName()
					+ " envidados com sucesso.");
		} catch (Throwable e) {
			e.printStackTrace();
			String msg = "Erro ao enviar dados de: "
					+ this.entidade.getClass().getSimpleName();
			Log.e(msg, e.getMessage());
			
			if (e instanceof java.net.UnknownHostException) {
				erro = "Operação não realizada.Verifique a conexão de internet.";
			} else if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
				erro = "Operação não realizada. Verifique se houve perda de conexão com a internet.";
			} else {
				erro = e.getMessage();
			}
		}
		return resposta;
	}

	/**
	 * Resposta eperada: OK\n, por isso verifico se o tamanho da String é 3
	 */
	@Override
	protected void onPostExecute(String resultado) {
		try {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(erro != null) {
				Toast.makeText(contexto, erro,
						Toast.LENGTH_LONG).show();
				erro = null;
				return;
			}
			
			if(resultado.length() == 0) {
				Toast.makeText(contexto, "Sem dados para processar.",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (resultado.substring(0,2).trim().equalsIgnoreCase("OK")) {
				Toast.makeText(contexto, "Dados sincronizados com sucesso",
						Toast.LENGTH_LONG).show();

				if (contexto instanceof ExportarVendaActivity) {
//					String[] split = resultado.split("###");
//					long idVendaWeb = 0;
//					if(split.length > 1) {
//						idVendaWeb = Long.parseLong(split[1]);
//					}
					((ExportarVendaActivity) contexto)
							.removerVendaCompleta((Venda) entidade);
				}
				return;
			}
		} catch (Exception e) {
			Toast.makeText(contexto, "Detalhe do erro:" + e.getMessage(),
					Toast.LENGTH_LONG).show();
			return;
		}

		new TratarResposta(contexto).tratarResposta(resultado);

	}

	public boolean isSucesso() {
		return sucesso;
	}

	public Class<? extends IEntidade>[] getTipoAdapter() {
		return tipoAdapter;
	}

}
