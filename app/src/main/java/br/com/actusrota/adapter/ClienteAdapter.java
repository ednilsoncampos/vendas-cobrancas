package br.com.actusrota.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.SuperActivity;
import br.com.actusrota.entidade.Cliente;

public class ClienteAdapter extends ArrayAdapter<Cliente> {
	private final SuperActivity<Cliente> context;
	private final List<Cliente> clientes;
	private Cliente cliente;

	public ClienteAdapter(SuperActivity<Cliente> context, List<Cliente> clientes) {
		super(context, R.layout.radio_layout, clientes);
		this.context = context;
		this.clientes = clientes;
	}

	static class ViewHolder {
		void setarComponentes(View view) {
			txtCodigo = (TextView) view.findViewById(R.id.rad_Texto1);
			txtDescricao = (TextView) view.findViewById(R.id.rad_Texto2);
			radioSelecione = (RadioButton) view.findViewById(R.id.rad_radio);
		}

		protected TextView txtCodigo;
		protected TextView txtDescricao;
		protected RadioButton radioSelecione;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();

		if (viewNull) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.radio_layout, null);
		}
		
		viewHolder.setarComponentes(convertView);
		convertView.setTag(viewHolder);

		Cliente cliente = clientes.get(position);
		viewHolder.txtCodigo.setText(String.valueOf(cliente.getId()));
		viewHolder.txtDescricao.setText(cliente.getNome() + " / "
				+ cliente.getCidade());

		viewHolder.radioSelecione.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (((RadioButton) view).isChecked()) {
					setCliente(getItem(position));
					((RadioButton) view).setChecked(true);
				} else {
					((RadioButton) view).setChecked(false);
				}
			}

		});

		return convertView;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
