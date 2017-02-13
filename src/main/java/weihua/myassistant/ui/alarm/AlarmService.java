package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.Map;

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
import weihua.myassistant.ui.util.AlarmUtil;
import weihua.myassistant.util.DateUtil;

public class AlarmService extends Service {

	protected MediaPlayer mediaPlayer;

	private static Map<String, String> servicesMap = new HashMap<String, String>();

	static {
		servicesMap.put(String.valueOf(Constans.HOLIDAY_ALARM_ID),
				"http://172.16.0.199/IXC7321d40d247713b278c1bc035a77324c/hot/2010/08-26/370453.mp3");
		servicesMap.put(String.valueOf(Constans.WETHER_ALARM_ID), "http://42.81.26.18/mp3.9ku.com/m4a/637791.m4a");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
		setForegroundService("明天是情人节，请记得买礼物和问候。", "明天是情人节，请记得买礼物和问候。", servicesMap.get(extraInfo));
		mediaPlayer = MediaIntent.playMusic(this, MusicPlaySource.WEB, servicesMap.get(extraInfo));
		return 0;
	}

	protected void setForegroundService(String ticker, String title, String text) {
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
		notificationManager.notify(Constans.ALARM_SERVICE_ID, notification);

		startForeground(Constans.ALARM_SERVICE_ID, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}