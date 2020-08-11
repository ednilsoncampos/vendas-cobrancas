package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import br.com.actusrota.Importacao;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ImportarVendaAdapter;
import br.com.actusrota.dao.ClienteDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.Usuario;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.enumerador.EnumStatusVenda;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilArquivo;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ImportarVendaActivity extends SuperActivity<Venda> {
	private String EXPORTAR_VENDA_SERVLET = "/actusrota/ExportarVendaServlet?";
	private final String paramIdRota = "&idRota=";
	private final String paramIdCidade = "&idCidade=";
//	private final String paramStatusVenda = "&statusVenda=";
	private String paramNumVenda = "&numVendaWeb=";
	private String paramCPFResponsavel = "&CPFResponsavel=";

	private VendaDAO vendaDAO;

	private ObterValor obterValor;
	private ImportarVendaAdapter vendaAdapter;
	private EnumStatusVenda statusVenda;

	private Cidade cidade;

	private Rota rota;
	private ClienteDAO clienteDAO;
	private Usuario usuario;

	public ImportarVendaActivity() {
		vendaDAO = new VendaDAO(this);
		clienteDAO = new ClienteDAO(this);
		obterValor = new ObterValor(this);
		statusVenda = EnumStatusVenda.ABERTA;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EXPORTAR_VENDA_SERVLET += usuarioConexao;
		setContentView(R.layout.importar_venda_layout);
		try {
			obterParametros();
			criarComponentes();
			criarAdapter(new ArrayList<Venda>());
			usuario = UtilArquivo.recuperarUsuario(this, UtilArquivo.NOME_ARQUIVO);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_importar_venda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnipv_importar_venda:
			importarVendas();
			return true;
		case R.id.mnipv_voltar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean isSemCidadeOuRota() {
		return cidade == null || cidade.isNovo() || rota == null
				|| rota.isNovo();
	}

	private void obterParametros() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("parametros");
			if(object != null) {
				Object[] array = (Object[]) object;
				rota = (Rota) array[0];
				cidade = (Cidade) array[1];
				obterValor.setTextoTextView(rota.getDescricao(), R.id.ipv_txvRota);
				obterValor.setTextoTextView(cidade.getNome(), R.id.ipv_txvCidade);
				return;
			}
			
			object = b.get("cidade");
			if (object != null) {
				cidade = (Cidade) object;
				obterValor.setTextoTextView(cidade.getNome(), R.id.ipv_txvCidade);
			}
			
			if(cidade == null) {
				object = b.get("parametro");
				if (object != null) {
					cidade = (Cidade) object;
					obterValor.setTextoTextView(cidade.getNome(), R.id.ipv_txvCidade);
				}
			}

			Object objRota = b.get("rota");
			if (object != null)
				rota = (Rota) objRota;
		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		// new AdicionarListener(obterValor).adicionarListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// pesquisarPorCodigo();
		// }
		// }, R.id.ipv_btnPesquisar);

		if (!isSemCidadeOuRota()) {
//			obterValor.setTextoTextView(rota.getDescricao(), R.id.ipv_txvRota);
//			obterValor.setTextoTextView(cidade.getNome(), R.id.ipv_txvCidade);
		}

		// new AdicionarListener(obterValor).adicionarListener(
		// new EventoEditText(), R.id.ipv_edtNome);
	}

	private List<Venda> carregarVendaSemItens() {
		return vendaDAO.carregarVendaSemItens(cidade.getId(), rota.getId(),
				statusVenda);
	}

	/**
	 * 1 - Salvar cliente e endereï¿½o ao importar a venda 2 - O servidor não deve
	 * permitir importar venda que jï¿½ tenha idMovel, a venda com idMovel jï¿½
	 * poderia ter sido apagada do banco e isso causaria problemas. Ex.: - Uma
	 * venda com idWeb = 1 e sem idMovel pode ser importada o SQLite geraria um
	 * novo idMovel - Uma venda com idWeb = 2 e com idMovel = 3 não poderia ser
	 * importada se a sequï¿½ncia do SQLite estivesse com o valor 4, por exemplo,
	 * e a venda de idMovel jï¿½ estivesse apagada. 3 - Viagem ainda não
	 * importada, serï¿½ comum nas importaï¿½ï¿½es. O que fazer neste caso??
	 */
	@SuppressWarnings("unchecked")
	private void importarVendas() {
		if(isInValido())
			return;
		try {
			boolean usarAdapter = true;
			StringBuilder urlFinal = new StringBuilder(EXPORTAR_VENDA_SERVLET);
			urlFinal.append(paramIdRota);
			urlFinal.append(rota.getId());

			urlFinal.append(paramIdCidade);
			urlFinal.append(cidade.getId());
			
			if (usuario == null || usuario.getCpf() == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Usuário não registrado.");
				return;
			}
			
			urlFinal.append(paramCPFResponsavel);
			urlFinal.append(UtilString.removerMaskCPF(usuario.getCpf()).trim());

//			urlFinal.append(paramStatusVenda);
//			urlFinal.append(statusVenda);

			new Importacao<Venda>(this, Venda.class, vendaDAO,
					urlFinal.toString(), true, usarAdapter, true, Viagem.class)
					.execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// class EventoEditText implements TextWatcher {
	//
	// @Override
	// public void afterTextChanged(Editable arg0) {
	//
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	// int arg3) {
	//
	// }
	//
	// /**
	// * Pesquisa a cada 3 caracteres digitados
	// */
	// @Override
	// public void onTextChanged(CharSequence textoDigitado, int arg1,
	// int arg2, int arg3) {
	// if (textoDigitado.length() < 3) {
	// if (textoDigitado.length() == 0) {
	// criarAdapter(new ArrayList<Venda>());
	// }
	// return;
	// }
	// if (textoDigitado.length() == 3) {
	// pesquisarLike(textoDigitado.toString());
	// return;
	// }
	// if (textoDigitado.length() > 3) {
	// if (textoDigitado.length() % 3 == 0)
	// pesquisarLike(textoDigitado.toString());
	// return;
	// }
	// }
	//
	// }

	// public void pesquisarLike(String nomeCliente) {
	// try {
	// List<Venda> vendas = vendaDAO.pesquisarLike(nomeCliente);
	// if (vendas == null) {
	// vendas = new ArrayList<Venda>();
	// Log.w("pesquisarLike:", "lista de vendas null");
	// }
	// criarAdapter(vendas);
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	// public void pesquisarPorCodigo() {
	// try {
	// String valorEdit = obterValor.getValorEdit(R.id.ipv_edtNome);
	//
	// if (UtilString.isValorInvalido(valorEdit)) {
	// mostarMensagem("Informe o cï¿½digo para prosseguir.");
	// return;
	// }
	//
	// int idVenda = Integer.parseInt(valorEdit);
	// List<Venda> vendas = new ArrayList<Venda>();
	// Venda venda = vendaDAO.consultarPorId(idVenda);
	//
	// if (venda == null) {
	// UtilMensagem.mostarMensagemLonga(this, "Venda não encontrada.");
	// } else {
	// vendas.add(venda);
	// }
	//
	// criarAdapter(vendas);
	// } catch (NumberFormatException e) {
	// UtilMensagem.mostarMensagemLonga(this,
	// "O cï¿½digo deve ser numï¿½rico.");
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	private void criarAdapter(List<Venda> vendas) {
		if (vendas == null)
			vendas = new ArrayList<Venda>();
		vendaAdapter = new ImportarVendaAdapter(this, vendas,
				"Deseja realmente enviar a venda?");
		criarAdapter(vendaAdapter, R.id.ipv_ltv_dados);
	}

	public void eventoCliqueRadioButton(View view) {
		boolean selecionadio = ((RadioButton) view).isChecked();
		RadioButton rdbAberta = (RadioButton) findViewById(R.id.ipv_radioAberta);
		RadioButton rdbRecobranca = (RadioButton) findViewById(R.id.ipv_radioRecobranca);
		RadioButton rdbAtrasada = (RadioButton) findViewById(R.id.ipv_radioAtrasada);

		switch (view.getId()) {
		case R.id.ipv_radioAberta:
			if (selecionadio) {
				statusVenda = EnumStatusVenda.ABERTA;
				rdbAberta.setChecked(true);
				rdbRecobranca.setChecked(false);
				rdbAtrasada.setChecked(false);
			}
			break;
		case R.id.ipv_radioRecobranca:
			if (selecionadio) {
				statusVenda = EnumStatusVenda.EM_COMPENSACAO;
				rdbRecobranca.setChecked(true);
				rdbAberta.setChecked(false);
				rdbAtrasada.setChecked(false);
			}
			break;
		case R.id.ipv_radioAtrasada:
			if (selecionadio) {
				statusVenda = EnumStatusVenda.CALOTE;
				rdbAtrasada.setChecked(true);
				rdbAberta.setChecked(false);
				rdbRecobranca.setChecked(false);
			}
			break;

		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.ipv_btnPesquisar:
			pesquisarPorStatusSincronizado();
			break;
		case R.id.ipv_btnImportar:
			if(obterValor.isInValidoValorEdit(R.id.ipv_edtNumVenda)) {
				mostarMensagem("O número da venda deve ser informado.");
				return;
			}
			criarDialogImportarVenda();
			break;
		}
	}

	private void criarDialogImportarVenda() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle("Confirma importação da venda?");


		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(obterValor.isInValidoValorEdit(R.id.ipv_edtNumVenda)) {
							mostarMensagem("Informe o número da venda.");
							return;
						}
						importarVendaPorNumero(Long.valueOf(obterValor.getValorEdit(R.id.ipv_edtNumVenda)));
					}
				})
				.setNegativeButton("Não",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();		
	}
	
	@SuppressWarnings("unchecked")
	private void importarVendaPorNumero(long idVenda) {
		if(isInValido())
			return;
		try {
			
			if (usuario == null || usuario.getCpf() == null) {
				UtilMensagem.mostarMensagemCurta(this,
						"Usuário não registrado.");
				return;
			}
			
			boolean usarAdapter = true;
			StringBuilder urlFinal = new StringBuilder(EXPORTAR_VENDA_SERVLET);
			
			urlFinal.append(paramIdRota);
			urlFinal.append(rota.getId());
			
			urlFinal.append(paramIdCidade);
			urlFinal.append(cidade.getId());
			
			urlFinal.append(paramNumVenda);
			urlFinal.append(idVenda);

			urlFinal.append(paramCPFResponsavel);
			urlFinal.append(UtilString.removerMaskCPF(usuario.getCpf()).trim());
			
			boolean esperaLista = false;
			new Importacao<Venda>(this, Venda.class, vendaDAO,
					urlFinal.toString(), esperaLista, usarAdapter, true, Viagem.class, Rota.class)
					.execute();
		} catch (Exception e) {
			tratarExibirErro(e);
		}		
	}
	
	private boolean isInValido() {
		if (isSemCidadeOuRota()) {
			mostarMensagem("Rota e/ou cidade não foram informadas.");
			return true;
		}
		try {
			Long buscarMaxID = clienteDAO.buscarMaxID();
			if (buscarMaxID == null || buscarMaxID <= 0) {
				mostarMensagem("Não há clientes cadastrados.");
				return true;
			}
		} catch (Exception e) {
			tratarExibirErro(e);
			return true;
		}
		return false;
	}

	private void pesquisarPorStatusSincronizado() {
		try {
			List<Venda> vendas = carregarVendaSemItens();
			if (vendas == null || vendas.isEmpty()) {
				mostarMensagem("Venda(s) não encontrada(s).");
			}
			criarAdapter(vendas);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
}
