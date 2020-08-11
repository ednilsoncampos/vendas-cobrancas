package br.com.actusrota.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.CidadeActivity;
import br.com.actusrota.activity.CidadeRotaActivity;
import br.com.actusrota.activity.ImportarVendaActivity;
import br.com.actusrota.activity.SuperActivity;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.util.UtilMensagem;

public class CidadeAdapter extends ArrayAdapter<Cidade> implements
		OnClickListener {

	private final SuperActivity<Cidade> context;
	private final List<Cidade> cidades;
	private Cidade cidade;
	private int ultimaPosicao = -1;

	public CidadeAdapter(SuperActivity<Cidade> context, List<Cidade> cidades) {
		super(context, R.layout.linha_cidade, cidades);
		this.context = context;
		this.cidades = cidades;
	}

	static class ViewHolder {
		protected TextView txtDescricao;
//		protected RadioButton rdbSelecionado;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();

		if (viewNull) {
			// LayoutInflater inflator = context.getLayoutInflater();
			LayoutInflater inflator = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.linha_cidade, null);
		}

		convertView
				.setBackgroundResource(android.R.drawable.menuitem_background);

		viewHolder.txtDescricao = (TextView) convertView
				.findViewById(R.id.lcid_txv_descricao);

//		viewHolder.rdbSelecionado = (RadioButton) convertView
//				.findViewById(R.id.lcid_rbSelecionado);

//		viewHolder.rdbSelecionado.setTag(String.valueOf(position));

		// viewHolder.rgSelecionado.setOnClickListener(this);

		convertView.setTag(viewHolder);

		Cidade cidade = super.getItem(position);
		if (cidade != null) {
			viewHolder.txtDescricao.setText(cidade.toString());
		}
		
//		viewHolder.rdbSelecionado.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// super.onListItemClick(l, v, position, id);
//				// setSelectedIndex(position);
//				// notifyDataSetChanged();
//				Object obj = null;
//				if (ultimaPosicao != -1) {
////					viewHolder.rdbSelecionado.setChecked(false);
//					obj = v.getTag(ultimaPosicao);
//				}
//				System.out.println(obj);
//				ultimaPosicao = position;
//				setCidade(getItem(position));
//			}
//		});
		
		return convertView;
	}

	private void sincronizarClientes(final Cidade cidade) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(cidade.toString())
				.setMessage("Deseja realmente importar clientes?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((CidadeActivity) context)
										.sincronizarClientes();//Não deve ser utilizado
							}

						}).setNegativeButton("Não", null).show();
	}

	@Override
	public void onClick(View view) {
		try {
			Object pos = view.getTag();

			if (pos != null) {
				final Cidade cidade = getItem(Integer.parseInt(pos.toString()));
				if (context instanceof CidadeActivity) {
					sincronizarClientes(cidade);
				} else if (context instanceof CidadeRotaActivity) {
					((CidadeRotaActivity) context).criarTelaComParametroRota(
							ImportarVendaActivity.class, cidade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMensagem.mostarMensagemLonga(context, e.getMessage());
		}
	}

	public void atualizar() {
		notifyDataSetChanged();
	}

	public void remover(Venda venda) {
		cidades.remove(venda);
		notifyDataSetChanged();
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
