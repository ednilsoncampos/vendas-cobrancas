package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import br.com.actusrota.AcaoDialog;
import br.com.actusrota.CalendarioData;
import br.com.actusrota.Dialog;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ItemSelecionadoAdapter;
import br.com.actusrota.dao.CidadeDAO;
import br.com.actusrota.dao.ClienteDAO;
import br.com.actusrota.dao.RotaDAO;
import br.com.actusrota.dao.TabelaPrecoDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemParcelable;
import br.com.actusrota.entidade.ItemVenda;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.TabelaDePreco;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.tabela.TabelaVenda;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.Mascara;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilDinheiro;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class VendaActivity extends SuperActivity<Venda> implements AcaoDialog {

	private ClienteDAO clienteDAO;
	private VendaDAO vendaDAO;
	private Cliente cliente;
	private Venda venda;
	private long idViagem;
	private static final int INTERVALO_COBRANCA = 2;
	private static final String LABEL = "Nº Venda:";
	private CalendarioData calendario;
	private ObterValor obterValor;
	private RotaDAO rotaDAO;
	private Rota rota;
	private TabelaPrecoDAO tabelaPrecoDAO;
	private TabelaDePreco tabela;
	private CidadeDAO cidadeDAO;

	public VendaActivity() {
		vendaDAO = new VendaDAO(this);
		clienteDAO = new ClienteDAO(this);
		cidadeDAO = new CidadeDAO(this);
		rotaDAO = new RotaDAO(this);
		tabelaPrecoDAO = new TabelaPrecoDAO(this);
		venda = new Venda();
		venda.criarItensVenda();
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.venda_layout);
		try {
			buscarUltimaViagem();
			// buscarRotaUltimaViagem();

			if (idViagem == 0) {
				mostarMensagem("Viagem não importada. Por favor importe.");
				// criarActivityGenerica(ViagemActivity.class);
				return;
			}

			obeterParametrosActivity();// deve ser chamado antes de criar
										// componentes

			// Cidade cidade =
			// cidadeDAO.consultarPorId(cliente.getCidade().getId());
			if (cliente.getCidadeRota() == null) {
				mostarMensagem("Cidade não encontrada");
				return;
			}

			if (cliente.getCidadeRota().getRota() == null) {
				mostarMensagem("Rota não encontrada");
				return;
			}
			rota = cliente.getCidadeRota().getRota();
			buscarTabelaPreco();
			criarComponentes();

			if (idViagem <= 0) {
				UtilMensagem.mostarMensagemLonga(this,
						"Viagem não encotrada. Por favor importe a Viagem.");
				return;
			}

			if (venda == null || venda.isNovo())
				setarDadosCliente();
			else
				setarDadosTelaVenda();

			// if(ambienteDeTeste)
			// vendaDAO.deleteTodos();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void buscarTabelaPreco() {
		try {
			tabela = tabelaPrecoDAO.pesquisarPor(rota.getId());
			isTabelaDePrecoNaoImportada();
		} catch (Throwable e) {
			tratarExibirErro(e);
		}
	}

	private boolean isTabelaDePrecoNaoImportada() {
		if (tabela == null) {
			mostarMensagem("Tabela de preço não importada para Rota: "
					+ rota.getDescricao());
			mostarMensagem("Por favor, abra a tela de Rota para importá-la.");
			return true;
		}
		return false;
	}

	private void criarComponentes() {
		try {
			calendario = new CalendarioData(VendaActivity.this);

			final EditText edtCpf = (EditText) findViewById(R.id.vnd_edtCPF);
			edtCpf.setInputType(InputType.TYPE_NULL);
			obterValor.adicionarMascara(edtCpf, "###.###.###-##");
			// obterValor.desabilitarTecladoVirtual(R.id.vnd_edtCPF);

			EditText edtData = obterValor.getEditText(R.id.vnd_edtDtVencimento);
			edtData.addTextChangedListener(Mascara
					.insert("##/##/####", edtData));

			edtData.setOnFocusChangeListener(new OnFocusChangeListener() {

				public void onFocusChange(View v, boolean hasFocus) {
					System.out.println("foco dt Vencimento:" + hasFocus);
					setarDataVencimento();
				}
			});

			// setarDataVencimento();

			if (idViagem <= 0) {
				return;
			}
			obterValor.getTextView(R.id.vnd_txvNumViagem).setText(
					"Nº viagem:" + String.valueOf(idViagem));
			obterValor.getEditText(R.id.vnd_edtCodigo).requestFocus();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("parametro");
			if (object != null) {

				if (object instanceof Cliente) {
					cliente = (Cliente) object;
				} else if (object instanceof Venda) {
					venda = (Venda) object;
					cliente = venda.getCliente();
				}

			}
		} catch (NullPointerException e) {
			// esperado, esta chamada getIntent().getExtras() pode retornar null
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	protected void setarDataVencimento() {
		venda.setDataVencimento(UtilDates.adicionarMes(
				calendario.getDataFormatada(), INTERVALO_COBRANCA));
		obterValor.setDataEditText(R.id.vnd_edtDtVencimento,
				venda.getDataVencimento());
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.vnd_btnPesquisarPorCPF:
			buscarClienteCPF();
			setarDadosCliente();
			break;
		case R.id.vnd_btnAbriProdutos:
			if (isTabelaDePrecoNaoImportada()) {
				return;
			}
			if (isSemCPF()) {
				mostarMensagem("Cliente sem CPF. Solicite a alteração do cadastro no servidor e importe novamente.");
				return;
			}
			criarItemActivity();
			break;
		case R.id.vnd_btnPesquisarCliente:
			pesquisarPorCodigo();
			setarDadosCliente();
			break;
		}
	}

	private void pesquisarPorCodigo() {
		if (obterValor.isInValidoValorEdit(R.id.vnd_edtCodigo)) {
			mostarMensagem("O código deve ser informado.");
			return;
		}
		try {
			;
			cliente = clienteDAO.consultarPorId(Integer.parseInt(obterValor
					.getValorEdit(R.id.vnd_edtCodigo)));
			if (cliente == null) {
				mostarMensagem("Verifique se o cliente pertence a esta rota.");
			}
		} catch (NumberFormatException e) {
			mostarMensagem("Somente números é permitido.");
		}
	}

	private void buscarUltimaViagem() {
		try {
			idViagem = vendaDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// private void buscarRotaUltimaViagem() {
	// try {
	// rota = rotaDAO.buscarPor(idViagem);
	// } catch (Exception e) {
	// tratarExibirErro("Erro a buscar viagem.", e);
	// }
	// }

	/**
	 * nï¿½o precisa de try catch
	 */
	private void montarVenda() {
		venda.setDataVenda(calendario.getDataFormatada());
		venda.setDataVencimento(UtilDates.adicionarMes(venda.getDataVenda(),
				INTERVALO_COBRANCA));

		venda.setCliente(cliente);
//		venda.setIdViagem(idViagem);
		venda.setViagem(new Viagem(idViagem));
		venda.setRota(rota);
	}

	@Override
	protected android.app.Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case CalendarioData.DATE_DIALOG_ID:
			return calendario.criar();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_venda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnv_salvar:
			salvarVenda();
			return true;
		case R.id.mnv_produtos:
			criarItemActivity();
			return true;
		case R.id.mnv_voltar:
			finish();
			return true;
//		case R.id.mnv_delete:
//			deleteVenda();
//			return true;
		case R.id.mnv_devolucao:
			criarDialog(EnumOperacao.DEVOLUCAO, "Devolução");
			// if (venda.isNovo())
			// criarDialog(EnumOperacao.DEVOLUCAO, "Devoluï¿½ï¿½o");
			// else
			// execute(EnumOperacao.DEVOLUCAO, "Devoluï¿½ï¿½o");
			return true;
		case R.id.mnv_brinde:
			criarDialog(EnumOperacao.BRINDE, "Brinde");
			return true;
		case R.id.mnv_brinde_extra:
			criarDialog(EnumOperacao.BRINDE_EXTRA, "Brinde Extra");
			return true;
		case R.id.mnv_troca:
			criarDialog(EnumOperacao.TROCA, "Troca");
			return true;
		case R.id.mnv_receb_cheque:
			criarActivityGenerica(PagChequeActivity.class);
			return true;
		case R.id.mnv_receb_dinheiro:
			criarActivityGenerica(PagDinherioActivity.class);
			return true;
		case R.id.mnv_brinde_dinheiro:
			criarDialogBrindeDinheiro();
			return true;
		case R.id.mnv_delete_venda:
			criarDialogDeleteVenda();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void criarDialogDeleteVenda() {
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = null;
		try {
			promptsView = li.inflate(R.layout.dialog_viagem, null);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder
				.setTitle("Deseja excluir a venda, devolução, brinde, troca e os pagamentos?");

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.dlg_edtCodigo);

		final TextView txvLabel = (TextView) promptsView
				.findViewById(R.id.dlg_txvLabel);
		txvLabel.setText("Nº da Venda:");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Editable edtText = userInput.getEditableText();
								if (edtText != null
										&& (edtText.toString().trim().length() > 0)) {
									String idVenda = edtText.toString();
									System.out.println(idVenda);

									deleteVenda(Long.valueOf(idVenda));

								} else {
									UtilMensagem.mostarMensagemCurta(
											VendaActivity.this,
											"O código deve ser informado.");
								}
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

	private void criarDialogBrindeDinheiro() {
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = null;
		try {
			promptsView = li.inflate(R.layout.dialog_viagem, null);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setTitle("Brinde em Dinheiro");

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.dlg_edtCodigo);

		final TextView txvLabel = (TextView) promptsView
				.findViewById(R.id.dlg_txvLabel);
		txvLabel.setText("Nº da Venda:");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Editable edtText = userInput.getEditableText();
						if (edtText != null
								&& (edtText.toString().trim().length() > 0)) {
							String idVenda = edtText.toString();
							System.out.println(idVenda);

							criarTelaBrindeDinherio(Long.valueOf(idViagem));

						} else {
							UtilMensagem.mostarMensagemCurta(
									VendaActivity.this,
									"O código deve ser informado.");
						}
					}
				})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	void criarTelaBrindeDinherio(long idVenda) {
		try {
			Intent intent = new Intent(VendaActivity.this,
					BrindeDinheiroActivity.class);
			intent.putExtra("idVenda", idVenda);
			startActivity(intent);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarDialog(EnumOperacao operacao, String labelSubTitulo) {
		try {
			if (venda == null || venda.isNovo())
				new Dialog(this, operacao, labelSubTitulo, LABEL);
			else
				criarTelaItemAcerto(operacao);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarTelaItemAcerto(EnumOperacao operacao) {
		try {
			// if (venda.isNovo()) {
			// carregarVenda(Long.parseLong(codigoVenda));
			// if (!venda.isNovo())
			// setarDadosTelaVenda();
			// }
			if (venda == null || venda.isNovo()) {
				mostarMensagem("Venda não encontrada. Verifique se a venda está em modo de edição.");
				return;
			}

			Intent intent = new Intent(VendaActivity.this,
					ItemAcertoActivity.class);
			intent.putExtra("operacao", operacao);
			intent.putExtra("venda", venda);
			intent.putExtra("idViagem", idViagem);
			// intent.putExtra("venda", venda);
			startActivityForResult(intent, 0);
		} catch (NumberFormatException e) {
			mostarMensagemCurta("Apenas valor numérico é permitido.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void carregarVenda(long idVenda) {
		try {
			venda = vendaDAO.consultarPorId(idVenda);
			cliente = venda.getCliente();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void setarDadosTelaVenda() {
		setarDadosCliente();

		calendario.alterar(venda.getDataVenda());

		obterValor.setTextoTextView(venda.getValorTotal().getValorFormatado(),
				R.id.vnd_txvTotalVenda);

		obterValor.setDataEditText(R.id.vnd_edtDtVencimento,
				venda.getDataVencimento());

		obterValor.setTextoTextView(
				"Nº Viagem: " + String.valueOf(venda.getViagem().getId()),
				R.id.vnd_txvNumViagem);

		List<ItemListView> itens = new ArrayList<ItemListView>();
		for (ItemVenda item : venda.getItensVenda()) {
			ItemListView itemView = new ItemListView(item);
			itemView.setQuantidade(item.getQuantidade());
			itens.add(itemView);
		}
		criarAdapterItem(new ItemSelecionadoAdapter(this,
				R.layout.listview_fonte_menor, itens), R.id.vnd_lst_produtos);
	}

	private void deleteVenda(long idVenda) {
		try {
			Venda venda = vendaDAO.carregarVendaCompleta(idVenda);
			boolean delete = vendaDAO.delete(venda);
			if (delete)
				mostarMensagem("Venda excluída com sucesso!");
			else
				mostarMensagem("Venda não pode ser excluída!");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void salvarVenda() {
		if (idViagem <= 0) {
			UtilMensagem.mostarMensagemLonga(this,
					"Última viagem não encotrada.");
			return;
		}

		if (cliente == null || cliente.isNovo()) {
			UtilMensagem.mostarMensagemLonga(this,
					"Cliente deve ser informado.");
			return;
		}

		if (venda.getItensVenda().isEmpty()) {
			UtilMensagem.mostarMensagemLonga(this,
					"Os itens da venda devem ser informados.");
			return;
		}
		criarDialogSalvar();
	}

	@Override
	protected void salvar() {
		if (isSemCPF()) {
			mostarMensagem("Cliente sem CPF. Solicite a alteração do cadastro no servidor e importe novamente.");
			return;
		}
		try {
			montarVenda();
			Date dtVenda = calendario.getDataFormatada();
			venda.setDataVenda(dtVenda);
			Date dtVencimento = UtilDates.formatarData(obterValor
					.getValorEdit(R.id.vnd_edtDtVencimento));
			venda.setDataVencimento(dtVencimento);

			vendaDAO.adicionar(venda);
			UtilMensagem.mostarMensagemLonga(VendaActivity.this,
					"Venda salva com sucesso!");
			venda = new Venda();
			Thread.sleep(1000);
			finish();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private boolean isSemCPF() {
		boolean semCpf = cliente == null || cliente.getCpf() == null
				|| cliente.getCpf().trim().length() == 0;
		return semCpf;
	}

	private void criarItemActivity() {
		try {
			Intent intent = new Intent(VendaActivity.this, ItemActivity.class);
			intent.putExtra("classeActivity", getClass());
			intent.putExtra("venda", venda);
			intent.putExtra("rota", rota);
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Object obj = bundle.getParcelableArrayList("itensSelecionados");
			if (obj == null) {
				return;
			}

			List<ItemParcelable> extras = (List<ItemParcelable>) obj;

			List<ItemListView> itens = new ArrayList<ItemListView>();
			Set<ItemVenda> intensVenda = new HashSet<ItemVenda>();
			if (extras != null) {
				Dinheiro valorTotal = Dinheiro.novo();
				for (ItemParcelable itemParcelable : extras) {
					ItemListView item = itemParcelable.getItem();
					ItemVenda itemVenda = new ItemVenda();
					itemVenda.setProduto(item.getProduto());
					itemVenda.setQuantidade(item.getQuantidade());
					itemVenda.setValorUnitario(item.getValorUnitario());

					Dinheiro total = UtilDinheiro.multiplicar(
							item.getValorUnitario(), item.getQuantidade());
					itemVenda.setTotalUnitario(total);
					item.setTotalUnitario(total);

					itemVenda.setVenda(venda);

					valorTotal = UtilDinheiro.somar(valorTotal,
							item.getTotalUnitario());

					intensVenda.add(itemVenda);
					itens.add(item);
				}

				venda.setValorTotal(valorTotal);
				venda.setItensVenda(intensVenda);

				// if (venda.isSemItensVenda()) {
				// venda.setItensVenda(intensVenda);
				// } else {
				// venda.getItensVenda().addAll(intensVenda);
				// for (ItemVenda itemVenda : venda.getItensVenda()) {
				// ItemListView item = new ItemListView(itemVenda);
				// item.setQuantidade(itemVenda.getQuantidade());
				//
				// Dinheiro total = UtilDinheiro.multiplicar(
				// item.getValorUnitario(), item.getQuantidade());
				// item.setTotalUnitario(total);
				//
				// itens.add(item);
				// }
				// }

				obterValor.setTextoTextView(venda.getValorTotal()
						.getValorFormatado(), R.id.vnd_txvTotalVenda);
			}

			criarAdapterItem(new ItemSelecionadoAdapter(this,
					R.layout.listview_fonte_menor, itens),
					R.id.vnd_lst_produtos);

		}

	}

	private void setarDadosCliente() {
		if (cliente != null) {
			venda.setCliente(cliente);

			if (cliente.getCpf() != null && cliente.getCpf().length() > 0) {
				obterValor.setTextoEditText(
						UtilString.formatarCPF(cliente.getCpf()),
						R.id.vnd_edtCPF);
			}

			obterValor.setTextoEditText(cliente.getId().toString(),
					R.id.vnd_edtCodigo);
			obterValor.setTextoTextView("Nome: " + cliente.getNome(),
					R.id.vnd_txvNome);
			obterValor.setTextoTextView("Cidade: "
					+ cliente.getCidade().getNome(), R.id.vnd_txvCidade);
		}
	}

	private void buscarClienteCPF() {
		if (idViagem <= 0) {
			mostarMensagem("Verifique se a viagem foi importada.");
			return;
		}

		String cpf = Mascara.unmask(obterValor.getValorEdit(R.id.vnd_edtCPF));
		if (UtilString.isValorInvalido(cpf)) {
			mostarMensagem("Informe o CPF para prosseguir");
			return;
		}

		if (cpf.length() < 11) {
			mostarMensagem("O CPF deve conter 11 números.");
			return;
		}
		try {
			cliente = clienteDAO.pesquisarPor(rota.getId(), cpf);
			if (cliente == null) {
				mostarMensagem("Verifique se o cliente pertence a esta rota.");
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public void execute(Object... parametro) {
		criarTelaItemAcerto((EnumOperacao) parametro[0]);
	}

	@Override
	public void onBackPressed() {
		if (cliente != null) {

			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Venda em aberto")
					.setMessage("Deseja realmente sair?")
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							}).setNegativeButton("Não", null).show();
		} else {
			finish();
		}
	}
}
