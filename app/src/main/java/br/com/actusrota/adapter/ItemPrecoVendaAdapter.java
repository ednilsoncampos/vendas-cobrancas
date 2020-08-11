package br.com.actusrota.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.ItemAcertoActivity;
import br.com.actusrota.activity.ItemActivity;
import br.com.actusrota.entidade.Item;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemManter;
import br.com.actusrota.entidade.ItemTabelaPreco;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.util.UtilMensagem;

public class ItemPrecoVendaAdapter extends ArrayAdapter<ItemListView> {

	private final List<ItemListView> itens;
	private final Activity context;
	private final Venda venda;

	public ItemPrecoVendaAdapter(Activity context, List<ItemListView> list,
			Venda venda) {
		super(context, R.layout.item_preco_venda_adapter, list);
		this.context = context;
		this.itens = list;
		this.venda = venda;
	}

	static class ViewHolder {

		protected TextView txtDescricao;
		protected TextView txvQuantidade;
		protected TextView txtPrecoVenda;
		protected TextView txtTotalItem;
		protected ImageButton btnEditar;
		protected ImageButton btnExcluir;

		void setarComponentes(View view) {
			txvQuantidade = (TextView) view.findViewById(R.id.itpva_quantidade);
			txtDescricao = (TextView) view.findViewById(R.id.itpva_DesProduto);
			txtPrecoVenda = (TextView) view
					.findViewById(R.id.itpva_preco_venda);
			txtTotalItem = (TextView) view.findViewById(R.id.itpva_total_item);
			btnEditar = (ImageButton) view.findViewById(R.id.itpva_btnEditar);
			btnExcluir = (ImageButton) view.findViewById(R.id.itpva_btnExcluir);
		}

		public void setItem(ItemListView itemListView) {
			txtDescricao.setText(itemListView.getProduto().getDescricao());
			txvQuantidade.setText(String.valueOf(itemListView.getQuantidade()));
			txtPrecoVenda.setText(itemListView.getValorUnitario()
					.getValorFormatado());
			txtTotalItem.setText(itemListView.getTotalItem()
					.getValorFormatado());
		}

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();

		try {

			if (viewNull) {
				LayoutInflater inflator = context.getLayoutInflater();
				convertView = inflator.inflate(
						R.layout.item_preco_venda_adapter, null);
			}

			viewHolder.setarComponentes(convertView);

			viewHolder.btnEditar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// if (venda.isVendaWeb()) {
					// UtilMensagem.mostarMensagemCurta(context,
					// "Venda web não pode ser alterada.");
					// return;
					// }
					ItemListView item = getItem(position);
					ItemTabelaPreco itemPreco = new ItemTabelaPreco(item);
					if (context instanceof ItemActivity) {
						((ItemActivity) context)
								.criarPrecoVendaActivity(itemPreco);
					} else if (context instanceof ItemAcertoActivity) {
						((ItemAcertoActivity) context)
								.criarPrecoVendaActivity(itemPreco);
					}
				}
			});

			viewHolder.btnExcluir.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (venda.isVendaWeb()) {
						UtilMensagem.mostarMensagemCurta(context,
								"Venda web não pode ser alterada.");
						return;
					}
					ItemListView item = getItem(position);
					removerItem(item);
				}
			});

			convertView.setTag(viewHolder);
			viewHolder.setItem(getItem(position));
		} catch (Exception e) {
			e.printStackTrace();
			UtilMensagem.mostarMensagemLonga(context,
					"Detalhe do erro:getView:" + e.getMessage());
		}
		return convertView;
	}

	public List<ItemListView> getItens() {
		return itens;
	}

	public <T extends ItemManter> List<T> getItensSelecionaods() {
		return null;
	}

	public void adicionarAdapter(ItemListView item) {
		if (!itens.contains(item)) {
			itens.add(item);
			notifyDataSetChanged();
		}
	}

	public void removerItem(ItemListView item) {
		remove(item);
	}

	public void adicionarAdapter(Item item) {
		ItemListView itemView = new ItemListView(item);
		if (!itens.contains(itemView)) {
			itens.add(itemView);
			notifyDataSetChanged();
		}
	}

	public void alterar(ItemListView itemView) {
		if (!itens.contains(itemView)) {
			itens.add(itemView);
		} else {
			// itens.remove(itemView);
			for (ItemListView item : itens) {
				if (item.equals(itemView)) {
					itemView.setQuantidade(item.getQuantidade()+itemView.getQuantidade());
					itens.add(itemView);
					break;
				}
			}
		}
		notifyDataSetChanged();
	}
}