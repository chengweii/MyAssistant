package weihua.myassistant.util;

import java.util.Calendar;

public class DateUtil {

	public static long getTimeFromCurrent(int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTimeInMillis();
	}

	public static long getTimeFromTime(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTimeInMillis();
	}
}
