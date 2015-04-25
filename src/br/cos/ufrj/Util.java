package br.cos.ufrj;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Fabrício PEREIRA, COPPE/UFRJ/PESC
 * @email fabriciorsf@gmail.com
 */
public class Util {

	public static final Locale LOCALE_PTBR = new Locale("pt", "BR");

	/* **************** BEGIN: STRING **************** */

	public static boolean isNullOrEmpty(String str) {
		return ((str == null) || str.trim().isEmpty());
	}

	public static String getNotEmpty(String str) {
		return (isNullOrEmpty(str) ? null : str.trim());
	}

	/* **************** END: STRING **************** */

	/* **************** BEGIN: DATE **************** */
	public static String[] getMonthNames(Locale locale) {
		return (new DateFormatSymbols(locale)).getMonths();
	}

	public static int getMinute(Date date) {
		return get(date, Calendar.MINUTE);
	}

	public static int getHour(Date date) {
		return get(date, Calendar.HOUR_OF_DAY);
	}

	public static int getDayOfMonth(Date date) {
		return get(date, Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfWeek(Date date) {
		return get(date, Calendar.DAY_OF_WEEK);
	}

	public static int getMonth(Date date) {
		return get(date, Calendar.MONTH);
	}

	public static int getYear(Date date) {
		return get(date, Calendar.YEAR);
	}

	public static Date setMinute(Date date, int value) {
		return set(date, Calendar.MINUTE, value);
	}

	public static Date setHour(Date date, int value) {
		return set(date, Calendar.HOUR_OF_DAY, value);
	}

	public static Date setDayOfMonth(Date date, int value) {
		return set(date, Calendar.DAY_OF_MONTH, value);
	}

	public static Date setDayOfWeek(Date date, int value) {
		return set(date, Calendar.DAY_OF_WEEK, value);
	}

	public static Date setMonth(Date date, int value) {
		return set(date, Calendar.MONTH, value);
	}

	public static Date setYear(Date date, int value) {
		return set(date, Calendar.YEAR, value);
	}

	private static int get(Date date, int field) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(field);
	}

	private static Date set(Date date, int field, int value) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(field, value);
		return calendar.getTime();
	}

