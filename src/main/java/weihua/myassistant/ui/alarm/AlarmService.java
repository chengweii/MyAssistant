package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import weihua.myassistant.MainActivity;
import weihua.myassistant.R;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.util.FileUtil;

public class AlarmService extends Service {

	private MediaPlayer mediaPlayer;

	private boolean isRunning = false;

	private Queue<String> serviceQueue = new LinkedList<String>();

	private static Map<String, String> servicesMap = new HashMap<String, String>();

	static {
		FileUtil.assistantRootPath = Environment.getExternalStorageDirectory().getPath() + "/"
				+ Constans.ASSISTANT_ROOT_PATH_NAME + "/";
		servicesMap.put(String.valueOf(Constans.HOLIDAY_ALARM_ID), "test1.mp3");
		servicesMap.put(String.valueOf(Constans.WETHER_ALARM_ID), "test2.mp3");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
		serviceQueue.offer(extraInfo);

		if (!isRunning) {
			isRunning = true;
			setForegroundService("Hello! I am assistant.", "Hello! I am assistant.", "What can I do for you?");
		}

		if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
			excuteService(this);
		}

		return 0;
	}

	private void excuteService(Context context) {
		if (serviceQueue.size() > 0) {
			String serviceId = serviceQueue.poll();
			showNotification("Tomorow is your day", "Current service size：" + serviceQueue.size(),
					servicesMap.get(serviceId), Constans.ALARM_SERVICE_ID, null);
			playMusic(context, MusicPlaySource.LOCAL, servicesMap.get(serviceId));

			showNotification(servicesMap.get(serviceId), servicesMap.get(serviceId), servicesMap.get(serviceId),
					Integer.parseInt(serviceId), MainActivity.class);
		}
	}

	private void setForegroundService(String ticker, String title, String text) {
		Notification notification = showNotification(ticker, title, text, Constans.ALARM_SERVICE_ID, null);
		startForeground(Constans.ALARM_SERVICE_ID, notification);
	}

	private Notification showNotification(String ticker, String title, String text, int notificationId, Class<?> cls) {
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
		if (cls != null) {
			Intent intent = new Intent(this, cls);
			intent.putExtra("serviceName", text);
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.contentIntent = pIntent;
		}
		notificationManager.notify(notificationId, notification);
		return notification;
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private MediaPlayer playMusic(final Context context, MusicPlaySource musicPlaySource, String mediaLink) {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			// mediaPlayer.stop();
			return mediaPlayer;
		}

		if (musicPlaySource == MusicPlaySource.LOCAL) {
			mediaLink = FileUtil.getInnerAssistantFileSDCardPath() + mediaLink;
		}

		mediaPlayer = MediaPlayer.create(context, Uri.parse(mediaLink));

		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				excuteService(context);
			}
		});

		mediaPlayer.start();

		return mediaPlayer;
	}

	private static enum MusicPlaySource {

		LOCAL("0", "本地"),

		WEB("1", "网络");

		private MusicPlaySource(String code, String value) {
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

		public static MusicPlaySource fromCode(String code) {
			for (MusicPlaySource entity : MusicPlaySource.values()) {
				if (entity.getCode().equals(code)) {
					return entity;
				}
			}
			return null;
		}
	}
}