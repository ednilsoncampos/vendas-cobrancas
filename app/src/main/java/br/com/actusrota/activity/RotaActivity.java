package br.com.actusrota.activity;

import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.dao.CidadeDAO;
import br.com.actusrota.dao.PessoaDAO;
import br.com.actusrota.dao.RotaDAO;
import br.com.actusrota.dao.TabelaPrecoDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.TabelaDePreco;
import br.com.actusrota.entidade.Usuario;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilArquivo;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class RotaActivity extends SuperActivity<Rota> {

	private String ROTA_SERVLET = "/actusrota/RotaServlet?";
	private String CIDADE_SERVLET = "/actusrota/CidadeServlet?";
	private String TABELA_PRECO_SERVLET = "/actusrota/TabelaPrecoServlet?";

	private ListView ltvRotas;
	// private RotaAdapter adapter;
	private List<Rota> rotas;
	private Rota rota;
	private RotaDAO rotaDAO;
	private CidadeDAO cidadeDAO;
	private PessoaDAO pessoaDAO;
	private ObterValor obterValor;

	public RotaActivity() {
		rotaDAO = new RotaDAO(this);
		cidadeDAO = new CidadeDAO(this);
		obterValor = new ObterValor(this);
		pessoaDAO = new PessoaDAO(this);
	}
	
	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.rt_btnAbrir:
			if (rota == null) {
				mostarMensagem("Selecione uma rota para prosseguir.");
				return;
			}
			criarActivityComParametro(CidadeActivity.class, rota);
			break;
		case R.id.rt_btnSincronizar:
			criarDialogSincronizarCidades();
			break;			
		}
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CIDADE_SERVLET += usuarioConexao + "&idRota=";
		TABELA_PRECO_SERVLET += usuarioConexao + "&idRota=";
		ROTA_SERVLET += usuarioConexao + "&cpf=";
		setContentView(R.layout.rota_layout);
