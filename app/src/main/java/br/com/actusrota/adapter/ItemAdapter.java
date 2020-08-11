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
import br.com.actusrota.enumerador.EnumGrupoProduto;
import br.com.actusrota.util.UtilMensagem;

public class ItemAdapter extends ArrayAdapter<ItemListView> {

	private final List<ItemListView> itens;
	private final Activity context;

	public ItemAdapter(Activity context, List<ItemListView> list) {
		super(context, R.layout.item_layout, list);
		this.context = context;
		this.itens = list;
	}

	static class ViewHolder {

		protected TextView txvCodigo;
		protected TextView txtGrupo;
		protected TextView txtDescricao;
		protected ImageButton imbAdicionar;

		void setarComponentes(View view) {
			txvCodigo = (TextView) view.findViewById(R.id.itlCodigo);
			txtGrupo = (TextView) view.findViewById(R.id.itl_grupo);
			txtDescricao = (TextView) view.findViewById(R.id.itl_DesProduto);
			imbAdicionar = (ImageButton) view
					.findViewById(R.id.itl_btnAdicionar);
		}

		public void setItem(ItemListView itemListView) {
			txvCodigo.setText(itemListView.getProduto().getCodigo());
			EnumGrupoProduto grupo = itemListView.getProduto().getGrupo();
			txtGrupo.setText(grupo.getDescricao());
			txtDescricao.setText(itemListView.getProduto().getDescricao());
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
				convertView = inflator.inflate(R.layout.item_layout, null);
			}

			viewHolder.setarComponentes(convertView);

			viewHolder.imbAdicionar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ItemListView item = getItem(position);
					if (context instanceof ItemActivity) {
						((ItemActivity) context).criarPrecoVendaActivity(item
								.getProduto());
					} else if (context instanceof ItemAcertoActivity) {
						((ItemAcertoActivity) context).criarPrecoVendaActivity(item
								.getProduto());
					}
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

	public void adicionarAdapter(ItemListView item) {
		if (!itens.contains(item)) {
			itens.add(item);
			notifyDataSetChanged();
		}
	}

	public void adicionarAdapter(Item item) {
		ItemListView itemView = new ItemListView(item);
		if (!itens.contains(itemView)) {
			itens.add(itemView);
			notifyDataSetChanged();
		}
	}
}
