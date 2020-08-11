package br.com.actusrota.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.entidade.ItemListView;

public class ItemSelecionadoAdapter extends ArrayAdapter<ItemListView> {
	private List<ItemListView> itens;
	private Context contexto;

	public ItemSelecionadoAdapter(Context context, int textViewResourceId,
			List<ItemListView> itens) {
		super(context, textViewResourceId, itens);
		this.contexto = context;
		this.itens = itens;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) contexto
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.linha_item_selecionado, null);
		}

		ItemListView item = itens.get(position);

		if (item != null) {
			TextView txvQuantidade = (TextView) convertView
					.findViewById(R.id.lis_txtQuantidade);
			TextView txvDescricao = (TextView) convertView
					.findViewById(R.id.lis_descProduto);
			TextView txvPrecoVenda = (TextView) convertView
					.findViewById(R.id.lis_precoVenda);

			if (txvQuantidade != null) {
				txvQuantidade.setText(String.valueOf(item.getQuantidade()));
			}

			if (txvDescricao != null) {
				txvDescricao.setText(item.getProduto().getDescricao());
			}

			if (txvPrecoVenda != null) {
				txvPrecoVenda.setText(item.getValorUnitario()
						.getValorFormatado());
			}
		}
		return convertView;
	}
}
