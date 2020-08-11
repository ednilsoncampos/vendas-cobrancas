package br.com.actusrota.util;

import java.util.Date;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.actusrota.entidade.Dinheiro;

public class ObterValor {
	private Activity activity;

	public ObterValor(Activity activity) {
		this.activity = activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getValorEdit(int id) {
		EditText edit = (EditText) activity.findViewById(id);
		Editable editableText = edit.getEditableText();
		String valor = null;
		if (editableText != null)
			valor = editableText.toString();
		return valor;
	}

	public Dinheiro getValorMonetarioEdit(int id) {
		String valor = getValorEdit(id);
		return (valor == null || valor.trim().length() == 0) ? Dinheiro.novo() : Dinheiro.valueOf(valor);
	}

	public Dinheiro getValorMonetarioTextView(int id) {
		String valor = getTextoTextView(id);
		return Dinheiro.valueOf(valor);
	}

	public String getTextoTextView(int id) {
		CharSequence text = getTextView(id).getText();
		return text == null ? null : text.toString();
	}

	public TextView getTextView(int id) {
		TextView textView = (TextView) activity.findViewById(id);
		return textView;
	}

	public EditText getEditText(int id) {
		EditText edit = (EditText) activity.findViewById(id);
		return edit;
	}

	public ImageButton getImageButton(int id) {
		ImageButton imgButton = (ImageButton) activity.findViewById(id);
		return imgButton;
	}
	
	public void setFocusImagemButton(int id) {
		getImageButton(id).requestFocus();
	}

	public Button getButton(int id) {
		Button botao = (Button) activity.findViewById(id);
		return botao;
	}

	public void setFocusBotao(int id) {
		getButton(id).requestFocus();
	}

	// **************seter************
	public void setTextoEditText(String texto, int id) {
		EditText edit = getEditText(id);
		edit.setText(texto);
	}

	public void addTextoTextView(String texto, int id) {
		TextView txtView = getTextView(id);
		Editable editableText = txtView.getEditableText();
		if (editableText != null)
			txtView.setText(editableText.toString() + texto);
		else
			txtView.setText(texto);
	}

	public void setTextoTextView(String texto, int id) {
		TextView txtView = getTextView(id);
		txtView.setText(texto);
	}

	public boolean isInValidoValorEdit(int id) {
		String valorEdit = getValorEdit(id);
		return UtilString.isValorInvalido(valorEdit);
	}

	public boolean isInValidoValorTextView(int id) {
		String texto = getTextoTextView(id);
		return UtilString.isValorInvalido(texto);
	}

	/**
	 * Este método desativa o OnFocusChangeListener
	 * 
	 * @param id
	 */
	public void desabilitarTecladoVirtual(int id) {
		getEditText(id).setInputType(InputType.TYPE_NULL);
	}

	public void adicionarMascara(int id, String padrao) {
		EditText edtMask = getEditText(id);
		edtMask.addTextChangedListener(Mascara.insert(padrao, edtMask));
	}

	public void adicionarMascara(EditText edtMask, String padrao) {
		edtMask.addTextChangedListener(Mascara.insert(padrao, edtMask));
	}

	public void setDataEditText(int id, Date data) {
		getEditText(id).setText(UtilDates.formatarData(data));
	}

	public Date getDataEditText(int id) {
		String valorEdit = getValorEdit(id);
		return UtilDates.formatarData(valorEdit);
	}

	public String getDataStringEditText(int id) {
		// String valorEdit = getValorEdit(id);
		// return UtilDates.formatarData(valorEdit);
		return getValorEdit(id);
	}

	public void habilitarTecladoVirtual(int id) {
		getEditText(id).setInputType(InputType.TYPE_NULL);
	}

}
