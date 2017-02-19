package weihua.myassistant.ui.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import weihua.myassistant.ui.alarm.AlarmReceiver;
import weihua.myassistant.ui.common.Constans;

public class AlarmUtil {

	public static void startAlarmOnce(Context context, int alarmReceiverId, long triggerAtMillis, String extraInfo,
			boolean isAgain) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(Constans.ALARM_EXTRA_INFO, extraInfo);
		PendingIntent sender = PendingIntent.getBroadcast(context, alarmReceiverId, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (isAgain) {
			manager.cancel(sender);
			manager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
		} else {
			manager.setWindow(AlarmManager.RTC_WAKEUP, triggerAtMillis, 0, sender);
		}
	}

	public static void startAlarmRepeating(Context context, int alarmReceiverId, long triggerAtMillis,
			long intervalMillis, String action, String extraInfo) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(action);
		intent.putExtra(Constans.ALARM_EXTRA_INFO, extraInfo);
		PendingIntent sender = PendingIntent.getBroadcast(context, alarmReceiverId, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, sender);
	}

	public static void stopAlarm(Context context, int alarmReceiverId) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmReceiverId, intent, 0);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
	}

}
