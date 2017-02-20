package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.response.Response;
import weihua.myassistant.service.Assistant;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.Log4JUtil;
import weihua.myassistant.ui.util.MediaUtil;
import weihua.myassistant.ui.util.MediaUtil.MusicPlaySource;
import weihua.myassistant.ui.util.NotificationUtil;
import weihua.myassistant.util.DateUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;

public class AlarmService extends Service {

	private static Logger loger = Logger.getLogger(AlarmService.class);

	private MediaPlayer mediaPlayer;

	private boolean isRunning = false;

	private Queue<String> serviceQueue = new LinkedList<String>();

	private static Map<String, Class<?>> servicesMap = new HashMap<String, Class<?>>();

	static {
		FileUtil.assistantRootPath = Environment.getExternalStorageDirectory().getPath() + "/"
				+ Constans.ASSISTANT_ROOT_PATH_NAME + "/";
		Log4JUtil.configure();
		servicesMap.put(String.valueOf(Constans.DAILYDIET_ALARM_ID), TopicType.DAILYDIET.getClz());
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
		serviceQueue.offer(extraInfo);
		loger.info("Current serviceId:" + intent.getExtras());
		try {
			if (!isRunning) {
				isRunning = true;
				setForegroundService("Master,I am at your service.^_^", "Master,I am at your service.^_^",
						"Please keep me here with you.⊙﹏⊙ ");
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
			final String serviceId = serviceQueue.poll();

			final Class<?> serviceClass = servicesMap.get(serviceId);
			if (serviceClass != null) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Assistant serviceAssistant = (Assistant) serviceClass.newInstance();
							Response response = serviceAssistant.getResponse(null, null, null);
							if (response != null) {
								String content = response.getResponseData();

								AlarmData data = GsonUtil.getEntityFromJson(content, new TypeToken<AlarmData>() {
								});

								mediaPlayer = MediaUtil.playMusic(context, MusicPlaySource.LOCAL, data.musicLink, false,
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

								NotificationUtil.showNotification(context, data.ticker, data.title, data.text,
										data.subText, DateUtil.getCurrentTimeString(), data.iconLink,
										Integer.parseInt(serviceId), null);
							}
						} catch (Exception e) {
							loger.error("ExcuteService ShowNotification failed:" + ExceptionUtil.getStackTrace(e));
						}
					}
				}).start();

			}

		}
	}

	private void setForegroundService(String ticker, String title, String text) throws Exception {
		Notification notification = NotificationUtil.showNotification(this, ticker, title, text, null, null, null,
				Constans.ALARM_SERVICE_ID, null);
		startForeground(Constans.ALARM_SERVICE_ID, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}