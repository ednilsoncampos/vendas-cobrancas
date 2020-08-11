package br.com.actusrota;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import br.com.actusrota.util.UtilDates;

public class CalendarioData {
	
	private DatePicker datePicker;
	private int ano;
	private int mes;
	private int dia;
	public static final int DATE_DIALOG_ID = 999;
	private Activity contexto;
	private Date dataFormatada;
	
	public CalendarioData(Activity contexto) {
		super();
		this.contexto = contexto;
		criarDatePicker();
	}
	
	public DatePickerDialog criar() {
		return new DatePickerDialog(contexto, datePickerListener, ano, mes,
				dia);		
	}
	
	public DatePickerDialog alterar(Date data) {
		final Calendar c = Calendar.getInstance();
		c.setTime(data);
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH);
		dia = c.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(contexto, datePickerListener, ano, mes,
				dia);		
	}
	
	private void criarDatePicker() {
		datePicker = (DatePicker) contexto.findViewById(R.id.vnd_dtpDataVenda);

		final Calendar c = Calendar.getInstance();
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH);
		dia = c.get(Calendar.DAY_OF_MONTH);

		datePicker.init(ano, mes, dia, null);
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			ano = selectedYear;
			mes = selectedMonth;
			dia = selectedDay;

			// seta a data selecionada
			datePicker.init(ano, mes, dia, null);
		}
	};	
	
	private void formatarData() {
		String dataString = datePicker.getDayOfMonth() + "/"
				+ (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
		try {
			dataFormatada = UtilDates.formatarData(dataString);
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

	public Date getDataFormatada() {
		formatarData();
		return dataFormatada;
	}
	
}
