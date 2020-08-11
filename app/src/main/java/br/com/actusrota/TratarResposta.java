package br.com.actusrota;

import android.content.Context;
import android.widget.Toast;

public class TratarResposta {

	private Context contexto;

	public TratarResposta(Context contexto) {
		this.contexto = contexto;
	}

	public void tratarResposta(String resultado) {
		if(resultado == null || resultado.trim().length() == 0) {
			Toast.makeText(contexto, "Sem reposta do servidor", Toast.LENGTH_LONG).show();
			return;
		}
		
		String[] split = resultado.split("#");
		boolean splitDiferenteNull = split != null;

		StringBuilder sBuilder = new StringBuilder();
		try {
			if (splitDiferenteNull) {
				Toast.makeText(contexto, split[0], Toast.LENGTH_LONG).show();

				sBuilder.append("Detalhe do erro: ");

				if (split.length > 1) {
					String[] split2 = split[1].split(":");
					sBuilder.append(split2.length == 1 ? split2[0] : split2[1]);
				} else {
					return;
				}

				Toast.makeText(contexto, sBuilder, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(contexto,
						"Não foi possível sincronizar os dados.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(contexto,
					"Erro lendo resposta:" + sBuilder + ". " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}
}
