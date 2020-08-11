package br.com.actusrota.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.actusrota.R;
import br.com.actusrota.activity.CidadeActivity;
import br.com.actusrota.activity.RotaActivity;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.util.UtilMensagem;

public class RotaAdapter extends ArrayAdapter<Rota> {

	private final RotaActivity context;
	private List<Rota> rotas;

	public RotaAdapter(RotaActivity context, List<Rota> rotas) {
		super(context, R.layout.linha_rota, rotas);
		this.context = context;
		this.rotas = rotas;
	}

	static class ViewHolder {
		protected TextView txtDescricao;
		protected ImageButton imgSincronizarCidade;
		protected ImageButton imgVerCidade;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			boolean viewNull = convertView == null;
			final ViewHolder viewHolder = viewNull ? new ViewHolder() : (ViewHolder) convertView.getTag();
			
			if (viewNull) {
				LayoutInflater inflator = context.getLayoutInflater();
				convertView = inflator.inflate(R.layout.linha_rota, null);
			}
			
			viewHolder.txtDescricao = (TextView) convertView
					.findViewById(R.id.rtl_txv_descricao);
			
			viewHolder.imgSincronizarCidade = (ImageButton) convertView
					.findViewById(R.id.rtl_imgSincronizarCidade);
			
			viewHolder.imgSincronizarCidade.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					final Rota rota = rotas.get(position);
					
					new AlertDialog.Builder(context)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(rota.getDescricao())
					.setMessage("Deseja realmente importar cidades?")
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
//									context.sincronizarCidades(rota);
								}

							}).setNegativeButton("Não", null).show();
				}
			});	
			
			viewHolder.imgVerCidade = (ImageButton) convertView
					.findViewById(R.id.rtl_imgVerCidade);
			
			viewHolder.imgVerCidade.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Rota rota = rotas.get(position);
					criarTelaCliente(rota);
				}
			});				
			
			convertView.setTag(viewHolder);					
			
			Rota rota = rotas.get(position);
			viewHolder.txtDescricao.setText(rota.getDescricao());
		} catch (Exception e) {
			e.printStackTrace();
			UtilMensagem.mostarMensagemLonga(context,
					"Detalhe do erro:getView:" + e.getMessage());
		}
		return convertView;
	}
	
	private void criarTelaCliente(Rota rota) {
		try {
			Intent intent = new Intent(context, CidadeActivity.class);
			intent.putExtra("rota", rota);
//			Bundle b = new Bundle();
//			b.putInt("key", rota);
			context.startActivity(intent);
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Erro criarTelaRota:", e.getMessage());
		}
	}
	
	public void atualizarRotas() {
//		this.rotas.clear();
//		this.rotas.addAll(rotas);
		notifyDataSetChanged();
	}

	public void setRotas(List<Rota> rotas) {
		this.rotas = rotas;
	}
	
}
