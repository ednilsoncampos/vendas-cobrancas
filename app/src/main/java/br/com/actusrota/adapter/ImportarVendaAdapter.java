package br.com.actusrota.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.SuperActivity;
import br.com.actusrota.entidade.Venda;

public class ImportarVendaAdapter extends ArrayAdapter<Venda> {
	private final List<Venda> vendas;
	private final SuperActivity<Venda> contextVenda;
	private String perguntaDialog;

	public ImportarVendaAdapter(SuperActivity<Venda> contextVenda,
			List<Venda> vendas, String perguntaDialog) {
		super(contextVenda, R.layout.linha_imp_venda_layout, vendas);
		this.contextVenda = contextVenda;
		this.vendas = vendas;
		this.perguntaDialog = perguntaDialog;
	}

	static class ViewHolder {
		void setarComponentes(View view) {
			txvNumVendaMovel = (TextView) view
					.findViewById(R.id.lip_txvLabelNumVendaMovel);
			txvNumVendaWeb = (TextView) view
					.findViewById(R.id.lip_txvLabelNumVendaWeb);
			txvCliente = (TextView) view.findViewById(R.id.lip_txvLabelCliente);
			txvTotal = (TextView) view.findViewById(R.id.lip_txvLabelTotal);
		}

		protected TextView txvNumVendaMovel;
		protected TextView txvNumVendaWeb;
		protected TextView txvCliente;
		protected TextView txvTotal;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();
		try {
			if (viewNull) {
				LayoutInflater inflator = contextVenda.getLayoutInflater();
				convertView = inflator.inflate(R.layout.linha_imp_venda_layout,
						null);
			}

			viewHolder.setarComponentes(convertView);
			convertView.setTag(viewHolder);
			
			viewHolder.txvCliente.setText(vendas.get(position).getCliente()
					.getNome());

			viewHolder.txvTotal.setText(vendas.get(position).getValorTotal()
					.getValorFormatado());
			
			if (vendas.get(position).isVendaWeb()) {
				viewHolder.txvNumVendaWeb.setText(String.valueOf(vendas.get(
						position).getIdVendaWeb()));
			} else {
				viewHolder.txvNumVendaWeb.setText("--");
			}
			
			if (vendas.get(position).isVendaMovel()) {
				viewHolder.txvNumVendaMovel.setText(String.valueOf(vendas.get(
						position).getIdMovel()));
			} else if (!vendas.get(position).isNovo()) {
				viewHolder.txvNumVendaMovel.setText(String.valueOf(vendas.get(
						position).getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public void atualizar() {
		notifyDataSetChanged();
	}
}
