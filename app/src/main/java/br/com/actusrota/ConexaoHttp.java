package br.com.actusrota;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import br.com.actusrota.activity.LoginActivity;
import br.com.actusrota.entidade.IEntidade;
import br.com.actusrota.enumerador.EnumMetodoDeEnvio;
import br.com.actusrota.util.EntidadeCastJson;

public class ConexaoHttp {

	// private static final String URL_PRODUCAO = "http://10.10.0.10:8080";//UEG
//	private static final String URL_PRODUCAO = "http://10.0.2.2:8080";//LOCALHOST
	private static final String URL_PRODUCAO = "http://www.actusrota.com.br";
	private Class<? extends IEntidade>[] tipoAdapter;
	private final boolean usarAdapter;
	private final EnumMetodoDeEnvio enviarDados;
	private Integer httpStatus = 0;
	private boolean esperaLisa;

	public ConexaoHttp(EnumMetodoDeEnvio enviarDados, boolean esperaLisa,
			boolean usarAdapter, Class<? extends IEntidade>... tipoAdapter) {
		this.usarAdapter = usarAdapter;
		this.tipoAdapter = tipoAdapter;
		this.enviarDados = enviarDados;
		this.esperaLisa = esperaLisa;
	}

	public ConexaoHttp(boolean esperaLisa, boolean usarAdapter,
			Class<? extends IEntidade>... tipoAdapter) {
		this.usarAdapter = usarAdapter;
		this.tipoAdapter = tipoAdapter;
		this.enviarDados = EnumMetodoDeEnvio.GET;
		this.esperaLisa = esperaLisa;
	}

	private boolean isAdapterNaoInformado() {
		return tipoAdapter == null || tipoAdapter.length == 0;
	}

	public String importarDadosJson(String servlet)
			throws ClientProtocolException, IOException {

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(montarURLComConexao(servlet));

		HttpResponse response = httpClient.execute(httpGet);

		String json = validarResposta(response);

		return json;

	}

	public <T extends IEntidade> List<T> converterJson(Class<T> classe,
			String json, boolean excluirCamposSerializacao)
			throws ClientProtocolException, IOException {

		List<T> dados = null;
		if (usarAdapter || !isAdapterNaoInformado()) {
			if (esperaLisa) {
				dados = EntidadeCastJson.jsonToListAdapter(json, classe,
						excluirCamposSerializacao, tipoAdapter);
			} else {
				dados = new ArrayList<T>();
				dados.add(EntidadeCastJson.jsonToEntidadeAdapter(json, classe,
						excluirCamposSerializacao, tipoAdapter));
			}

		} else {
			if (esperaLisa) {
				dados = EntidadeCastJson.jsonToList(json, classe);
			} else {
				dados = new ArrayList<T>();
				EntidadeCastJson.jsonToEntidade(json, classe);
			}
		}

		return dados;

	}

	public <T extends IEntidade> String enviarDados(String servlet, T entidade,
			boolean excluirCamposSerializacao) throws ClientProtocolException,
			IOException {
		if (enviarDados.equals(EnumMetodoDeEnvio.GET)) {
			return enviarDadosGet(servlet, entidade, excluirCamposSerializacao);
		} else {
			return enviarDadosPost(servlet, entidade, excluirCamposSerializacao);
		}
	}

	private <T extends IEntidade> String enviarDadosGet(String servlet,
			T entidade, boolean excluirCamposSerializacao)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;

		InputStream is = null;

		try {
			String json = null;
			if (usarAdapter || !isAdapterNaoInformado()) {
				json = EntidadeCastJson.entidadeToJsonAdapter(entidade,
						excluirCamposSerializacao, tipoAdapter);
			} else {
				json = EntidadeCastJson.entidadeToJson(entidade);
			}

			// String encode = URL_PRODUCAO + servlet +"?json="+
			// URLEncoder.encode(json);
			String encode = URL_PRODUCAO + servlet + "?json=" + json;
			if (LoginActivity.NOME_EMPRESA != null) {
				encode += "&con=" + LoginActivity.NOME_EMPRESA + "&movel=true";
			}
			Log.i("envio", encode);

			HttpGet httpGet = new HttpGet(encode);
			httpGet.addHeader(new BasicHeader("Accept", "application/json"));
			httpGet.addHeader(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			// HttpGet httpGet = new HttpGet(urlFinal);

			try {
				response = httpClient.execute(httpGet);
			} catch (java.net.UnknownHostException e) {
				if (e instanceof java.net.UnknownHostException) {
					throw new RuntimeException(
							"Operação não realizada. Verifique a conexão de internet.");
				} else {
					throw new RuntimeException(e);
				}
			}

			return validarResposta(response);
		} finally {
			if (is != null)
				is.close();
		}
	}

	private <T extends IEntidade> String enviarDadosPost(String servlet,
			T entidade, boolean excluirCamposSerializacao)
			throws ClientProtocolException, IOException {

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);

		HttpResponse response = null;

		HttpPost post = new HttpPost(montarURLComConexao(servlet));

		String json = null;
		if (usarAdapter || !isAdapterNaoInformado()) {
			json = EntidadeCastJson.entidadeToJsonAdapter(entidade,
					excluirCamposSerializacao, tipoAdapter);
		} else {
			json = EntidadeCastJson.entidadeToJson(entidade);
		}

		StringEntity se = new StringEntity(json, HTTP.UTF_8);
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		post.setEntity(se);

		try {
			response = client.execute(post);
		} catch (Throwable e) {
			if (e instanceof java.net.UnknownHostException) {
				throw new RuntimeException(
						"Operação não realizada.Verifique a conexão de internet.");
			} else if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
				throw new RuntimeException(
						"Operação não realizada. Verifique se houve perda de conexão com a internet.");
			} else {
				throw new RuntimeException(e);
			}
		}

		return validarResposta(response);
	}

	private String lerImportacao(HttpResponse response) {
		if (response == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		String contentCharSet = EntityUtils.getContentCharSet(response
				.getEntity());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"), 8);

			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	private String validarResposta(HttpResponse response) {
		if (response == null) {
			return "";
		}
		String resposta = "";
		try {
			String contentCharSet = EntityUtils.getContentCharSet(response
					.getEntity());
			resposta = EntityUtils.toString(response.getEntity());
			httpStatus = response.getStatusLine().getStatusCode();
			String reasonPhrase = response.getStatusLine().getReasonPhrase();
			Log.i("Content Type:", contentCharSet);
			Log.i("Status Http:", httpStatus + "");
			Log.i("Mensagem Status Http:", reasonPhrase);
			Log.i("Resposta:", resposta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resposta;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	private String montarURLComConexao(String servlet) {
		String url = null;
		if (LoginActivity.NOME_EMPRESA != null) {
			if (servlet.contains("?")) {
				url = URL_PRODUCAO + servlet + "&con="
						+ LoginActivity.NOME_EMPRESA + "&movel=true";
			} else {
				url = URL_PRODUCAO + servlet + "?con="
						+ LoginActivity.NOME_EMPRESA + "&movel=true";
			}
		} else {
			url = URL_PRODUCAO + servlet;
		}

		return url;
	}
	
	private void teste() {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://www.actusrota.com.br/actusrota/LoginServlet");
			HttpResponse response = httpClient.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			System.out.println(string);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
	}

}
