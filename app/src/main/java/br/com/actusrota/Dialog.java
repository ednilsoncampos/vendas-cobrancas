package br.com.actusrota;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.util.UtilMensagem;

public class Dialog {
	private final Context contexto;
	private String titulo;
	private String label;
	private final EnumOperacao operacao;
	
	public Dialog(AcaoDialog dialogContexto, EnumOperacao operacao, String titulo, String label) {
		this.contexto = (Context) dialogContexto;
		this.operacao = operacao;
		this.label = label;
		this.titulo = titulo;
		criarDialog();
	}
	
	public void criarDialog() {
		LayoutInflater li = LayoutInflater.from(contexto);
		View promptsView = null;
		try {
			promptsView = li.inflate(R.layout.dialog_viagem, null);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				contexto);

		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setTitle(titulo);
		
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.dlg_edtCodigo);
		
		final TextView txvLabel = (TextView) promptsView
				.findViewById(R.id.dlg_txvLabel);
		txvLabel.setText(label);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Editable edtText = userInput.getEditableText();
						if (edtText != null && (edtText.toString().trim().length() > 0)) {
//							criarTelaItemAcerto(edtText.toString());	
							String string = edtText.toString();
							System.out.println(string);
							getAcaoDialog().execute(operacao, edtText.toString());
						} else {
							UtilMensagem.mostarMensagemCurta(contexto, "O código deve ser informado.");
						}
					}
				})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();		
	}
	
	public AcaoDialog getAcaoDialog() {
		return (AcaoDialog) contexto;
	}
	
}
