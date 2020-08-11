package br.com.actusrota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import br.com.actusrota.dao.ViagemDAO;
import br.com.actusrota.entidade.Viagem;
import br.com.actusrota.util.UtilMensagem;

//import android.support.v4.app.DialogFragment;

public class DialogViagem extends DialogFragment {
	private ViagemDAO viagemDAO = null;
	private final static String VIAGEM_SERVLET = "/actusrota/ViagemServlet?numViagem=";
	private final static String IMPORTAR = "&importarViagem=";
	
	public DialogViagem() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.dialog_viagem, null);
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.str_importarViagem,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								final EditText edtNumViagem = (EditText) dialogView
										.findViewById(R.id.dlg_edtCodigo);
								sincronizarViagem(edtNumViagem.getEditableText().toString());
							}
						})
				.setNegativeButton(R.string.str_cancelarImpViagem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DialogViagem.this.getDialog().cancel();
							}
						});
		return builder.create();
	}
	
	@SuppressWarnings("unchecked")
	public void sincronizarViagem(String numViagem) {
		try {
			if (numViagem == null) {
				UtilMensagem.mostarMensagemCurta(getDialog().getContext(),
						"Informe o cpf para prosseguir.");
				return;
			}
			new Importacao<Viagem>(getDialog().getContext(), Viagem.class, viagemDAO, VIAGEM_SERVLET + numViagem+IMPORTAR+true, true, false, false)
					.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

//	private void importarViagem() {
//		final ProgressDialog progressDialog = ProgressDialog.show(getDialog()
//				.getContext(), "Aguarde...", "Enviando dados para web!!", true);
//
//		final Toast aviso = Toast.makeText(getDialog().getContext(),
//				"Dados enviados com sucesso!", Toast.LENGTH_LONG);
//
//		new Thread(new Runnable() {
//
//			public void run() {
//				Viagem viagem;
//				try {
//					Thread.sleep(2000);
//					Sincronismo sincronismo = new Sincronismo();
//					viagem = sincronismo.importarViagem(1l);
//					Log.i("Retorno:", viagem.getId() + " viagem a inserir.");
//					processarViagem(viagem);
//				} catch (Exception e3) {
//					aviso.setText("Falha ao importar viagem:"
//							+ e3.getMessage());
//					Log.i("Falha ao importar viagem:", e3.getMessage());
//					e3.printStackTrace();
//				}
//				aviso.show();
//				progressDialog.dismiss();
//			}
//		}).start();
//	}
//
//	private void processarViagem(Viagem viagem) {
//		try {
//			getViagemDAO().adicionar(viagem);
//			Log.i("Sucesso!", "Produtos inseridos com sucesso!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private ViagemDAO getViagemDAO() {
		if (viagemDAO == null)
			viagemDAO = new ViagemDAO(getDialog().getContext());
		return viagemDAO;
	}
}
