package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.actusrota.AcaoDialog;
import br.com.actusrota.Dialog;
import br.com.actusrota.R;
import br.com.actusrota.dao.ConexaoBancoWebDAO;
import br.com.actusrota.entidade.ConexaoBanco;
import br.com.actusrota.entidade.IEntidade;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemManter;
import br.com.actusrota.util.UtilMensagem;

public abstract class SuperActivity<T extends IEntidade> extends Activity {

	private ArrayAdapter<T> adapter;
	protected boolean dialogConfirmado;
	private ConexaoBancoWebDAO conexaoBancoWebDAO;
	protected String usuarioConexao = "usuarioConexao=";
	protected boolean ambienteDeTest = false; 

	public SuperActivity() {
		conexaoBancoWebDAO = new ConexaoBancoWebDAO(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buscarUsuarioConexao();
	}

	protected boolean isUsuarioNaoCadastrado() {
		ConexaoBanco conexao = conexaoBancoWebDAO
				.buscarUsuarioConexaoCadastrado();
		return conexao == null || conexao.isNovo();
	}

	protected void buscarUsuarioConexao() {
		try {
			ConexaoBanco conexao = conexaoBancoWebDAO
					.buscarUsuarioConexaoCadastrado();
			System.out.println(conexao);
			if (conexao != null) {
				if (usuarioConexao.length() < 16) {
					System.out.println(usuarioConexao.length());
					usuarioConexao = usuarioConexao
							+ conexao.getUsuarioConexao();
				}
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void criarActivityGenerica(Class<? extends Activity> classeActivity) {
		try {
			startActivity(new Intent(this, classeActivity));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro criarTela:" + classeActivity.getSimpleName(),
					e.getMessage());
		}
	}
	
	public void criarActivityComParametro(Class<? extends Activity> classeActivity, IEntidade entidade) {
		try {
			Intent intent = new Intent(this, classeActivity);
			intent.putExtra("parametro", entidade);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro criarTelaRota:", e.getMessage());
		}
	}
	
	public void criarActivityComParametros(Class<? extends Activity> classeActivity, Object... parametro) {
		try {
			Intent intent = new Intent(this, classeActivity);
			intent.putExtra("parametros", parametro);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro criarTelaRota:", e.getMessage());
		}
	}

	protected void criarAdapterItem(ArrayAdapter<ItemListView> adapter,
			int idListView) {
		try {
			final ListView listProdutos = (ListView) findViewById(idListView);
			listProdutos.setAdapter(adapter);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	protected void criarAdapter(ArrayAdapter<T> adapter, int idListView) {
		try {
			final ListView listProdutos = (ListView) findViewById(idListView);
			listProdutos.setAdapter(adapter);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	/**
	 * 
	 * @param dados
	 *            nï¿½o pode ser nulo
	 * @param id
	 */
	protected void criarAdapter(List<T> dados, int id) {
		if (dados == null) {
			Log.w("SuperActivity:criarAdapter", "Parametro dados nulo.");
			return;
		}
		try {
			ArrayAdapter<T> adapter = new ArrayAdapter<T>(this,
					R.layout.listview_fonte_menor, dados);

			final ListView listaAlunos = (ListView) findViewById(id);

			listaAlunos.setAdapter(adapter);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	protected void atualizarAdapter() {
		try {
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected List<ItemListView> castItemManterToItemListView(
			Collection<? extends ItemManter> itens) {
		List<ItemListView> itensView = new ArrayList<ItemListView>();
		for (ItemManter item : itens) {
			ItemListView itemView = new ItemListView(item);
			itemView.setQuantidade(item.getQuantidade());
			itensView.add(itemView);
		}
		return itensView;
	}

	protected void criarDialog(AcaoDialog acaoDialog, String titulo,
			String label) {
		try {
			new Dialog(acaoDialog, null, titulo, label);
		} catch (Exception e) {
			tratarExibirErro("Erro ao criar dialog:" + titulo, e);
		}
	}
	
	protected void tratarExibirErro(String mensagem, Throwable e) {
		e.printStackTrace();// log..
		Log.e("Erro!", e.getMessage());
		UtilMensagem.mostarMensagemLonga(this, mensagem + e.getMessage());
	}

	protected void tratarExibirErro(Throwable e) {
		if(e == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"Erro tratado, mas, não identificado.");
			return;
		}
		e.printStackTrace();// log..
		String causa = e.getCause()!=null ? e.getCause().getMessage() : e.getMessage();
		String message = e.getMessage();
		Log.e("Erro:", message);
		UtilMensagem.mostarMensagemLonga(this,
				"Detalhe do erro:" + message);
	}

	protected void mostarMensagem(String mensagem) {
		UtilMensagem.mostarMensagemLonga(this, mensagem);
	}

	protected void mostarMensagemCurta(String mensagem) {
		UtilMensagem.mostarMensagemCurta(this, mensagem);
	}

	public void confirmar(String perguntaDialog, final Object... parametros) {
		try {
			new AlertDialog.Builder(SuperActivity.this)
			.setMessage(perguntaDialog)
			.setCancelable(false)
			.setPositiveButton("Sim",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try {
								execute(parametros);
							} catch (Throwable e) {
								tratarExibirErro(e);
							}
						}
					}).setNegativeButton("Não", null).show();
		} catch (Throwable e) {
			tratarExibirErro("", e);
		}
	}

	/**
	 * deve ser sobrescrito
	 * 
	 * @param parametros
	 */
	protected void execute(Object... parametros) {
	}
	
	protected void criarDialogSalvar() {
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Salvando registro")
		.setMessage("Deseja realmente salvar?")
		.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						salvar();
					}

				}).setNegativeButton("Não", null).show();
	}
	
	// existem activities que não utilizam o metodo salvar, poderia ser uma interface
	protected void salvar() {
		
	}
	
	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			return b.get("parametro");
		} catch (NullPointerException e) {
			// esperado, esta chamada getIntent().getExtras() pode retornar null
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}
}
