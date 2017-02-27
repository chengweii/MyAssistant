package weihua.myassistant.ui.alarm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import weihua.myassistant.common.Constants;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.data.ServiceType;
import weihua.myassistant.response.Response;
import weihua.myassistant.service.AssistantService;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.Log4JUtil;
import weihua.myassistant.ui.util.NotificationUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.HttpUtil;

public class AlarmService extends Service {

	private static Logger loger = Logger.getLogger(AlarmService.class);

	private static final String serviceConfigPath = FileUtil.getInnerAssistantFileSDCardPath() + "service/service.json";
	private static final String serviceConfigWebPath = Constants.WEB_SOURCE_ROOT_PATH + "service/service.json";
	private static List<ServiceConfig> serviceConfigList;
	private static Map<String, String> servicesMap;

	private static ExecutorService executorService = null;
	private static ConcurrentLinkedQueue<String> serviceQueue = new ConcurrentLinkedQueue<String>();

	private static Map<String, Data> serviceData = new HashMap<String, Data>();

	@Override
	public void onCreate() {
		Log4JUtil.configure();
		initServiceConfigList();
		initScheduledExecutorService();

		super.onCreate();

		setForegroundService("Master,I am at your service.^_^", "Master,I am at your service.^_^",
				"Please keep me here with you.⊙﹏⊙ ");
	}

	private static void initScheduledExecutorService() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(3);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (serviceQueue.size() == 0) {
			offerAllServiceTask();
			initScheduledExecutorService();
			executorService.submit(new ServiceTask(this));
		}
		return 0;
	}

	private static void excuteServiceTask(Context context, String serviceId) {
		try {
			loger.info("The service currently running is:" + serviceId);
			if (servicesMap.containsKey(serviceId)) {
				String serviceClass = servicesMap.get(serviceId);
				AssistantService serviceAssistant = (AssistantService) Class.forName(serviceClass).newInstance();
				Response response = serviceAssistant.getResponse(null, serviceData, getServiceConfig(serviceId));
				if (response != null) {
					String content = response.getResponseData();

					List<AlarmData> dataList = GsonUtil.getEntityFromJson(content, new TypeToken<List<AlarmData>>() {
					});

					for (AlarmData data : dataList) {
						NotificationUtil.showNotification(context, data.ticker, data.title, data.text, data.subText,
								data.contentInfo, data.iconLink, Integer.parseInt(serviceId), null);
					}
				}
			}
		} catch (Exception e) {
			loger.error("Excute ServiceTask failed,serviceId:" + serviceId + ",Exception info:"
					+ ExceptionUtil.getStackTrace(e));
		}
	}

	private static void offerAllServiceTask() {
		for (Map.Entry<String, String> entity : servicesMap.entrySet()) {
			serviceQueue.offer(entity.getKey());
		}
		loger.info("ServiceQueue size:" + serviceQueue.size());
	}

	private static ServiceConfig getServiceConfig(String serviceId) {
		ServiceConfig serviceConfig = null;
		for (ServiceConfig entity : serviceConfigList) {
			if (entity.serviceId.equals(serviceId)) {
				serviceConfig = entity;
				break;
			}
		}
		return serviceConfig;
	}

	private void setForegroundService(String ticker, String title, String text) {
		try {
			Notification notification = NotificationUtil.showNotification(this, ticker, title, text, null, null, null,
					Constans.ALARM_SERVICE_ID, null);
			startForeground(Constans.ALARM_SERVICE_ID, notification);
		} catch (Exception e) {
			loger.info("ForegroundService started failed:" + ExceptionUtil.getStackTrace(e));
		}
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

	static class ServiceTask implements Runnable {
		private Context context;

		public ServiceTask(Context context) {
			this.context = context;
		}

		public void run() {
			while (!serviceQueue.isEmpty()) {
				String serviceId = serviceQueue.poll();
				excuteServiceTask(context, serviceId);
			}
		}
	}

}