//		criarComponentes();
		criarListView();
	}

	private void criarListView() {
		try {
			rotas = rotaDAO.getRotas();
			criarListViewRota(rotas);
			// criarAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_rota_cidade, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mrc_voltar:
			finish();
			return true;
			// case R.id.mr_pesquisar:
			// try {
			// listarRotas();
			// } catch (Exception e) {
			// tratarExibirErro(e);
			// }
			// return true;sincronizarCidades
		case R.id.mrc_sincronizar_rota:
			sincronizarRota();//obterValor.getValorEdit(R.id.rt_edtCPF)
			return true;
		case R.id.mrc_listarRotas:
			criarListView();
			return true;			
//		case R.id.mrc_sincronizar_cidade:
//			return criarDialogSincronizarCidades();
		case R.id.mrc_sincronizar_tabela_preco:
			if (rota == null) {
				mostarMensagem("Selecione uma rota para prosseguir.");
				return false;
			}
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(rota.getDescricao())
					.setMessage("Confirma importação da Tabela de Preço de Venda?")
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									sincronizarTabelaPrecoDeVenda();
								}

							}).setNegativeButton("Não", null).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean criarDialogSincronizarCidades() {
		if (rota == null) {
			mostarMensagem("Selecione uma rota para prosseguir.");
			return false;
		}
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(rota.getDescricao())
				.setMessage("Deseja realmente importar cidades?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sincronizarCidades();
							}

						}).setNegativeButton("Não", null).show();	
		return true;
	}

	private void criarListViewRota(List<Rota> rotas) {
		try {
			ltvRotas = (ListView) findViewById(R.id.rt_ltv_rota);
			ltvRotas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			Collections.sort(rotas);
			ArrayAdapter<Rota> adapter = new ArrayAdapter<Rota>(this,
					android.R.layout.simple_list_item_single_choice, rotas);
			ltvRotas.setAdapter(adapter);
			ltvRotas.setItemChecked(0, true);

			ltvRotas.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					rota = (Rota) parent.getItemAtPosition(position);
				}
			});

			ltvRotas.getCheckedItemPosition();
			if (ltvRotas.getCount() > 0)
				rota = (Rota) ltvRotas.getAdapter().getItem(0);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void apagarTodasRotas() {
		try {
			rotaDAO.deleteTodos();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

//	private void criarComponentes() {
//		try {
////			obterValor.adicionarMascara(R.id.rt_edtCPF, "###.###.###-##");
//			// obterValor.desabilitarTecladoVirtual(R.id.rt_edtCPF);
//
//			// ImageButton imageButton = (ImageButton)
//			// findViewById(R.id.rt_imgPesquisarRota);
//			//
//			// imageButton.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View arg0) {
//			// listarRotas();
//			// }
//			//
//			// });
//		} catch (Exception e) {
//			tratarExibirErro(e);
//		}
//	}

	// protected void atualizarTela() {
	// try {
	// // ltvRotas.destroyDrawingCache();
	// // ltvRotas.setVisibility(ListView.INVISIBLE);
	// // ltvRotas.setVisibility(ListView.VISIBLE);
	// adapter.setRotas(rotas);
	// adapter.atualizarRotas();
	//
	// // ltvRotas.setVisibility(ListView.INVISIBLE);
	// // ltvRotas.setVisibility(ListView.VISIBLE);
	// // ((ArrayAdapter<Rota>)
	// // ltvRotas.getAdapter()).notifyDataSetChanged();
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	// private void criarAdapter() {
	// try {
	// ltvRotas = (ListView) findViewById(R.id.rt_ltv_rota);
	// Collections.sort(rotas);
	// adapter = new RotaAdapter(this, rotas);
	// ltvRotas.setAdapter(adapter);
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	@SuppressWarnings("unchecked")
	public void sincronizarRota() {
		try {
			Usuario usuario = UtilArquivo.recuperarUsuario(this, UtilArquivo.NOME_ARQUIVO);
			if (usuario == null || usuario.getCpf() == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Usuário não registrado.");
				return;
			}
			boolean usarAdapter = false;
			new Importacao<Rota>(this, Rota.class, rotaDAO, ROTA_SERVLET
					+ UtilString.removerMaskCPF(usuario.getCpf()), true, false, usarAdapter)
					.execute();
			// adapter.atualizarRotas();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void sincronizarCidades() {
		try {
			boolean usarAdapter = false;
			new Importacao<Cidade>(this, Cidade.class, cidadeDAO,
					CIDADE_SERVLET + rota.getId(), true, false, usarAdapter)
					.execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void sincronizarTabelaPrecoDeVenda() {
		try {
			TabelaPrecoDAO tabelaPrecoDAO = new TabelaPrecoDAO(this);
			boolean esperaLisa = false;
			boolean usarAdapter = true;
			boolean excluirCamposSerializacao = false;
			new Importacao<TabelaDePreco>(this, TabelaDePreco.class,
					tabelaPrecoDAO, TABELA_PRECO_SERVLET + rota.getId(),
					esperaLisa, usarAdapter, excluirCamposSerializacao, ItemTabelaPreco.class)
					.execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// private void listarRotas() {
	// try {
	// String cpf = obterValor.getValorEdit(R.id.rt_edtCPF);
	// cpf = UtilString.removerMaskCPF(cpf);
	// if (UtilString.isValorInvalido(cpf)) {
	// UtilMensagem.mostarMensagemCurta(this,
	// "Informe o cpf para prosseguir.");
	// return;
	// }
	// if (cpf.length() < 11) {
	// mostarMensagem("O CPF deve conter 11 números.");
	// return;
	// }
	//
	// Pessoa pessoa = pessoaDAO
	// .pesquisarPor(cpf);
	//
	// if (pessoa == null) {
	// UtilMensagem.mostarMensagemLonga(this,
	// "Pessoa não encontrada com CPF informado:" + cpf + ".");
	// return;
	// }
	//
	// rotas = rotaDAO.getRotas(pessoa.getId());
	// if (rotas == null) {
	// mostarMensagem("Rotas não encontradas.");
	// return;
	// }
	// // criarAdapter();
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// // finally {
	// // try {
	// // if (rotaDAO.getFuncionarioDAO().isAberto())
	// // rotaDAO.getFuncionarioDAO().fecharBanco();
	// // } catch (Throwable e2) {
	// // tratarExibirErro(e2);
	// // }
	// // }
	//
	// }

}
