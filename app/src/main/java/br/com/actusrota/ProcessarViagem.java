package br.com.actusrota;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;
import br.com.actusrota.dao.ViagemDAO;
import br.com.actusrota.entidade.Viagem;
import android.content.Context;

public class ProcessarViagem {
	
	private ViagemDAO viagemDAO = null;
	private Context contexto;
	
	public ProcessarViagem(Context contexto) {
		this.contexto = contexto;
	}
	
	public void importarViagem(final Long numViagem) {
		final ProgressDialog progressDialog = ProgressDialog.show(contexto, "Aguarde...", "Enviando dados para web!!", true);

		final Toast aviso = Toast.makeText(contexto,
				"Dados enviados com sucesso!", Toast.LENGTH_LONG);

		new Thread(new Runnable() {

			public void run() {
				Viagem viagem = null;
				try {
					Thread.sleep(2000);
					ConexaoHttp sincronismo = new ConexaoHttp(true,false);
					Log.i("Retorno:", viagem.getId() + " viagem a inserir.");
					processarViagem(viagem);
				} catch (Exception e3) {
					aviso.setText("Falha ao importar viagem:"
							+ e3.getMessage());
					Log.i("Falha ao importar viagem:", e3.getMessage());
					e3.printStackTrace();
				}
				aviso.show();
				progressDialog.dismiss();
			}
		}).start();
	}

	protected void processarViagem(Viagem viagem) {
		try {
			getViagemDAO().adicionar(viagem);
			Log.i("Sucesso!", "Produtos inseridos com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ViagemDAO getViagemDAO() {
		if (viagemDAO == null)
			viagemDAO = new ViagemDAO(contexto);
		return viagemDAO;
	}
}
