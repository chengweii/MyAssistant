package weihua.myassistant.ui.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;
import weihua.myassistant.R;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;

public class MusicPlayerService extends Service {
	private static final String TAG = MusicPlayerService.class.getSimpleName();

	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
		builder.setTicker("sdfsdf");
		builder.setContentTitle("sdfsd");
		builder.setContentText("sdfsdf");
		builder.setWhen(System.currentTimeMillis());
		builder.setDefaults(Notification.DEFAULT_ALL);
		Notification notification = builder.build();
		notificationManager.notify(456, notification);
		startForeground(110, notification);// 开始前台服务
		Toast toast = Toast.makeText(this, "MusicPlayerService", Toast.LENGTH_SHORT);
		toast.show();
		mediaPlayer = MediaIntent.playMusic(this, MusicPlaySource.WEB, "http://42.81.26.18/mp3.9ku.com/m4a/637791.m4a");
		return 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}