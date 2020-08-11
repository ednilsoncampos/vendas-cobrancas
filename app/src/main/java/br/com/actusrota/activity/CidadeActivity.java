package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.dao.CidadeDAO;
import br.com.actusrota.dao.ClienteDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class CidadeActivity extends SuperActivity<Cidade> {

	private String CLIENTE_SERVLET = "/actusrota/ClienteServlet?";
	private ClienteDAO clienteDAO;
	private Rota rota;
	private CidadeDAO cidadeDAO;
//	private RotaDAO rotaDAO;
	private ListView ltvCidades;
	// private CidadeAdapter adapter;
	private Cidade cidade;
	private ObterValor obterValor;

	public CidadeActivity() {
		cidadeDAO = new CidadeDAO(this);
		clienteDAO = new ClienteDAO(this);
//		rotaDAO = new RotaDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CLIENTE_SERVLET += usuarioConexao + "&idCidade=";
		setContentView(R.layout.cidade_layout);
		try {
			obeterParametrosActivity();

//			if (rota == null)
//				buscarUltimaViagem();
			// se ainda estiver null
//			if (rota == null) {
//				UtilMensagem
//						.mostarMensagemLonga(
//								this,
//								"Rota de Trabalho não importada. Verifique se todas as Rotas foram importadas ou Sincronize a Viagem para importá-la.");
//				return;
//			}
			criarComponentes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void buscarUltimaViagem() {
//		long idViagem = cidadeDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
//		try {
//			rota = rotaDAO.buscarPor(idViagem);
//		} catch (Exception e) {
//			tratarExibirErro("Erro a buscar viagem.", e);
//		}
//	}

	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("rota");

			// refatorar
			if (object == null) {
				object = b.get("parametro");
			}

			if (object != null)
				rota = (Rota) object;
		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	private void criarComponentes() {
		new AdicionarListener(obterValor).adicionarListener(
				new EventoEditText(), R.id.cid_edtNome);

		TextView txvRota = (TextView) findViewById(R.id.cid_txv_rota);
		txvRota.setText(rota.getDescricao());

		criarListViewCidades(cidadeDAO.listarCidadesPor(rota));
	}

//	View row = null;

	private void criarListViewCidades(List<Cidade> cidades) {
		try {
			ltvCidades = (ListView) findViewById(R.id.cid_ltv_cidade);
			ltvCidades.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			Collections.sort(cidades);
			ArrayAdapter<Cidade> adapter = new ArrayAdapter<Cidade>(this,
					android.R.layout.simple_list_item_single_choice, cidades);
			ltvCidades.setAdapter(adapter);
			ltvCidades.setItemChecked(0, true);

			ltvCidades.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					cidade = (Cidade) parent.getItemAtPosition(position);
				}
			});

			ltvCidades.getCheckedItemPosition();
			if (ltvCidades.getCount() > 0)
				cidade = (Cidade) ltvCidades.getAdapter().getItem(0);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_cidade, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if(adapter.getCidade() == null) {
		// if (cidade == null) {
		// mostarMensagem("Selecione uma Cidade para prosseguir.");
		// return false;
		// }
		switch (item.getItemId()) {
		case R.id.mcid_voltar:
			finish();
			return true;
			// case R.id.mcid_sincronizarCliente:
			// if (cidade == null) {
			// mostarMensagem("Selecione uma cidade para prosseguir.");
			// return false;
			// }
			// new AlertDialog.Builder(this)
			// .setIcon(android.R.drawable.ic_dialog_alert)
			// .setTitle(cidade.toString())
			// .setMessage("Deseja realmente importar clientes?")
			// .setPositiveButton("Sim",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// sincronizarClientes();
			// }
			//
			// }).setNegativeButton("Não", null).show();
			// return true;
		case R.id.mcid_NovoCliente:
			if (cidade == null) {
				mostarMensagem("Selecione uma cidade para prosseguir.");
				return false;
			}
			cidade.setRota(rota);
			criarActivityComParametro(CadastroClienteActivity.class, cidade);
			return true;
			// case R.id.mcid_clientes:
			// if (cidade == null) {
			// mostarMensagem("Selecione uma cidade para prosseguir.");
			// return false;
			// }
			// criarActivityComParametro(ClienteActivity.class, cidade);
			// return true;
		case R.id.mcid_importarVenda:
			if (cidade == null) {
				mostarMensagem("Selecione uma cidade para prosseguir.");
				return false;
			}
			Object[] array = new Object[] { rota, cidade };
			criarActivityComParametros(ImportarVendaActivity.class, array);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("unchecked")
	public void sincronizarClientes() {
		try {
			boolean usarAdapter = false;
			clienteDAO.setIdCidade(cidade.getId());
			new Importacao<Cliente>(this, Cliente.class, clienteDAO,
					CLIENTE_SERVLET + this.cidade.getId(), true, false,
					usarAdapter).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.cid_btnPesquisar:
			pesquisarPorCodigo();
			break;
		case R.id.cid_btnAbrir:
			if (cidade == null) {
				mostarMensagem("Selecione uma cidade para prosseguir.");
				return;
			}
			cidade.setRota(rota);
			criarActivityComParametro(ClienteActivity.class, cidade);
			break;
		case R.id.cid_btnSincronizar:
			criarDialogSincronizarClientes();
			break;
		}
	}

	private void criarDialogSincronizarClientes() {
		if (cidade == null) {
			mostarMensagem("Selecione uma cidade para prosseguir.");
			return;
		}
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(cidade.toString())
				.setMessage("Deseja realmente importar clientes?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sincronizarClientes();
							}

						}).setNegativeButton("Não", null).show();
	}

	private void pesquisarPorCodigo() {
		try {
			String valorEdit = obterValor.getValorEdit(R.id.cid_edtNome);

			if (UtilString.isValorInvalido(valorEdit)) {
				mostarMensagem("O código deve ser informado.");
				return;
			}

			int codigo = Integer.parseInt(valorEdit);
			Cidade cidade = cidadeDAO.consultarPorId(codigo);
			List<Cidade> cidades = new ArrayList<Cidade>();
			if (cidade == null) {
				UtilMensagem
						.mostarMensagemLonga(this, "Cidade não encontrada.");
			} else {
				cidades.add(cidade);
			}
			// criarAdapter(cidades, R.id.cid_ltv_cidade);
			criarListViewCidades(cidades);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void pesquisarLike(String nome) {
		try {
			List<Cidade> cidades = cidadeDAO.pesquisarLikePor(rota, nome);
			;
			if (cidades == null) {
				cidades = new ArrayList<Cidade>();
				Log.w("pesquisarLike:", "lista de cidades nula");
			}
			// criarAdapter(clientes, R.id.cid_ltv_cidade);
			criarListViewCidades(cidades);
		} catch (Exception e) {
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

}
