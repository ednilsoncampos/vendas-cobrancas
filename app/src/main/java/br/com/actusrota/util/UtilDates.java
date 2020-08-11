package br.com.actusrota.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtilDates {
	
	private static final String FORMATO = "dd/MM/yyyy";
	
	private UtilDates() {
	}
	
	public static Date formatarData(String dataString) {
		DateFormat dfCriarData = new SimpleDateFormat(FORMATO);

		Date data = null;
		try {
			data = dfCriarData.parse(dataString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}	
	
	public static String formatarData(Date data) {
		DateFormat dfCriarData = new SimpleDateFormat(FORMATO);
		try {
			return dfCriarData.format(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * Não pode ser utilizado para validar data, apenas adiciona a mascara
	 * @param data
	 * @return
	 */
	public static String adicionarMascaraData(String data) {
			if(data.trim().length() == 8 ) {
				data = new StringBuilder(data).insert(2,"/").insert(5, "/").toString();
			}
			return data;
	}	

	/**
	 * Adiciona um mÃªs a uma data atual, e define o dia com o valor diaLimite
	 */
	public static Calendar defineDataLimite(Date dataAtual, Integer diaLimite) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataAtual);

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, diaLimite);

		return calendar;
	}
	
	public static Date adicionarMes(Date dataRef, int qtdMeses) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataRef);

		calendar.add(Calendar.MONTH, qtdMeses);
		return calendar.getTime();
	}

	public static Date definerPrimeiroDiaAno(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return calendar.getTime();
	}

	public static Date definerUltimoDiaAno(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);

		return calendar.getTime();
	}

	public static Date buscarPrimeiroDiaMes(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date buscarUltimoDiaMes(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static String mesPorExtenso(Date data) {
		Calendar calendarMsg = Calendar.getInstance();
		calendarMsg.setTime(data);
		return new DateFormatSymbols(Locale.getDefault()).getMonths()[calendarMsg
				.get(Calendar.MONTH)];
	}

	public static int buscarDiaDaSemana(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int extrairAno(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}

	public static Date getPrimeiroDia(Integer ano, Integer mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMinimum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	public static Date getUltimoDia(Integer ano, Integer mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	public static Integer getMesAtual() {
		Calendar cal = Calendar.getInstance();

		return cal.get(Calendar.MONTH);
	}

	public static Integer getAnoAtual() {
		Calendar cal = Calendar.getInstance();

		return cal.get(Calendar.YEAR);
	}

	public static Date setTime(Date data, int hour, int minute, int sec,
			int milliSec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, milliSec);

		return calendar.getTime();
	}

}
