package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ItemAdapter;
import br.com.actusrota.adapter.ItemPrecoVendaAdapter;
import br.com.actusrota.dao.AcertoClienteParametroDAO;
import br.com.actusrota.dao.ItemDevolucaoDAO;
import br.com.actusrota.dao.ItemViagemDAO;
import br.com.actusrota.dao.ProdutoDAO;
import br.com.actusrota.dao.RotaDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.dao.ViagemDAO;
import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.Item;
import br.com.actusrota.entidade.ItemBrinde;
import br.com.actusrota.entidade.ItemBrindeExtra;
import br.com.actusrota.entidade.ItemDevolucao;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemManter;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.ItemTroca;
import br.com.actusrota.entidade.ItemViagem;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.negocio.AcertoClienteNegocio;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.AdicionarListener;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDinheiro;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ItemAcertoActivity<T extends ItemManter> extends SuperActivity<T> {

	private ListView ltvItens;
	private ItemAdapter adapter;
	private List<ItemListView> itensView = new ArrayList<ItemListView>(0);
	// preço de venda
	private ItemPrecoVendaAdapter adapterItemPrecoVenda;
	private ListView ltvItensPrecoVenda;
	private List<ItemListView> itensPrecoVendaView = new ArrayList<ItemListView>(
			0);
	private VendaDAO vendaDAO;
	private EnumOperacao operacao;
	private Venda venda;
	// private Long idVenda;
	private Viagem viagem;
	private ProdutoDAO produtoDAO;
	private ObterValor obterValor;
	private Produto produto;
	// private ItemTabelaPrecoDAO itemTabelaPrecoDAO;
	List<T> selecionados = new ArrayList<T>(0);
	private ItemViagemDAO itemViagemDAO;
	private ItemDevolucaoDAO itemDevolucaoDAO;
	private List<ItemViagem> itensViagem;
	private AcertoClienteParametroDAO acertoClienteParametroDAO;
	private RotaDAO rotaDAO;
	private Rota rota;
	private Long idViagem;

	public ItemAcertoActivity() {
		vendaDAO = new VendaDAO(this);
		// itemTabelaPrecoDAO = new ItemTabelaPrecoDAO(this);
		itemDevolucaoDAO = new ItemDevolucaoDAO(this);
		acertoClienteParametroDAO = new AcertoClienteParametroDAO(this);
		itemViagemDAO = new ItemViagemDAO(this);
		produtoDAO = new ProdutoDAO(this);
		rotaDAO = new RotaDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devolucao_layout);
		obterParametrosIntent();
		// carregarVenda();
		if (venda == null) {
			mostarMensagem("Venda não carregada.");
			return;
		}
		buscarRota();
		if (rota == null) {
			mostarMensagem("Rota não encontrada.");
			return;
		}
		buscarUltimaViagem();
		if (idViagem == null) {
			return;
		}
		listarItensViagem();
		criarComponentes();
		criarAdapterAcerto();
		criarItemPrecoVendaAdapter();
	}
	
	private void buscarUltimaViagem() {
		try {
			idViagem = vendaDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
	
	private void listarItensViagem() {
		itensViagem = itemViagemDAO.buscarItensAcerto(new Viagem(idViagem));
	}

	private void buscarRota() {
		try {
			rota = venda.getRota();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_item_acerto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mna_acerto_salvar:
			salvarAcerto();
			return true;
			// case R.id.mna_acerto_add_item:
			// // adicionarAdapter();
			// return true;
		case R.id.mna_detalhamento_venda:
			criarDetalhamentoDeVenda();
			return true;
			// case R.id.mn_acerto_a_receber:
			// calcularValorAReceber();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// private void calcularValorAReceber() {
	// for (ItemListView item : adapter.getItens()) {
	// UtilDinheiro.multiplicar(item.getValorUnitario(), item.getQuantidade());
	// }
	// }

	public void eventoCliqueBotao(View v) {
		this.itensView = new ArrayList<ItemListView>(1);
		switch (v.getId()) {
		case R.id.dev_pes_btnPesquisar:
			pesquisarPorCodigo();
			break;
		case R.id.dev_pes_btnListarTodos:
			criarAdapterAcerto();
			break;			
		}
	}

	private void salvarAcerto() {
		if (venda == null) {
			mostarMensagem("Venda não carregada.");
			return;
		}
		criarItensSelecionaodos();
		// selecionados = adicionarProdutos();
		if (selecionados.isEmpty()) {
			mostarMensagem("No mínimo um item deve ser adicionado.");
			return;
		}

		AcertoClienteParametro acertoCliente = acertoClienteParametroDAO
				.buscar(operacao);
		if (acertoCliente != null) {
			Dinheiro totalAcerto = Dinheiro.novo();
			for (T elemento : selecionados) {
				Item total = (Item) elemento;
				totalAcerto = UtilDinheiro.somar(totalAcerto,
						total.getTotalUnitario());
			}

			venda = vendaDAO.carregarVendaCompleta(venda.getId());
			venda.somarRecebimentos();

			AcertoClienteNegocio acertoNegocio = new AcertoClienteNegocio(
					acertoCliente);
			if (!acertoNegocio.isMargemValida(venda.getValorTotalRecebido(),
					totalAcerto)) {
				mostarMensagem("Porcentagem de " + operacao.getDescricao()
						+ " excede o valor permitido:"
						+ acertoCliente.getMargemTotal());
				return;
			}
		}

		criarDialogSalvar();
	}

	private void criarItensSelecionaodos() {
		List<ItemListView> itens = adapterItemPrecoVenda.getItens();
		switch (operacao) {
		case BRINDE:
			for (ItemListView item : itens) {
				ItemBrinde itemBrinde = new ItemBrinde(item);
				itemBrinde.setVenda(venda);
				// itemBrinde.setViagem(venda.getViagem());
				itemBrinde.setViagem(venda.getViagem());
				itemBrinde.setTotalUnitario(UtilDinheiro.multiplicar(
						itemBrinde.getValorUnitario(),
						itemBrinde.getQuantidade()));
				selecionados.add((T) itemBrinde);
			}
			break;
		case BRINDE_EXTRA:
			for (ItemListView item : itens) {
				ItemBrindeExtra itemBrindeExtra = new ItemBrindeExtra(item);
				itemBrindeExtra.setVenda(venda);
				itemBrindeExtra.setViagem(venda.getViagem());
				itemBrindeExtra.setTotalUnitario(UtilDinheiro.multiplicar(
						itemBrindeExtra.getValorUnitario(),
						itemBrindeExtra.getQuantidade()));
				selecionados.add((T) itemBrindeExtra);
			}
			break;
		case DEVOLUCAO:
			for (ItemListView item : itens) {
				ItemDevolucao itemDevolucao = new ItemDevolucao(item);
				itemDevolucao.setVenda(venda);
				itemDevolucao.setViagem(venda.getViagem());
				itemDevolucao.setTotalUnitario(UtilDinheiro.multiplicar(
						itemDevolucao.getValorUnitario(),
						itemDevolucao.getQuantidade()));
				selecionados.add((T) itemDevolucao);
			}
			break;
		case TROCA:
			for (ItemListView item : itens) {
				ItemTroca itemTroca = new ItemTroca(item);
				itemTroca.setVenda(venda);
				itemTroca.setViagem(venda.getViagem());
				itemTroca
						.setTotalUnitario(UtilDinheiro.multiplicar(
								itemTroca.getValorUnitario(),
								itemTroca.getQuantidade()));
				selecionados.add((T) itemTroca);
			}
		}
	}

	@Override
	protected void salvar() {
		try {
			operacao.salvar(ItemAcertoActivity.this, selecionados);

			UtilMensagem.mostarMensagemLonga(ItemAcertoActivity.this,
					operacao.getDescricao() + " salvo(a) com sucesso.");

			if (EnumOperacao.DEVOLUCAO.equals(operacao)) {
				// FECHAR A VENDA
				Set<ItemDevolucao> set = new HashSet<ItemDevolucao>(
						selecionados.size());
				for (T item : selecionados) {
					set.add((ItemDevolucao) item);
				}
				venda.setItensDevolucao(set);
				venda.fecharVenda();
				// vendaDAO.atualizar(venda.getId(), venda);
			}
			vendaDAO.atualizar(venda.getId(), venda);
			Thread.sleep(1000);
			finish();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// private void carregarVenda() {
	// try {
	// if (venda == null || venda.isNovo())
	// venda = vendaDAO.consultarPorId(idVenda);
	// System.out.println(venda);
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	private void obterParametrosIntent() {
		try {
			Bundle b = getIntent().getExtras();
			operacao = (EnumOperacao) b.get("operacao");
			viagem = new Viagem(b.getLong("idViagem"));

			Object object = b.get("venda");
			if (object != null) {
				venda = (Venda) object;
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private <E extends Item> void criarItemPrecoVendaAdapter() {
		try {
			ltvItensPrecoVenda = (ListView) findViewById(R.id.dev_ltvItensSelecionados);

			criarItemViewEditar();

			Collections.sort(itensPrecoVendaView);
			adapterItemPrecoVenda = new ItemPrecoVendaAdapter(this,
					itensPrecoVendaView, venda);
			ltvItensPrecoVenda.setAdapter(adapterItemPrecoVenda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarItemViewEditar() {
		Set<? extends Item> set = null;
		switch (operacao) {
		case BRINDE:
			// venda.setItensBrinde(set);
			set = venda.getItensBrinde();
			break;
		case BRINDE_EXTRA:
			set = venda.getItensBrindeExtra();
			break;
		case DEVOLUCAO:
			set = venda.getItensDevolucao();
			break;
		case TROCA:
			set = venda.getItensTroca();
			break;
		}

		if (set != null) {
			for (Item item : set) {
				ItemListView itemView = new ItemListView(item);
				itemView.setQuantidade(item.getQuantidade());
				itensPrecoVendaView.add(itemView);
			}
		}
	}

	private void criarAdapterAcerto() {
		try {
			ltvItens = (ListView) findViewById(R.id.dev_ltv_produtos);

			// if (venda != null && !venda.isNovo()) {
			// buscarItensEditarVenda();
			//
			// Set<? extends Item> itens = Collections.emptySet();
			//
			// if (EnumOperacao.BRINDE.equals(operacao)
			// || EnumOperacao.BRINDE_EXTRA.equals(operacao)) {
			// if (EnumOperacao.BRINDE.equals(operacao)
			// && !venda.isSemBrinde()) {
			// itens = venda.getItensBrinde();
			// } else if (EnumOperacao.BRINDE_EXTRA.equals(operacao)
			// && !venda.isSemBrindeExtra()) {
			// itens = venda.getItensBrindeExtra();
			// }
			// if (itens.isEmpty()) {
			// criarItensViewComItensViagem();
			// } else {
			// criarItensView(itens);
			// }
			//
			// Collections.sort(this.itensView);
			// adapter = new ItemAdapter(this, itensView);
			// ltvItens.setAdapter(adapter);
			// return;
			// }
			// }

			if (EnumOperacao.DEVOLUCAO.equals(operacao)) {
				criarItensView(venda.getItensVenda());
			} else {
				if (itensViagem == null || itensViagem.isEmpty()) {
					listarItensViagem();
				}
				criarItensViewComItensViagem();
			}

			setarTotalRegistros();

			Collections.sort(this.itensView);
			adapter = new ItemAdapter(this, itensView);
			ltvItens.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
			tratarExibirErro(e);
		}
	}

	private void setarTotalRegistros() {
		obterValor.setTextoTextView(String.valueOf(itensView.size()),
				R.id.dev_pestxvTotalRegistros);
	}

	private <E extends Item> void criarItensView(Set<E> itens) {
		for (Item item : itens) {
			item.setId(null);
			ItemListView itemView = new ItemListView(item);
			if (!itensView.contains(itemView))
				itensView.add(itemView);
		}
	}

	private void criarItensViewComItensViagem() {
		if (itensViagem == null || itensViagem.isEmpty()) {
			UtilMensagem.mostarMensagemLonga(this,
					"Verifique se a viagem foi importada.");
			return;
		}

		for (ItemViagem item : itensViagem) {
			item.setId(null);
			ItemListView itemView = new ItemListView(item);
			itensView.add(itemView);
		}
	}

	/**
	 * Carrega brinde, devolução, troca ou brinde extra
	 */
	private void buscarItensEditarVenda() {
		Set<ItemManter> itens = operacao.buscarItens(this, venda);
		for (ItemManter item : itens) {
			ItemListView itemView = new ItemListView(item);
			itemView.setQuantidade(item.getQuantidade());
			if (!itensView.contains(itemView))
				itensView.add(itemView);
		}
	}

	// private void adicionarAdapter() {
	// if (obterValor.isInValidoValorEdit(R.id.dev_pes_edtNome)
	// || produto == null) {
	// mostarMensagem("Faça uma pesquisa de produto prosseguir.");
	// return;
	// }
	//
	// ItemViagem item = itemViagemDAO.pesquisarPor(produto);
	// if (item == null) {
	// ItemDevolucao itemDevolucao = itemDevolucaoDAO
	// .pesquisarPor(produto);
	// if (itemDevolucao == null) {
	// mostarMensagem("Produto fora da viagem ou não foi devolvido.");
	// return;
	// }
	// item = new ItemViagem(itemDevolucao);
	// }
	// item.setId(null);
	//
	// try {
	// if (adapter == null || adapter.isEmpty()) {
	// ltvItens = (ListView) findViewById(R.id.dev_ltv_itens);
	//
	// ItemListView itemView = new ItemListView(item);
	// itensView.add(itemView);
	//
	// Collections.sort(itensView);
	// adapter = new ItemAdapter(this, itensView);
	// ltvItens.setAdapter(adapter);
	// } else {
	// ItemListView itemView = new ItemListView(item);
	// if (!adapter.getItens().contains(itemView)) {
	// // itensView.add(itemView);
	// adapter.adicionarAdapter(itemView);
	// adapter.notifyDataSetChanged();
	// // ((ArrayAdapter<ItemListView>) ltvItens.getAdapter())
	// // .notifyDataSetChanged();
	// }
	// }
	// obterValor.setFocusImagemButton(R.id.dev_pes_btnPesquisar);
	// obterValor.setTextoEditText("", R.id.dev_pes_edtNome);
	// obterValor.setTextoTextView("", R.id.dev_pes_txvNome);
	// produto = null;
	// } catch (Exception e) {
	// tratarExibirErro(e);
	// }
	// }

	private void criarComponentes() {

		try {
			// ObterValor obterValor = new ObterValor(ItemAcertoActivity.this);
			obterValor.setTextoTextView("Cliente: "
					+ venda.getCliente().getNome(), R.id.dev_txvCliente);
			obterValor.setTextoTextView(
					"Nº Venda: " + venda.getId().toString(),
					R.id.dev_txvNumVenda);

			obterValor.setTextoTextView("Nº Viagem: "
					+ venda.getViagem().getId().toString(),
					R.id.dev_txvNumViagem);

			obterValor.setTextoTextView(venda.getCliente().getCidade()
					.getNome(), R.id.dev_txvCidade);

			obterValor.setTextoTextView(venda.getValorTotal()
					.getValorFormatado(), R.id.dev_txvTotalVenda);

			obterValor.setTextoTextView(operacao.getDescricao(),
					R.id.dev_txvTitulo);

			obterValor.setTextoTextView(venda.getValorTotalRecebido()
					.getValorFormatado(), R.id.dev_txvTotalRecebido);
			// obterValor.setTextoTextView(venda.getPercentualDevolucao(),
			// R.id.dev_txvPercentualDevolucao);

			new AdicionarListener(obterValor).adicionarListener(
					new EventoEditText(), R.id.dev_pes_edtNome);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private List<T> adicionarProdutos() {
		List<T> itens = new ArrayList<T>();
		try {
			for (ItemListView item : adapter.getItens()) {
				if (!(item.getQuantidade() == null || item.getQuantidade() <= 0)) {
					item.setTotalUnitario(UtilDinheiro.multiplicar(
							item.getValorUnitario(), item.getQuantidade()));
					T itemManter = operacao.criarItem(item);
					itemManter.setVenda(venda);
					itemManter.setViagem(viagem);
					itens.add(itemManter);
				}
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return itens;
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Acerto de venda")
				.setMessage("Deseja realmente sair?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).setNegativeButton("Não", null).show();
	}

	public void pesquisarPorCodigo() {
		String codigo = obterValor.getValorEdit(R.id.dev_pes_edtCodigo).trim();
		if (UtilString.isValorInvalido(codigo)) {
			mostarMensagem("Informe o código para prosseguir");
			return;
		}
		try {
			Integer.parseInt(codigo);// valida se é numero ou letra
			pesquisarPorCodigo(codigo);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numerico.");
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void pesquisarPorCodigo(String codigo) {
		try {
			produto = produtoDAO.pesquisarPorCodigo(codigo);

			if (produto == null || produto.isNovo()) {
				UtilMensagem.mostarMensagemLonga(this,
						"Produto não encontrado.");
				return;
			}
			// obterValor.setTextoTextView(produto.getDescricao(),
			// R.id.dev_pes_txvNome);
			List<Produto> produtos = new ArrayList<Produto>(1);
			produtos.add(produto);
			criarAdapter(produtos);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarAdapter(List<Produto> produtos) {
		if (produtos == null || produtos.isEmpty()) {
			return;
		}
		try {
			itensView = new ArrayList<ItemListView>(produtos.size());
			for (Produto produto : produtos) {
				ItemListView item = new ItemListView(produto);
				itensView.add(item);
			}
			Collections.sort(this.itensView);
			adapter = new ItemAdapter(this, itensView);
			ltvItens.setAdapter(adapter);
			setarTotalRegistros();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarDetalhamentoDeVenda() {
		try {
			venda = vendaDAO.carregarVendaCompleta(venda.getId());

			List<T> itensTela = adicionarProdutos();

			Set set = new HashSet(itensTela.size());
			for (T item : itensTela) {
				set.add((Item) item);
			}

			switch (operacao) {
			case BRINDE:
				venda.setItensBrinde(set);
				break;
			case BRINDE_EXTRA:
				venda.setItensBrindeExtra(set);
				break;
			case DEVOLUCAO:
				venda.setItensDevolucao(set);
				break;
			case TROCA:
				venda.setItensTroca(set);
				break;
			}

			venda.somarTodosItens();
			criarActivityComParametro(DetalhamentoVenda.class, venda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// public void calcularPercentualDevolucao(String valor) {
	//
	// }

	public void pesquisarLike() {
		String valorEdit = obterValor.getValorEdit(R.id.dev_pes_edtNome);
		try {
			if(valorEdit.trim().length() == 0) {
				return;
			}
			List<Produto> produtos = produtoDAO.pesquisarLike(valorEdit);
			if (produtos == null) {
				produtos = new ArrayList<Produto>();
				Log.w("pesquisarLike:", "Produtos não encontrados, parametro: "+ valorEdit);
			}
			criarAdapter(produtos);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	class EventoEditText implements TextWatcher {

		@Override
		public void afterTextChanged(Editable editable) {
			if (editable == null || editable.toString().length() > 0) {
				return;
			}
			criarAdapterAcerto();
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
				pesquisarLike();
				return;
			}
			if (textoDigitado.length() > 3) {
				if (textoDigitado.length() % 3 == 0)
					pesquisarLike();
				return;
			}
		}

	}

	public void criarPrecoVendaActivity(ItemTabelaPreco itemEditar) {
		try {
			Intent intent = new Intent(this, PrecoVendaActivity.class);
			intent.putExtra("classeActivity", getClass());
			intent.putExtra("parametro", itemEditar);
			intent.putExtra("rota", rota);
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Object obj = bundle.get("itemTabelaPreco");
			if (obj == null) {
				return;
			}
			ItemListView item = (ItemListView) obj;
			adapterItemPrecoVenda.alterar(item);
		}

	}

	public void criarPrecoVendaActivity(Produto produto) {
		try {
			Intent intent = new Intent(this, PrecoVendaActivity.class);
			intent.putExtra("classeActivity", getClass());
			intent.putExtra("parametro", produto);
			intent.putExtra("rota", rota);
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
