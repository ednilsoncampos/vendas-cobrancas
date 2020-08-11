package br.com.actusrota.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.entidade.ItemListView;

public class ItemViagemAdapter extends ArrayAdapter<ItemListView> {
	private final List<ItemListView> itens;
	private final Activity context;

	public ItemViagemAdapter(Activity context, List<ItemListView> list) {
		super(context, R.layout.linha_text_view, list);
		this.context = context;
		this.itens = list;
	}
	
	static class ViewHolder {
		void setarComponentes(View view) {
			txtCodigo = (TextView) view.findViewById(R.id.ltv_txvLinhaUm);
			txtDescricao = (TextView) view.findViewById(R.id.ltv_txvLinhaDois);
			txtQuantidade = (TextView) view.findViewById(R.id.ltv_txvLinhaTres);
		}

		protected TextView txtQuantidade;
		protected TextView txtCodigo;
		protected TextView txtDescricao;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();
		try {
			if (viewNull) {
				LayoutInflater inflator = context.getLayoutInflater();
				convertView = inflator.inflate(R.layout.linha_text_view, null);
			} else {
				((ViewHolder) convertView.getTag()).txtDescricao.setTag(itens
						.get(position));
			}

			viewHolder.setarComponentes(convertView);
			convertView.setTag(viewHolder);
			
			ItemListView item = itens.get(position);

			if (item != null) {
				viewHolder.txtQuantidade.setText(String.valueOf(item.getQuantidade()));
				viewHolder.txtCodigo.setText(String.valueOf(item.getProduto().getCodigo()));
				
				viewHolder.txtDescricao.setText(item.getProduto()
						.getDescricao());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
