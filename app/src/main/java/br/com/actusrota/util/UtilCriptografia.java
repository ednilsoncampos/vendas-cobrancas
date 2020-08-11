package br.com.actusrota.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilCriptografia {

	public synchronized static String toHash(String senha) {
		MessageDigest md = null;
		String ret = "";
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(senha.getBytes());
			byte[] hashMd5 = md.digest();
			ret = stringHexa(hashMd5);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return ret;
	}

	private synchronized static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}
}
