package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.actusrota.R;
import br.com.actusrota.dao.ClienteDAO;
import br.com.actusrota.dao.RotaDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.tabela.TabelaCliente;
import br.com.actusrota.tabela.TabelaVenda;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.PesquisaLayout;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ClienteActivity extends SuperActivity<Cliente> {

	private ClienteDAO clienteDAO;
	private ObterValor obterValor;

	private RotaDAO rotaDAO;
//	private Rota rota;
	private Cidade cidade;
	private ListView ltvClientes;// pes_ltv_dados
	private Cliente cliente;

	// private ClienteAdapter adapter;

	public ClienteActivity() {
		clienteDAO = new ClienteDAO(this);
		rotaDAO = new RotaDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pesquisa_layout);
		try {
			obeterParametrosActivity();
			criarComponentes();
//			buscarUltimaViagem();
			listarClientesPorRota();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_pesq_cliente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mc_voltar:
			finish();
			return true;
		case R.id.mcp_nova_venda:
			cliente.setCidadeRota(cidade);
			criarActivityComParametro(VendaActivity.class, cliente);
			return true;
		case R.id.mcp_viagem:
			criarActivityGenerica(ViagemActivity.class);
			return true;			
			// case R.id.mcp_sincronizar:
			// sincronizarClientes();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("parametro");
			
			// refatorar
			if(object == null) {
				object = b.get("cidade");
			}
			
			if (object != null)
				cidade = (Cidade) object;
		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	private void excluir() {
		try {
			// clienteDAO.deleteTodos();
			clienteDAO.deleteTodos(TabelaCliente.TABELA_CLIENTE);
			clienteDAO.deleteTodos(TabelaVenda.TABELA_VENDA);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

//	private void buscarUltimaViagem() {
//		long idViagem = clienteDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
//		try {
//			rota = rotaDAO.buscarPor(idViagem);
//		} catch (Exception e) {
//			tratarExibirErro("Erro a buscar viagem.", e);
//		}
//	}

	/**
	 * carregado somente no inicio as pesquisas podem ser feitas sem o filtro da
	 * rota
	 */
	private void listarClientesPorRota() {
		try {
			if (cidade == null || cidade.isNovo()) {
				mostarMensagem("Cidade não encontrada");
				return;
			}
			criarAdapterCliente(clienteDAO.listarClientesPor(cidade.getId()));
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		new AdicionarListener(obterValor).adicionarListener(new EventoClique(),
				PesquisaLayout.btnPesquisar);

		new AdicionarListener(obterValor).adicionarListener(
				new EventoEditText(), PesquisaLayout.editNome);
		
		obterValor.setTextoTextView("Cidade: "+cidade.toString(), PesquisaLayout.textViewTitulo);

		obterValor.setTextoTextView("Nome / Cidade", PesquisaLayout.labelNome);
	}

	class EventoClique implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			pesquisarPorCodigo();
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
					criarAdapterCliente(new ArrayList<Cliente>());
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

	private void criarAdapterCliente(List<Cliente> clientes) {
		if (clientes == null)
			clientes = new ArrayList<Cliente>(0);
		else
			Collections.sort(clientes);

		// adapter = new ClienteAdapter(this, clientes);
		// criarAdapter(adapter, PesquisaLayout.listViewDados);

		try {
			ltvClientes = (ListView) findViewById(PesquisaLayout.listViewDados);
			ltvClientes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this,
					R.layout.listview_simples_selecao_fonte_menor, clientes);

			ltvClientes.setAdapter(adapter);
			ltvClientes.setItemChecked(0, true);

			ltvClientes.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					cliente = (Cliente) parent.getItemAtPosition(position);
				}
			});

			ltvClientes.getCheckedItemPosition();
			if (ltvClientes.getCount() > 0)
				cliente = (Cliente) ltvClientes.getAdapter().getItem(0);
		} catch (Exception e) {
			tratarExibirErro(e);
		}

	}

	public void pesquisarLike(String nome) {
		String valorEdit = obterValor.getValorEdit(PesquisaLayout.editNome);
		try {
			pesquisarPorCodigo(Integer.parseInt(valorEdit));
			return;
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		try {
			List<Cliente> clientes = clienteDAO.pesquisarLike(nome);
			if (clientes == null) {
				clientes = new ArrayList<Cliente>();
				Log.w("pesquisarLike:", "lista de clientes null");
			}
			criarAdapterCliente(clientes);
			// obterValor.setTextoTextView("Nome / Cidade",
			// PesquisaLayout.labelNome);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void pesquisarPorCodigo() {
		try {
			String valorEdit = obterValor.getValorEdit(PesquisaLayout.editNome);

			if (UtilString.isValorInvalido(valorEdit)) {
				mostarMensagem("O código deve ser informado.");
				return;
			}

			int codigo = Integer.parseInt(valorEdit);
			pesquisarPorCodigo(codigo);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void pesquisarPorCodigo(int codigo) {
		try {
			List<Cliente> clientes = new ArrayList<Cliente>();
			Cliente cliente = clienteDAO.consultarPorId(codigo);

			if (cliente == null) {
				UtilMensagem.mostarMensagemLonga(this,
						"Cliente não encontrado.");
			} else {
				clientes.add(cliente);
			}

			criarAdapterCliente(clientes);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numérico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// private void sincronizarClientes() {
	// try {
	// boolean usarAdapter = false;
	// new Importacao<Cliente>(ClienteActivity.this, Cliente.class,
	// clienteDAO, CLIENTE_SERVLET, true, false, usarAdapter)
	// .execute();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
