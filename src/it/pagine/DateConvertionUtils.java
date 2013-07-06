package it.pagine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class DateConvertionUtils {

	public static String fromJavaDateToPdfDate(Date time) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
				"'D:'yyyyMMddHHmmssZ");
		String s = DATE_FORMAT.format(time);
		int i = s.length() - 2;
		return s.substring(0, i) + "'" + s.substring(i) + "'";
	}

	public static Date fromPdfDateToJavaDate(String time) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
				"'D:'yyyyMMddHHmmssZ");
		String s = time.replaceAll("'", "");
		try {
			return DATE_FORMAT.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Calendar cc = GregorianCalendar.getInstance();
		cc.setTime(date);
		String output = df.format(date);

		String result = output.replaceAll("UTC", "");
		StringBuffer sb = new StringBuffer(result);
		writeTZ(cc, sb);
		return sb.toString();

	}

	protected static final void writeTZ(Calendar calendar, StringBuffer sbuf) {
		int offset = calendar.get(Calendar.ZONE_OFFSET)
				+ calendar.get(Calendar.DST_OFFSET);
		if (offset == 0) {
			sbuf.append('Z');
		} else {
			int offsetHour = offset / 3600000;
			int offsetMin = (offset % 3600000) / 60000;
			if (offset >= 0) {
				sbuf.append('+');
			} else {
				sbuf.append('-');
				offsetHour = 0 - offsetHour;
				offsetMin = 0 - offsetMin;
			}
			appendInt(sbuf, offsetHour, 2);
			sbuf.append(':');
			appendInt(sbuf, offsetMin, 2);
		}
	}

	protected static final void appendInt(StringBuffer buf, int value,
			int length) {
		int len1 = buf.length();
		buf.append(value);
		int len2 = buf.length();
		for (int i = len2; i < len1 + length; ++i) {
			buf.insert(len1, '0');
		}
	}

}//END