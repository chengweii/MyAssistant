package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.data.ServiceType;
import weihua.myassistant.response.Response;
import weihua.myassistant.service.AssistantService;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.Log4JUtil;
import weihua.myassistant.ui.util.MediaUtil;
import weihua.myassistant.ui.util.MediaUtil.MusicPlaySource;
import weihua.myassistant.ui.util.NotificationUtil;
import weihua.myassistant.util.DateUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class AlarmService extends Service {

	private static Logger loger = Logger.getLogger(AlarmService.class);

	private static final String serviceConfigPath = FileUtil.getInnerAssistantFileSDCardPath() + "service/service.json";

	private static final String serviceConfigWebPath = "https://raw.githubusercontent.com/chengweii/myassistant/develop/src/main/source/assistant/service/service.json";

	private static List<ServiceConfig> serviceConfigList;

	private static Map<String, Class<?>> servicesMap;

	private MediaPlayer mediaPlayer;

	private boolean isRunning = false;

	private Queue<String> serviceQueue = new LinkedList<String>();

	private Map<String, Data> serviceData = new HashMap<String, Data>();

	static {
		initLog();
		initServiceConfigList();
		initServicesMap();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		offerAllService();
		try {
			if (!isRunning) {
				isRunning = true;
				setForegroundService("Master,I am at your service.^_^", "Master,I am at your service.^_^",
						"Please keep me here with you.⊙﹏⊙ ");
			}

			if (serviceQueue.size() > 0) {
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

			loger.info("The service currently running is:" + serviceId);

			final Class<?> serviceClass = servicesMap.get(serviceId);
			if (serviceClass != null) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							AssistantService serviceAssistant = (AssistantService) serviceClass.newInstance();
							Response response = serviceAssistant.getResponse(null, serviceData,
									getServiceConfig(serviceId));
							if (response != null) {
								String content = response.getResponseData();

								List<AlarmData> dataList = GsonUtil.getEntityFromJson(content,
										new TypeToken<List<AlarmData>>() {
										});

								mediaPlayer = MediaUtil.playMusic(context, MusicPlaySource.LOCAL,
										dataList.get(0).musicLink, false, new MediaPlayer.OnCompletionListener() {
											@Override
											public void onCompletion(MediaPlayer arg0) {
												try {
													excuteService(context);
												} catch (Exception e) {
													loger.info(ExceptionUtil.getStackTrace(e));
												}
											}
										});

								for (AlarmData data : dataList) {
									NotificationUtil.showNotification(context, data.ticker, data.title, data.text,
											data.subText, DateUtil.getCurrentTimeString(), data.iconLink,
											Integer.parseInt(serviceId), null);
								}

							}
						} catch (Exception e) {
							loger.error("ExcuteService ShowNotification failed:" + ExceptionUtil.getStackTrace(e));
						}
					}
				}).start();

			}

		}
	}

	private void offerAllService() {
		for (ServiceType serviceType : ServiceType.values()) {
			serviceQueue.offer(String.valueOf(serviceType.getCode()));
		}
	}

	private ServiceConfig getServiceConfig(String serviceId) {
		ServiceConfig serviceConfig = null;
		for (ServiceConfig entity : serviceConfigList) {
			if (entity.serviceId.equals(serviceId)) {
				serviceConfig = entity;
				break;
			}
		}
		return serviceConfig;
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

	private static void initServicesMap() {
		servicesMap = new HashMap<String, Class<?>>();
		for (ServiceType serviceType : ServiceType.values()) {
			servicesMap.put(String.valueOf(serviceType.getCode()), serviceType.getClz());
		}
	}

	private static void initServiceConfigList() {
		try {
			if (FileUtil.isFileExists(serviceConfigPath)) {
				String json = FileUtil.getFileContent(serviceConfigPath);
				serviceConfigList = GsonUtil.getEntityFromJson(json, new TypeToken<List<ServiceConfig>>() {
				});
			} else {
				Call<ResponseBody> result = RetrofitUtil.retrofitService.get(serviceConfigWebPath, "");

				retrofit2.Response<ResponseBody> response = result.execute();
				String json = response.body().string();
				serviceConfigList = GsonUtil.getEntityFromJson(json, new TypeToken<List<ServiceConfig>>() {
				});
				FileUtil.writeFileContent(json, serviceConfigPath);
			}
		} catch (Exception e) {
			loger.info(ExceptionUtil.getStackTrace(e));
		}
	}

	private static void initLog() {
		FileUtil.assistantRootPath = Environment.getExternalStorageDirectory().getPath() + "/"
				+ Constans.ASSISTANT_ROOT_PATH_NAME + "/";
		Log4JUtil.configure();
	}

}