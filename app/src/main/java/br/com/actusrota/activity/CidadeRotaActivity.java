package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import br.com.actusrota.R;
import br.com.actusrota.dao.CidadeDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class CidadeRotaActivity extends SuperActivity<Cidade> {

	private Rota rota;
	private CidadeDAO cidadeDAO;
//	private RotaDAO rotaDAO;
	private ListView ltvCidades;
//	private CidadeAdapter adapter;
	private Cidade cidade;
	private ObterValor obterValor;

	public CidadeRotaActivity() {
		cidadeDAO = new CidadeDAO(this);
//		rotaDAO = new RotaDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cidade_layout);
		try {
//			buscarUltimaViagem();
//			if (rota == null) {
//				UtilMensagem.mostarMensagemLonga(this,
//						"Verifique se a viagem já foi importada.");
//				return;
//			}
			Long idViagem = cidadeDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
			if (idViagem == null || idViagem == 0) {
				UtilMensagem.mostarMensagemLonga(this,
						"Verifique se a viagem já foi importada.");
				return;
			} 
			criarComponentes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void buscarUltimaViagem() {
//		try {
//			Long idViagem = cidadeDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
//			if (idViagem == null || idViagem == 0) {
//				UtilMensagem.mostarMensagemLonga(this,
//						"Verifique se a viagem já foi importada.");
//				return;
//			} 
////			rota = rotaDAO.buscarPor(idViagem);
//		} catch (Exception e) {
//			tratarExibirErro("Erro a buscar viagem.", e);
//		}
//	}

	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("rota");
			
			// refatorar
			if(object == null) {
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
			cidade = (Cidade) ltvCidades.getAdapter().getItem(0);			
			
//			adapter = new CidadeAdapter(this, cidades);
//			ltvCidades.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Erro!", e.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_cidade_rota, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mc_voltar:
			finish();
			return true;
		case R.id.mcr_salvarAbrirVenda:
			criarTelaComParametroRota(
					ImportarVendaActivity.class, cidade);	
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.cid_btnPesquisar:
			pesquisarPorCodigo();
			break;
		}
	}

	private void pesquisarPorCodigo() {
		if (rota == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"Verifique se a viagem já foi importada.");
			return;
		}
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
		if (rota == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"Verifique se a viagem já foi importada.");
			return;
		}
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

	public void criarTelaComParametroRota(
			Class<? extends Activity> classeActivity, Cidade cidade) {
		try {
			Intent intent = new Intent(this, classeActivity);
			intent.putExtra("rota", rota);
			intent.putExtra("cidade", cidade);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro criarTelaRota:", e.getMessage());
		}
	}

}
