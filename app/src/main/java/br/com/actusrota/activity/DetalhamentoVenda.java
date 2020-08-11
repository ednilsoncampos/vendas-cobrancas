package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.com.actusrota.R;
import br.com.actusrota.adapter.ItemSelecionadoAdapter;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemVenda;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDinheiro;

public class DetalhamentoVenda extends SuperActivity<Venda> {

	private Venda venda;
	private ObterValor obterValor;

	public DetalhamentoVenda() {
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhamento_layout);
		criarComponentes();
	}

	private void criarComponentes() {
		try {
			Bundle b = getIntent().getExtras();

			Object object = b.get("parametro");
			if (object != null) {
				venda = (Venda) object;
				obterValor.setTextoTextView(venda.getValorTotalCheque()
						.getValorFormatado(), R.id.dtl_txvCheque);
				obterValor.setTextoTextView(venda.getValorTotalEspecie()
						.getValorFormatado(), R.id.dtl_txvDinheiro);
				
				obterValor.setTextoTextView(
						UtilDinheiro.somar(venda.getValorTotalCheque(),
								venda.getValorTotalEspecie())
								.getValorFormatado(),
						R.id.dtl_txvTotalRecebimento);
				
				obterValor.setTextoTextView(venda.getValorReceber()
						.getValorFormatado(), R.id.dtl_txvTotalAReceber);

				obterValor.setTextoTextView(
						venda.getValorTotalDevolucao().getValorFormatado()
								+ "  (" + venda.getPercentualDevolucao()
								+ " %)", R.id.dtl_txvDevolucao);

				obterValor
						.setTextoTextView(
								venda.getComissaoClienteEspecie()
										.getValorFormatado()
										+ "  ("
										+ venda.getPercentualBrindeRS() + " %)",
								R.id.dtl_txvBrindeRS);

				obterValor.setTextoTextView(
						venda.getValorTotalBrindeProduto().getValorFormatado() + "  ("
								+ venda.getPercentualBrindeProduto() + " %)",
						R.id.dtl_txvBrindeProduto);
				
				obterValor.setTextoTextView(venda.getSomaTotalBrinde()
						.getValorFormatado()+"("+venda.getPercentualVenda()+"%)", R.id.dtl_txvSomaBrinde);

				obterValor.setTextoTextView(venda.getValorTotalTroca()
						.getValorFormatado(), R.id.dtl_txvTroca);

				obterValor.setTextoTextView(venda.getValorTotal()
						.getValorFormatado(), R.id.dtl_txvVenda);
				
				obterValor.setTextoTextView(venda.isVendaWeb() ? venda.getIdVendaWeb().toString() : "---", R.id.dtl_txvNumWeb);
				
				obterValor.setTextoTextView(venda.getId().toString(),
						R.id.dtl_txvNumVenda);

				int soma = 0;
				for(ItemVenda item : venda.getItensVenda()) {
					soma += item.getQuantidade();
				}
				
				obterValor.setTextoTextView(String.valueOf(soma),
						R.id.dtl_txvQtdePeca);
				
				List<ItemListView> itens = new ArrayList<ItemListView>(venda.getItensVenda().size());
				for (ItemVenda item : venda.getItensVenda()) {
					ItemListView itemView = new ItemListView(item);
					itemView.setQuantidade(item.getQuantidade());
					itens.add(itemView);
				}
				criarAdapterItem(new ItemSelecionadoAdapter(this,
						R.layout.listview_fonte_menor, itens), R.id.dtl_itensVenda);
				
			} else {
				mostarMensagem("Venda não carregada.");
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detalhamento, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mdv_voltar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
