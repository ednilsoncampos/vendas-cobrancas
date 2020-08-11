package br.com.actusrota.util;

import android.content.Context;
import android.widget.Toast;

public class UtilMensagem {

	public static void mostarMensagemCurta(Context contexto, String msg) {
		Toast.makeText(contexto, msg,
				Toast.LENGTH_SHORT).show();		
	}
	
	public static void mostarMensagemLonga(Context contexto, String msg) {
		Toast.makeText(contexto, msg,
				Toast.LENGTH_LONG).show();		
	}	
	
}
