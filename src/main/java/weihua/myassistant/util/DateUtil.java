package weihua.myassistant.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static void main(String[] args) throws Exception {
		System.out.println(getCurrentTimePeriod().getValue());
	}

	public static long getTimeFromCurrent(int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTimeInMillis();
	}

	public static long getTimeFromTime(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTimeInMillis();
	}

	public static String getCurrentTimeString() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	public static String getCurrentDateString() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static TimePeriod getCurrentTimePeriod() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 7 && hour < 9) {
			return TimePeriod.MORNING;
		} else if (hour >= 9 && hour < 11) {
			return TimePeriod.BEFORENOON;
		} else if (hour >= 11 && hour < 13) {
			return TimePeriod.NOON;
		} else if (hour >= 13 && hour < 17) {
			return TimePeriod.AFTERNOON;
		} else if (hour >= 17 && hour < 19) {
			return TimePeriod.DUSK;
		} else if (hour >= 19 && hour < 21) {
			return TimePeriod.NIGHT;
		} else if (hour >= 21) {
			return TimePeriod.DEEPNIGHT;
		} else if (hour >= 0 && hour < 1) {
			return TimePeriod.MIDNIGHT;
		} else if (hour >= 1 && hour < 7) {
			return TimePeriod.EARLYMORNING;
		}
		return null;
	}

	public static enum TimePeriod {

		/**
		 * 早晨
		 */
		MORNING("MORNING", "早晨"),

		/**
		 * 上午
		 */
		BEFORENOON("BEFORENOON", "上午"),

		/**
		 * 中午
		 */
		NOON("NOON", "中午"),

		/**
		 * 下午
		 */
		AFTERNOON("AFTERNOON", "下午"),

		/**
		 * 傍晚
		 */
		DUSK("DUSK", "傍晚"),

		/**
		 * 晚上
		 */
		NIGHT("NIGHT", "晚上"),

		/**
		 * 深夜
		 */
		DEEPNIGHT("DEEPNIGHT", "深夜"),

		/**
		 * 午夜
		 */
		MIDNIGHT("MIDNIGHT", "午夜"),

		/**
		 * 凌晨
		 */
		EARLYMORNING("EARLYMORNING", "凌晨");

		private TimePeriod(String code, String value) {
			this.code = code;
			this.value = value;
		}

		private String code;
		private String value;

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static TimePeriod fromCode(String code) {
			for (TimePeriod entity : TimePeriod.values()) {
				if (entity.getCode().equals(code)) {
					return entity;
				}
			}
			return null;
		}
	}
}
