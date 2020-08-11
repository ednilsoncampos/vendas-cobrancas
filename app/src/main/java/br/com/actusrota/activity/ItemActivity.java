package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ItemAdapter;
import br.com.actusrota.adapter.ItemPrecoVendaAdapter;
import br.com.actusrota.dao.ItemDevolucaoDAO;
import br.com.actusrota.dao.ItemViagemDAO;
import br.com.actusrota.dao.ProdutoDAO;
import br.com.actusrota.entidade.Item;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemParcelable;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.ItemVenda;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.tabela.TabelaViagem;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class ItemActivity extends SuperActivity<Item> implements IAdicionarItem {

	private ItemAdapter adapter;
	private ListView ltvItens;
	private List<ItemListView> itensView = new ArrayList<ItemListView>(0);
	// venda
	private ItemPrecoVendaAdapter adapterItemPrecoVenda;
	private ListView ltvItensPrecoVenda;
	private List<ItemListView> itensPrecoVendaView = new ArrayList<ItemListView>(
			0);
	// private ItemTabelaPrecoDAO itemTabelaPrecoDAO;
	private Class<Activity> classeActivity;
	private ProdutoDAO produtoDAO;
	private Produto produto;
	private ObterValor obterValor;
	private Venda venda;
	private ItemViagemDAO itemViagemDAO;
	private ItemDevolucaoDAO itemDevolucaoDAO;
	private Long idViagem;
	private Rota rota;

	public ItemActivity() {
		// itemTabelaPrecoDAO = new ItemTabelaPrecoDAO(this);
		itemDevolucaoDAO = new ItemDevolucaoDAO(this);
		itemViagemDAO = new ItemViagemDAO(this);
		produtoDAO = new ProdutoDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_produto_layout);
		try {
			obeterParametrosActivity();
			if (idViagem == null) {
				buscarUltimaViagem();
			}
			if(rota == null) {
				mostarMensagem("Rota não encontrada.");
				return;
			}
			criarItemAdapter();
			criarItemVendaAdapter();
		} catch (Exception e) {
			e.printStackTrace();
			UtilMensagem.mostarMensagemLonga(this,
					"Detalhe do erro:" + e.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnit_adicionar_produto:
			adicionarProdutos();
			finish();
			return true;
			// case R.id.mnit_adicionar_produto_tabela:
			// // adicionarAdapter();
			// return true;
		case R.id.mnit_voltar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Produtos não adicionados")
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

	private void buscarUltimaViagem() {
		try {
			idViagem = itemViagemDAO.buscarMaxID(TabelaViagem.TABELA_VIAGEM);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Object obeterParametrosActivity() {
		try {
			Bundle b = getIntent().getExtras();
			Object object = b.get("classeActivity");
			if (object != null)
				classeActivity = (Class<Activity>) object;

			Object objVenda = b.get("venda");
			if (objVenda != null) {
				venda = (Venda) objVenda;
				if (!venda.isNovo())
					idViagem = venda.getViagem().getId();
			}

			Object objRota = b.get("rota");
			if (objRota != null) {
				rota = (Rota) objRota;
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	private void adicionarProdutos() {
		if (adapterItemPrecoVenda == null) {
			return;
		}

		List<ItemListView> selecionados = new ArrayList<ItemListView>();
		for (ItemListView item : adapterItemPrecoVenda.getItens()) {
			if (!(item.getQuantidade() == null || item.getQuantidade() <= 0)) {
				selecionados.add(item);
			}
		}

		ArrayList<ItemParcelable> pointsExtra = new ArrayList<ItemParcelable>();
		for (ItemListView item : selecionados) {
			pointsExtra.add(new ItemParcelable(item));
		}
		Intent mIntent = new Intent(this, classeActivity);
		mIntent.putExtra("itensSelecionados", pointsExtra);
		setResult(ItemActivity.RESULT_OK, mIntent);
	}

	@SuppressWarnings("unchecked")
	private <E extends Item> void criarItemAdapter() {
		try {
			List<E> itensViagem = (List<E>) itemViagemDAO
					.getItensViagemDevolucao(new Viagem(idViagem));

			if (itensViagem == null || itensViagem.isEmpty()) {
				UtilMensagem
						.mostarMensagemLonga(this,
								"Itens da viagem não encontrados. Verifique se a viagem foi importada.");
				return;
			}

			ltvItens = (ListView) findViewById(R.id.ipl_ltv_itens);

			// if (venda == null || venda.isNovo()) {
			// criarItemView(itensViagem);
			// } else {
			// criarItemViewEditar(venda.getItensVenda());
			// }
			criarItemView(itensViagem);

			Collections.sort(itensView);
			adapter = new ItemAdapter(this, itensView);
			ltvItens.setAdapter(adapter);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private <E extends Item> void criarItemVendaAdapter() {
		try {
			ltvItensPrecoVenda = (ListView) findViewById(R.id.ipl_ltv_itensAdicionados);

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
		for (ItemVenda item : venda.getItensVenda()) {
			ItemListView itemView = new ItemListView(item);
			itemView.setQuantidade(item.getQuantidade());
			itensPrecoVendaView.add(itemView);
		}
	}

	private <E extends Item> void criarItemView(List<E> itensViagem) {
		for (Item item : itensViagem) {
			ItemListView itemView = new ItemListView(item);
			if (!itensView.contains(item)) {
				itemView.setQuantidade(0);
				itensView.add(itemView);
			}
		}
	}

	// private void adicionarAdapter() {
	// if (obterValor.isInValidoValorEdit(R.id.ipl_pes_edtNome)
	// || produto == null) {
	// mostarMensagem("Faça uma pesquisa de produto prosseguir.");
	// return;
	// }
	//
	// ItemDevolucao item = itemDevolucaoDAO.pesquisarPor(produto);
	// if (item == null) {
	// mostarMensagem("Produto fora da viagem ou não foi devolvido.");
	// return;
	// }
	// item.setId(null);
	// try {
	// if (adapter == null || adapter.isEmpty()) {
	// ltvItens = (ListView) findViewById(R.id.ipl_ltv_itens);
	//
	// ItemListView itemView = new ItemListView(item);
	// itensView.add(itemView);
	//
	// Collections.sort(itensView);
	// adapter = new ItemAdapter(this, itensView);
	// ltvItens.setAdapter(adapter);
	// } else {
	// ItemListView itemView = new ItemListView(item);
	// adapter.adicionarAdapter(itemView);
	// }
	// produto = null;
	// } catch (Exception e) {
	// UtilMensagem.mostarMensagemLonga(this, e.getMessage());
	// }
	// }

	public void pesquisarPorCodigo() {
		String codigo = obterValor.getValorEdit(R.id.ipl_pes_edtNome);
		if (UtilString.isValorInvalido(codigo)) {
			UtilMensagem.mostarMensagemLonga(this,
					"Informe o código para prosseguir");
			return;
		}
		try {
			Integer.parseInt(codigo);// valida se é numero ou letra
			pesquisarPorCodigo(codigo);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numerico.");
		}
	}

	private void pesquisarPorCodigo(String codigo) {
		try {
			produto = produtoDAO.pesquisarPorCodigo(codigo);

			if (produto == null) {
				UtilMensagem.mostarMensagemLonga(this,
						"Produto não encontrado.");
				return;
			}
			// obterValor.setTextoTextView(produto.getDescricao(),
			// R.id.ipl_pes_txvNome);
		} catch (NumberFormatException e) {
			UtilMensagem.mostarMensagemLonga(this,
					"O código deve ser numerico.");
		} catch (Exception e) {
			UtilMensagem.mostarMensagemLonga(this, e.getMessage());
		}
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.ipl_pes_btnPesquisar:
			pesquisarPorCodigo();
			break;
		}
	}

	@Override
	public void adicionar(ItemListView item) {
		itensView.add(item);
		Collections.sort(itensView);
		adapter.notifyDataSetChanged();
		// adapter = new ItemAdapter(this, itensView);
		// ltvItens.setAdapter(adapter);
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

}
