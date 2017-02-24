package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
import weihua.myassistant.common.Constants;
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
import weihua.myassistant.util.HttpUtil;
import weihua.myassistant.util.RetrofitUtil;

public class AlarmService extends Service {

	private static Logger loger = Logger.getLogger(AlarmService.class);

	private static final String serviceConfigPath = FileUtil.getInnerAssistantFileSDCardPath() + "service/service.json";

	private static final String serviceConfigWebPath = Constants.WEB_SOURCE_ROOT_PATH + "service/service.json";

	private static List<ServiceConfig> serviceConfigList;

	private static Map<String, String> servicesMap;

	private MediaPlayer mediaPlayer;

	private boolean isRunning = false;

	private Queue<String> serviceQueue = new LinkedList<String>();

	private Map<String, Data> serviceData = new HashMap<String, Data>();

	@Override
	public void onCreate() {
		initLog();
		initServiceConfigList();
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

			if (servicesMap.containsKey(serviceId)) {
				final String serviceClass = servicesMap.get(serviceId);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							AssistantService serviceAssistant = (AssistantService) Class.forName(serviceClass)
									.newInstance();
							Response response = serviceAssistant.getResponse(null, serviceData,
									getServiceConfig(serviceId));
							if (response != null) {
								String content = response.getResponseData();

								List<AlarmData> dataList = GsonUtil.getEntityFromJson(content,
										new TypeToken<List<AlarmData>>() {
										});

								for (AlarmData data : dataList) {
									NotificationUtil.showNotification(context, data.ticker, data.title, data.text,
											data.subText, data.contentInfo, data.iconLink, Integer.parseInt(serviceId),
											null);
								}

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
							} else {
								excuteService(context);
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
		for (Map.Entry<String, String> entity : servicesMap.entrySet()) {
			serviceQueue.offer(entity.getKey());
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

	private static void initServiceConfigList() {
		try {
			String json = FileUtil.getFileContent(serviceConfigPath);
			serviceConfigList = GsonUtil.getEntityFromJson(json, new TypeToken<List<ServiceConfig>>() {
			});
			initServicesMap();
		} catch (Exception e) {
			loger.info(ExceptionUtil.getStackTrace(e));
		}
	}

	private static void initServicesMap() {
		servicesMap = new LinkedHashMap<String, String>();
		for (ServiceConfig serviceConfig : serviceConfigList) {
			if (serviceConfig.enable) {
				servicesMap.put(serviceConfig.serviceId,
						ServiceType.fromCode(Integer.parseInt(serviceConfig.serviceId)).getClz());
			}
		}
	}

	public static void config() {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String json = HttpUtil.get(serviceConfigWebPath, null, null);
						FileUtil.writeFileContent(json, serviceConfigPath);
					} catch (Exception e) {
						loger.error("AlarmService config failed:" + ExceptionUtil.getStackTrace(e));
					}
				}
			}).start();
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