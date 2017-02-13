package weihua.myassistant.ui.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import weihua.myassistant.R;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;
import weihua.myassistant.ui.common.Constans;

public class AlarmService extends Service {

	protected MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String extraInfo = intent.getStringExtra("extraInfo");
		setForegroundService("明天是情人节，请记得买礼物和问候。", "明天是情人节，请记得买礼物和问候。", extraInfo, Constans.HOLIDAY_ALARM_SERVICE_ID,
				Constans.HOLIDAY_ALARM_SERVICE_ID);
		mediaPlayer = MediaIntent.playMusic(this, MusicPlaySource.WEB, extraInfo);
		return 0;
	}

	protected void setForegroundService(String ticker, String title, String text, int notificationId,
			int foregroundId) {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setWhen(System.currentTimeMillis());
		builder.setDefaults(Notification.DEFAULT_ALL);
		Notification notification = builder.build();
		notificationManager.notify(notificationId, notification);

		startForeground(foregroundId, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}