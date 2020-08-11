package br.com.actusrota.adapter;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import br.com.actusrota.R;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.util.Mascara;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDates;

public class PagamentoAdapter extends ArrayAdapter<PagamentoCheque> {
	private final List<PagamentoCheque> pagamentos;
	private final Activity context;
	private ObterValor obterValor;

	public PagamentoAdapter(Activity context, List<PagamentoCheque> pagamentos) {
		super(context, R.layout.item_pagamento, pagamentos);
		this.context = context;
		this.pagamentos = pagamentos;
		obterValor = new ObterValor(context);
	}

	static class ViewHolder {
		void setarComponentes(View view) {
			edtNumeroCheque = (EditText) view
					.findViewById(R.id.ipg_edtNumeroCheque);
			edtValorParcela = (EditText) view
					.findViewById(R.id.ipg_edtValorParcela);
			edtDataVencimento = (EditText) view
					.findViewById(R.id.ipg_edtDataVencimento);
		}

		protected EditText edtNumeroCheque;
		protected EditText edtValorParcela;
		protected EditText edtDataVencimento;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean viewNull = convertView == null;
		final ViewHolder viewHolder = viewNull ? new ViewHolder()
				: (ViewHolder) convertView.getTag();
		try {
			if (viewNull) {
				LayoutInflater inflator = context.getLayoutInflater();
				convertView = inflator.inflate(R.layout.item_pagamento, null);
			} else {
				((ViewHolder) convertView.getTag()).edtDataVencimento
						.setTag(pagamentos.get(position));
			}

			viewHolder.setarComponentes(convertView);
			convertView.setTag(viewHolder);

			// viewHolder.edtDataVencimento.setInputType(InputType.TYPE_NULL);
			// viewHolder.edtNumeroCheque.setInputType(InputType.TYPE_NULL);
			// viewHolder.edtValorParcela.setInputType(InputType.TYPE_NULL);

//			final EditText edtDataVencimento = (EditText) convertView
//					.findViewById(R.id.ipg_edtDataVencimento);
//			edtDataVencimento.setInputType(InputType.TYPE_NULL);
//			obterValor.adicionarMascara(edtDataVencimento, "##/##/####");

			viewHolder.edtDataVencimento
					.addTextChangedListener(new EventoEditText(
							viewHolder.edtDataVencimento, position));

			viewHolder.edtNumeroCheque
					.addTextChangedListener(new EventoEditText(
							viewHolder.edtNumeroCheque, position));

			viewHolder.edtValorParcela
					.addTextChangedListener(new EventoEditText(
							viewHolder.edtValorParcela, position));

			viewHolder.edtNumeroCheque.setText(pagamentos.get(position)
					.getNumeroCheque());

			Dinheiro valorUnitario = pagamentos.get(position).getValor();
			if (valorUnitario != null)
				viewHolder.edtValorParcela.setText(valorUnitario
						.getValorFormatado());

			Date dataVenc = pagamentos.get(position).getDataPrevistaCompensar();
			if (dataVenc != null)
				viewHolder.edtDataVencimento.setText(UtilDates
						.formatarData(dataVenc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public List<PagamentoCheque> getPagamentos() {
		return pagamentos;
	}

	class EventoEditText implements TextWatcher {
		int position;
		View editView;

		public EventoEditText(View view, int position) {
			this.position = position;
			this.editView = view;
		}

		@Override
		public void afterTextChanged(Editable editable) {
			String text = editable.toString();

			switch (editView.getId()) {
			case R.id.ipg_edtNumeroCheque:
				pagamentos.get(position).setNumeroCheque(text);
				break;
			case R.id.ipg_edtDataVencimento:
				text = UtilDates.adicionarMascaraData(text);

//				((EditText) editView).setText(text);

				pagamentos.get(position).setDataPrevistaCompensar(
						UtilDates.formatarData(text));
				//aplica a mascara
				if(text.trim().length() == 8)
					notifyDataSetChanged();
				break;
			case R.id.ipg_edtValorParcela:
				pagamentos.get(position).setValor(Dinheiro.valueOf(text));
				break;
			}

		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

	}

}
