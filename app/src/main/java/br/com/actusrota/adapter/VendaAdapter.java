package br.com.actusrota.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.ExportarVendaActivity;
import br.com.actusrota.activity.SuperActivity;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumStatusModificacao;
import br.com.actusrota.enumerador.EnumStatusSincronizar;
import br.com.actusrota.util.UtilMensagem;

public class VendaAdapter extends ArrayAdapter<Venda> {
	private final List<Venda> vendas;
	private final SuperActivity<Venda> contextVenda;
	private String perguntaDialog;

	public VendaAdapter(SuperActivity<Venda> contextVenda, List<Venda> vendas,
			String perguntaDialog) {
		super(contextVenda, R.layout.linha_venda_layout, vendas);
		this.contextVenda = contextVenda;
		this.vendas = vendas;
		this.perguntaDialog = perguntaDialog;
	}

	static class ViewHolder {
		void setarComponentes(View view) {
			txvNumVenda = (TextView) view.findViewById(R.id.lvn_txvNumVenda);
			txvCliente = (TextView) view.findViewById(R.id.lvn_txvLabelCliente);
			txvTotal = (TextView) view.findViewById(R.id.lvn_txvLabelTotal);
			// txvStatusVenda = (TextView)
			// view.findViewById(R.id.lvn_txvLabelStatus);
			txvStatusServidor = (TextView) view
					.findViewById(R.id.lvn_txvLabelServidor);
			btnEnviar = (ImageButton) view.findViewById(R.id.lvn_btnEnviar);
		}

		protected TextView txvNumVenda;
		protected TextView txvCliente;
		protected TextView txvTotal;
		// protected TextView txvStatusVenda;
		protected TextView txvStatusServidor;
		protected ImageButton btnEnviar;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();
		try {
			if (viewNull) {
				LayoutInflater inflator = contextVenda.getLayoutInflater();
				convertView = inflator.inflate(R.layout.linha_venda_layout,
						null);
			}

			viewHolder.setarComponentes(convertView);
			convertView.setTag(viewHolder);

			Venda venda = vendas.get(position);

			if (venda.isVendaWeb()) {
				viewHolder.txvNumVenda.setText(String.valueOf(venda
						.getIdVendaWeb()));
			} else {
				viewHolder.txvNumVenda.setText("--");
			}

			viewHolder.txvCliente.setText(venda.getCliente().getNome());

			viewHolder.txvTotal.setText(venda.getValorTotal()
					.getValorFormatado());

			EnumStatusSincronizar statusSincronizar = venda
					.getStatusSincronizar();
			viewHolder.txvStatusServidor.setText(statusSincronizar
					.getDescricao());

			switch (statusSincronizar) {
			case ENVIADA:
				viewHolder.txvStatusServidor.setTextColor(contextVenda
						.getResources().getColor(R.color.green));
				break;
			case NAO_ENVIADA:
				viewHolder.txvStatusServidor.setTextColor(contextVenda
						.getResources().getColor(R.color.red));
				break;
			default:
				break;
			}

			viewHolder.btnEnviar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Venda venda = vendas.get(position);

					if (contextVenda instanceof ExportarVendaActivity) {
						if (EnumStatusModificacao.NAO_MODIFICADA.equals(venda
								.getStatusModificacao())) {
							UtilMensagem
									.mostarMensagemLonga(contextVenda,
											"Venda 'Não Modificada' não pode ser Sincronizada.");
							return;
						}
					}

					contextVenda.confirmar(perguntaDialog, venda);
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
}