	public static long diffDate(Date date1, Date date2, int field) {
		long milliseconds1 = date1.getTime();
		long milliseconds2 = date2.getTime();
		long diffMilliseconds = milliseconds2 - milliseconds1;
		long diff;
		switch (field) {
		case Calendar.MILLISECOND:
			diff = diffMilliseconds;
			break;
		case Calendar.SECOND:
			diff = diffMilliseconds / 1000;
			break;
		case Calendar.MINUTE:
			diff = diffMilliseconds / (60 * 1000);
			break;
		case Calendar.HOUR:
		case Calendar.HOUR_OF_DAY:
			diff = diffMilliseconds / (60 * 60 * 1000);
			break;
		case Calendar.DAY_OF_MONTH:
		case Calendar.DAY_OF_WEEK:
		case Calendar.DAY_OF_WEEK_IN_MONTH:
		case Calendar.DAY_OF_YEAR:
			diff = diffMilliseconds / (24 * 60 * 60 * 1000);
			break;
		case Calendar.WEEK_OF_MONTH:
		case Calendar.WEEK_OF_YEAR:
			diff = diffMilliseconds / (7 * 24 * 60 * 60 * 1000);
			break;
		case Calendar.MONTH:
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date1);
			long m1 = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
			calendar.setTime(date2);
			long m2 = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
			diff = m2 - m1 + 1;
			break;
		default:
			diff = diffMilliseconds;
			break;
		}
		return diff;
	}

	public static Date incrementMillisecond(Date date, int amount) {
		return incrementDate(date, Calendar.MILLISECOND, amount);
	}

	public static Date incrementMinute(Date date, int amount) {
		return incrementDate(date, Calendar.MINUTE, amount);
	}

	public static Date incrementDayOfYear(Date date, int amount) {
		return incrementDate(date, Calendar.DAY_OF_YEAR, amount);
	}

	public static Date incrementDayOfMonth(Date date, int amount) {
		return incrementDate(date, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date incrementMonth(Date date, int amount) {
		return incrementDate(date, Calendar.MONTH, amount);
	}

	public static Date incrementYear(Date date, int amount) {
		return incrementDate(date, Calendar.YEAR, amount);
	}

	private static Date incrementDate(Date date, int field, int amount) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * Retorna o valor do horário minimo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data retornada por este metodo será
	 * "30/01/2009 as 00h:00m:00s e 000ms".
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o horário minimo para dia informado.
	 */
	public static Date lowDateTime(Date date) {
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toOnlyDate(aux); // zera os parametros de hour,min,sec,milisec
		return aux.getTime();
	}

	/**
	 * Retorna o valor do horário maximo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data retornada por este metodo será
	 * "30/01/2009 as 23h:59m:59s e 999ms".
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o horário maximo para dia informado.
	 */
	public static Date highDateTime(Date date) {
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toFinalDate(aux); // maximiza os parametros de hour,min,sec,milisec
		return aux.getTime();
	}

	public static Date highDateOfMonth(Date date) {
		date = incrementDate(date, Calendar.MONTH, 1);
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toOnlyDate(aux);
		aux.set(Calendar.DAY_OF_MONTH, 1);
		aux.add(Calendar.MILLISECOND, -1);
		return aux.getTime();
	}

	public static Date minDateOfMonth(Date date) {
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toOnlyDate(aux);
		aux.set(Calendar.DAY_OF_MONTH, 1);
		return aux.getTime();
	}

	/**
	 * Zera todas as referencias de hora, minuto, segundo e milesegundo do {@link Calendar}.
	 * 
	 * @param date
	 *            a ser modificado.
	 */
	private static void toOnlyDate(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Maximiza todas as referencias de hora, minuto, segundo e milesegundo do {@link Calendar}.
	 * 
	 * @param date
	 *            a ser modificado.
	 */
	private static void toFinalDate(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 23);
		date.set(Calendar.MINUTE, 59);
		date.set(Calendar.SECOND, 59);
		date.set(Calendar.MILLISECOND, 999);
	}

	public static Date max(Date date1, Date date2) {
		if (date1.compareTo(date2) > 0) {
			return date1;
		}
		return date2;
	}

	public static Date min(Date date1, Date date2) {
		if (date1.compareTo(date2) > 0) {
			return date2;
		}
		return date1;
	}

	public static String getMonthStr(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, LOCALE_PTBR);
	}

	public static String getMesReferenciaStr(Date date, String separador) {
		if (date != null) {
			return (getMonthStr(date) + separador + String.valueOf((getYear(date))));
		} else
			return null;
	}

	public static String getMesReferenciaStr(Date date) {
		return getMesReferenciaStr(date, ", ");
	}

	public static String formatDate(Date date, String pattern) {
		return (date != null) ? (new SimpleDateFormat(pattern)).format(date) : null;
	}

	public static String formatOnlyDate(Date date) {
		return formatDate(date, "dd/MM/yyyy");
	}

	public static String formatOnlyHorary(Date date) {
		return formatDate(date, "HH:mm");
	}

	public static String reformatDate(String dataStr, String formatIn, String formatOut) {
		List<String> formatsIn = new ArrayList<String>(1);
		formatsIn.add(formatIn);
		return reformatDate(dataStr, formatsIn, formatOut);
	}

	public static String reformatDate(String dataStr, List<String> formatsIn, String formatOut) {
		String dataOutStr = null;
		Date data = parseDate(dataStr, formatsIn);
		if (data != null) {
			dataOutStr = formatDate(data, formatOut);
		}
		return dataOutStr;
	}

	public static Date parseDate(String dataStr, String formatIn) {
		List<String> formatsIn = new ArrayList<String>(1);
		formatsIn.add(formatIn);
		return parseDate(dataStr, formatsIn);
	}

	public static Date parseDate(String dataStr, List<String> formatsIn) {
		Date data = null;
		if (!isNullOrEmpty(dataStr)) {
			Exception parseException = null;
			for (String formatIn : formatsIn) {
				try {
					if (dataStr.length() == formatIn.length()) {
						SimpleDateFormat df = new SimpleDateFormat(formatIn);
						data = df.parse(dataStr);
						parseException = null;
						break;
					}
				} catch (Exception e) {
					parseException = e;
				}
			}
			if (parseException != null) {
				parseException.printStackTrace();
			}
		}
		return data;
	}

	public static String formatDataSinob(Object object, String formatIn, String formatOut) {
		List<String> formatsIn = new ArrayList<String>();
		formatsIn.add(formatIn);
		return formatDataSinob(object, formatsIn, formatOut);
	}

	public static String formatDataSinob(Object object, List<String> formatsIn, String formatOut) {
		if (object == null) {
			return null;
		}
		String dataStr = object.toString();
		Date data = null;
		Exception parseException = null;
		for (String formatIn : formatsIn) {
			try {
				data = dateFormatWithTimeZone(object, formatIn);
				parseException = null;
				break;
			} catch (Exception e) {
				parseException = e;
			}
		}
		if (parseException != null) {
			parseException.printStackTrace();
		}
		String dataOutStr = dataStr;
		if (data != null) {
			dataOutStr = formatDate(data, formatOut);
			if (formatOut.endsWith("Z")) {
				dataOutStr = dataOutStr.substring(0, dataOutStr.length() - 2) + ":"
						+ dataOutStr.substring(dataOutStr.length() - 2, dataOutStr.length());
			}
		}
		return dataOutStr;
	}

	public static Date dateFormatWithTimeZone(Object object, String formatIn) throws ParseException {
		String dataStr = object.toString();
		if (formatIn.endsWith("Z")) {
			int index = dataStr.lastIndexOf(':');
			String timeZoneHour = "";
			if (dataStr.substring(0, index).endsWith("-02")) {
				timeZoneHour = "-03";
			}
			object = dataStr.substring(0, index - timeZoneHour.length()) + timeZoneHour
					+ dataStr.substring(index + 1, dataStr.length());
		}

		SimpleDateFormat df = new SimpleDateFormat(formatIn);
		return df.parse(object.toString());
	}

	public static Boolean checkRangeDate(Date data) {
		if ((getYear(data) > 9999) || (getMonth(data) > 12) || (getDayOfMonth(data) > 31)) {
			return false;
		}

		return true;
	}

	/**
	 * Valida se a String com a data informada está correta no padrão dd/MM/yyyy e se esta é menor ou igual a data atual
	 * 
	 * @param data
	 *            String contendo a data
	 * @return TRUE se passar pelas validações e não houver nenhum erro. FALSE caso contrário.
	 */
	public static Boolean validaDataDiaMesAno(String data) {
		try {
			if (data.length() != 10 || Integer.parseInt(data.substring(0, 2)) < 1 || Integer.parseInt(data.substring(0, 2)) > 31
					|| Integer.parseInt(data.substring(3, 5)) < 1 || Integer.parseInt(data.substring(3, 5)) > 12) {
				return false;
			}

			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(data);
			if (date.after(new Date())) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static Date setDate(int dia, int mes, int ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, dia);
		toOnlyDate(calendar);
		return calendar.getTime();
	}

	/* ***************** END: DATE ***************** */

}
