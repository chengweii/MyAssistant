package weihua.myassistant.ui.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {

	public static void startAlarmOnce(Context context, Class<?> cls, String serviceCls, long triggerAtMillis,
			String extraInfo) {
		Intent intent = new Intent(context, cls);
		intent.putExtra("extraInfo", extraInfo);
		intent.putExtra("serviceCls", serviceCls);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
	}

	public static void startAlarmRepeating(Context context, Class<?> cls, String serviceCls, long triggerAtMillis,
			long intervalMillis, String extraInfo) {
		Intent intent = new Intent(context, cls);
		intent.putExtra("extraInfo", extraInfo);
		intent.putExtra("serviceCls", serviceCls);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, sender);
	}

	public static void stopAlarm(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
	}

}
