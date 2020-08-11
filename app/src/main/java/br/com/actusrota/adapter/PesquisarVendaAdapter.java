package br.com.actusrota.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.SuperActivity;
import br.com.actusrota.entidade.Venda;

public class PesquisarVendaAdapter extends ArrayAdapter<Venda> {

	private final List<Venda> vendas;
	private final SuperActivity<Venda> contextVenda;
	private Venda venda;

	// public PesquisarVendaAdapter(SuperActivity<Venda> contextVenda,
	// List<Venda> vendas) {
	// super(contextVenda, R.layout.pesq_venda_adapter_layout, vendas);
	// this.contextVenda = contextVenda;
	// this.vendas = vendas;
	// }

	public PesquisarVendaAdapter(SuperActivity<Venda> contextVenda,
			List<Venda> vendas) {
		super(contextVenda, android.R.layout.simple_list_item_single_choice,
				R.layout.pesq_venda_adapter_layout, vendas);
		this.contextVenda = contextVenda;
		this.vendas = vendas;
	}

	static class ViewHolder {
		void setarComponentes(View view) {
			txvNumVenda = (TextView) view.findViewById(R.id.pva_txvNumVenda);
			txvCliente = (TextView) view.findViewById(R.id.pva_txvLabelCliente);
			txvTotal = (TextView) view.findViewById(R.id.pva_txvLabelTotal);
			txvStatus = (TextView) view.findViewById(R.id.pva_txvLabelStatus);
			radioGroup = (RadioGroup) view.findViewById(R.id.pva_radioGroup);
			rdbSelecione = (RadioButton) view.findViewById(R.id.pva_radio);
		}

		protected TextView txvNumVenda;
		protected TextView txvCliente;
		protected TextView txvTotal;
		protected TextView txvStatus;
		protected RadioGroup radioGroup;
		protected RadioButton rdbSelecione;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();
		try {
			if (viewNull) {
				LayoutInflater inflator = contextVenda.getLayoutInflater();
				convertView = inflator.inflate(
						R.layout.pesq_venda_adapter_layout, null);
			}
			
			viewHolder.setarComponentes(convertView);
			convertView.setTag(viewHolder);

			viewHolder.txvNumVenda.setText(String.valueOf(vendas.get(position)
					.getId()));

			viewHolder.txvCliente.setText(vendas.get(position).getCliente()
					.getNome());

			viewHolder.txvTotal.setText(vendas.get(position).getValorTotal()
					.getValorFormatado());

			viewHolder.txvStatus.setText(vendas.get(position).getStatusVenda()
					.getDescricao());

			viewHolder.rdbSelecione.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setVenda(vendas.get(position));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public void atualizar() {
		notifyDataSetChanged();
	}

	public void remover(Venda venda) {
		vendas.remove(venda);
		notifyDataSetChanged();
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
}
