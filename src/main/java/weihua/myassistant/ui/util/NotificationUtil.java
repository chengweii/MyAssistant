package weihua.myassistant.ui.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import weihua.myassistant.R;

public class NotificationUtil {

	public static Notification showNotification(Context context, String ticker, String title, String text,
			String subText, String contentInfo, String iconUrl, int notificationId, Intent intent) throws Exception {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		if (iconUrl != null && !"".equals(iconUrl)) {
			Bitmap icon = ImageUtil.getBitmap(iconUrl);
			icon = ImageUtil.fillet(icon, 50);
			builder.setLargeIcon(icon);
		}
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setSubText(subText);
		builder.setContentInfo(contentInfo);
		builder.setWhen(System.currentTimeMillis());
		builder.setDefaults(Notification.DEFAULT_ALL);
		Notification notification = builder.build();
		if (intent != null) {
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.contentIntent = pIntent;
		}
		notificationManager.notify(notificationId, notification);
		return notification;
	}

}
