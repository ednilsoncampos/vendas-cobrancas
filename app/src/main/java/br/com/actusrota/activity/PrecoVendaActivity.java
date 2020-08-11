package br.com.actusrota.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.com.actusrota.R;
import br.com.actusrota.dao.ItemTabelaPrecoDAO;
import br.com.actusrota.dao.TabelaPrecoDAO;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.Produto;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.entidade.TabelaDePreco;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDinheiro;

public class PrecoVendaActivity extends SuperActivity<ItemTabelaPreco> {

	private ItemTabelaPrecoDAO itemTabelaPrecoDAO;
	private TabelaPrecoDAO tabelaPrecoDAO;
	private ItemTabelaPreco itemTabelaPreco;
	private ObterValor obterValor;
	private Class<Activity> classeActivity;

	public PrecoVendaActivity() {
		itemTabelaPrecoDAO = new ItemTabelaPrecoDAO(this);
		tabelaPrecoDAO = new TabelaPrecoDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preco_venda_layout);
		try {
			Object obj = obeterParametrosActivity();
			if (obj == null) {
				mostarMensagem("Produto não encontrado.");
				return;
			}
			obterParametro();

			boolean editando = obj.getClass().isAssignableFrom(
					ItemTabelaPreco.class);

			Produto produto = null;
			if (obj.getClass().isAssignableFrom(Produto.class)) {
				produto = (Produto) obj;
			} else if (editando) {
				produto = ((ItemTabelaPreco) obj).getProduto();
			}

			Bundle b = getIntent().getExtras();
			Object object = b.get("rota");
			if (object == null) {
				mostarMensagem("Rota não encontrada.");
				return;
			}
			TabelaDePreco tabelaDePreco = buscarTabelaPreco(((Rota) object));

			itemTabelaPreco = itemTabelaPrecoDAO.pesquisarPor(produto,
					tabelaDePreco);

			criarComponentes();

			// sobrescreve o preço de venda e seta quantidade
			if (editando) {
				ItemTabelaPreco item = (ItemTabelaPreco) obj;
				itemTabelaPreco.setQuantidade(item.getQuantidade());
				itemTabelaPreco.setPrecoVenda(item.getPrecoVenda());
				alterarPrecoVendaQuantidade();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mostarMensagem(e.getMessage());
		}
	}

	private TabelaDePreco buscarTabelaPreco(Rota rota) {
		return tabelaPrecoDAO.pesquisarPor(rota.getId());
	}

	private void obterParametro() {
		Bundle b = getIntent().getExtras();
		Object object = b.get("classeActivity");
		if (object != null)
			classeActivity = (Class<Activity>) object;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private ItemListView criarItemListView() {
		ItemListView item = new ItemListView();
		item.setValorUnitario(obterValor
				.getValorMonetarioEdit(R.id.ipvl_edtPrecoVenda));
		item.setQuantidade(Integer.valueOf(obterValor
				.getValorEdit(R.id.ipvl_edtQuantidade)));
		item.setProduto(itemTabelaPreco.getProduto());
		return item;
	}

	private void criarComponentes() {
		obterValor.setTextoTextView(
				itemTabelaPreco.getProduto().getDescricao(),
				R.id.ipvl_txvProduto);
		obterValor.setTextoTextView(itemTabelaPreco.getPrecoMinimoVenda()
				.getValorFormatado(), R.id.ipvl_txvPrecoMinimoVenda);
		obterValor.setTextoTextView(itemTabelaPreco.getPrecoVenda()
				.getValorFormatado(), R.id.ipvl_txvPrecoTabela);

		alterarPrecoVendaQuantidade();
	}

	private void alterarPrecoVendaQuantidade() {
		if (itemTabelaPreco.getQuantidade() != null
				&& itemTabelaPreco.getQuantidade() > 0) {
			obterValor.setTextoEditText(
					String.valueOf(itemTabelaPreco.getQuantidade()),
					R.id.ipvl_edtQuantidade);
		}
		obterValor.setTextoEditText(itemTabelaPreco.getPrecoVenda()
				.getValorFormatado(), R.id.ipvl_edtPrecoVenda);
	}

	public void eventoCliqueBotao(View v) {
		switch (v.getId()) {
		case R.id.ipvl_btnAdicionar:
			adicionarItem();
			break;
		case R.id.ipvl_btnCancelar:
			finish();
			break;
		}
	}

	public boolean isPrecoVendaNaoPermitido() {
		Dinheiro precoVenda = obterValor
				.getValorMonetarioEdit(R.id.ipvl_edtPrecoVenda);
		return UtilDinheiro.maior(itemTabelaPreco.getPrecoMinimoVenda(),
				precoVenda);
		// return (precoVenda.getValor() <
		// itemTabelaPreco.getValorUnitario().getValor());
	}

	private void adicionarItem() {
		if (isPrecoVendaNaoPermitido()) {
			mostarMensagem("Valor para preço de venda não permitido.");
			return;
		}
		Intent mIntent = new Intent(this, classeActivity);
		ItemListView item = criarItemListView();
		mIntent.putExtra("itemTabelaPreco", item);
		setResult(ItemActivity.RESULT_OK, mIntent);
		finish();
	}

}
