package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.MediaUtil;
import weihua.myassistant.ui.util.MediaUtil.MusicPlaySource;
import weihua.myassistant.ui.util.NotificationUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;

public class AlarmService extends Service {

	private static Logger loger = Logger.getLogger(AlarmService.class);

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
		try {
			if (!isRunning) {
				isRunning = true;
				setForegroundService("Hello! I am your assistant.", "Hello! I am your assistant.",
						"What can I do for you?");
			}

			if (extraInfo != null && !"".equals(extraInfo)) {
				if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
					excuteService(this);
				}
			}
		} catch (Exception e) {
			loger.info(ExceptionUtil.getStackTrace(e));
		}
		return 0;
	}

	private void excuteService(final Context context) throws Exception {
		if (serviceQueue.size() > 0) {
			String serviceId = serviceQueue.poll();

			NotificationUtil.showNotification(context, "Tomorow is your day",
					"Current service size：" + serviceQueue.size(), String.valueOf(Constans.ALARM_SERVICE_ID), null,
					Integer.parseInt(serviceId), null);

			mediaPlayer = MediaUtil.playMusic(context, MusicPlaySource.LOCAL, servicesMap.get(serviceId), false,
					new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer arg0) {
							try {
								excuteService(context);
							} catch (Exception e) {
								loger.info(ExceptionUtil.getStackTrace(e));
							}
						}
					});

			NotificationUtil.showNotification(this, servicesMap.get(serviceId), servicesMap.get(serviceId),
					servicesMap.get(serviceId), null, Integer.parseInt(serviceId), null);
		}
	}

	private void setForegroundService(String ticker, String title, String text) throws Exception {
		Notification notification = NotificationUtil.showNotification(this, ticker, title, text, null,
				Constans.ALARM_SERVICE_ID, null);
		startForeground(Constans.ALARM_SERVICE_ID, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}