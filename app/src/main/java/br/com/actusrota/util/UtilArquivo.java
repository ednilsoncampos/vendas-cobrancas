package br.com.actusrota.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.actusrota.entidade.Usuario;

import android.app.Activity;
import android.content.Context;

public class UtilArquivo extends Activity {

	public static final String NOME_ARQUIVO = "login.txt";
	private static final String SEPARADOR = "/";
	private static final int DELIMITADOR = 4;

	public static void gravarArquivo(String dadosArquivo, String nomeArquivo,
			Context contexto) throws IOException {
		FileOutputStream fos = contexto.openFileOutput(nomeArquivo,
				Context.MODE_PRIVATE);
		fos.write(dadosArquivo.getBytes());
		fos.flush();
		fos.close();
	}

	public static String[] recuperarArquivo(Context contexto, String nomeArquivo)
			throws IOException {

		try {
			FileInputStream fis = contexto.openFileInput(nomeArquivo);
			byte[] bytesArquivo = new byte[256];
			fis.read(bytesArquivo);
			fis.close();

			String dadosArquivo = new String(bytesArquivo);
			String[] dadosArray = dadosArquivo.split(SEPARADOR);
			return dadosArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void gravarArquivo(Usuario usuario, String nomeArquivo,
			Context contexto) throws IOException {
		StringBuilder sb = new StringBuilder();

		sb.append(usuario.getEmpresa());
		sb.append(SEPARADOR);
		
		sb.append(usuario.getNomeUsuario());
		sb.append(SEPARADOR);
		
		sb.append(usuario.getSenhaHash());
		sb.append(SEPARADOR);
		
		sb.append(usuario.getCpf());
		sb.append(SEPARADOR);
		
		gravarArquivo(sb.toString(), nomeArquivo, contexto);
	}

	public static Usuario recuperarUsuario(Context contexto, String nomeArquivo)
			throws IOException {
		Usuario usuario = null;
		try {
			String[] dadosArray = recuperarArquivo(contexto, nomeArquivo);
			if (dadosArray != null && dadosArray.length >= DELIMITADOR) {
				usuario = new Usuario();
				usuario.setEmpresa(dadosArray[0]);
				usuario.setNomeUsuario(dadosArray[1]);
				usuario.setSenhaHash(dadosArray[2]);
				usuario.setCpf(dadosArray[3]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return usuario;
	}

}
