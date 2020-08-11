package br.com.actusrota.util;

import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class AdicionarListener {
	private ObterValor obterValor = null;

	public AdicionarListener(ObterValor obterValor) {
		this.obterValor = obterValor;
	}
	
	public void adicionarListener(OnClickListener litener, int id) {
		ImageButton imageButton = obterValor.getImageButton(id);
		imageButton.setOnClickListener(litener);
	}
	
	public void adicionarListener(TextWatcher litener, int id) {
		EditText edit = obterValor.getEditText(id);
		edit.addTextChangedListener(litener);
	}	
	
	public void adicionarListener(OnFocusChangeListener litener, int id) {
		EditText editText = obterValor.getEditText(id);
		editText.setOnFocusChangeListener(litener);
	}
}
