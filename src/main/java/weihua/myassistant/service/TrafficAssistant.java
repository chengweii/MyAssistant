package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.common.Constants;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.data.TrafficData;
import weihua.myassistant.response.Response;
import weihua.myassistant.response.TextResponse;
import weihua.myassistant.util.AmapUtil;
import weihua.myassistant.util.AmapUtil.IntegratedParam;
import weihua.myassistant.util.AmapUtil.TrafficTransits;
import weihua.myassistant.util.AmapUtil.TrafficTransits.Route.Transit;
import weihua.myassistant.util.AmapUtil.TrafficTransits.Route.Transit.Segment;
import weihua.myassistant.util.AmapUtil.TrafficTransits.Route.Transit.Segment.Bus.Busline;
import weihua.myassistant.util.DateUtil;
import weihua.myassistant.util.DateUtil.TimePeriod;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class TrafficAssistant implements AssistantService {

	private static Logger loger = Logger.getLogger(TrafficAssistant.class);

	private static Location location = null;

	private static final String trafficPath = FileUtil.getInnerAssistantFileSDCardPath() + "traffic/traffic.json";
	private static final String trafficWebPath = Constants.WEB_SOURCE_ROOT_PATH + "traffic/traffic.json";

	static {
		initLocation();
	}

	@Override
	public Response getResponse(String request, Map<String, Data> serviceData, ServiceConfig serviceConfig)
			throws Exception {
		Response response = null;
		AlarmData data = getCurrentTraffic(serviceData, serviceConfig);
		if (data != null) {
			List<AlarmData> dataList = new ArrayList<AlarmData>();
			dataList.add(data);
			response = new TextResponse();
			response.setResponseData(GsonUtil.toJson(dataList));
		}
		return response;
	}

	public static void main(String[] args) throws Exception {
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.enable = true;
		serviceConfig.serviceId = "205";
		serviceConfig.excuteTimePeriod = "MORNING,AFTERNOON,DUSK,NIGHT";
		Map<String, Data> serviceData = new HashMap<String, Data>();
		AlarmData alarmData = getCurrentTraffic(serviceData, serviceConfig);
		loger.info(alarmData);
	}

	private static AlarmData getCurrentTraffic(Map<String, Data> serviceData, ServiceConfig serviceConfig)
			throws Exception {
		AlarmData alarmData = null;

		TimePeriod timePeriod = DateUtil.getCurrentTimePeriod();
		loger.info("Current timePeriod is:" + timePeriod.getValue());
		if (ServiceConfig.matchTimePeriod(serviceConfig.excuteTimePeriod, timePeriod.getCode())
				&& checkIsTiped(timePeriod, serviceData, serviceConfig)) {
			loger.info("Finding reasonable routes...");
			IntegratedParam param = new IntegratedParam();
			if (timePeriod == TimePeriod.MORNING) {
				param.origin = location.familyLocation;
				param.destination = location.companyLocation;
			} else {
				param.origin = location.companyLocation;
				param.destination = location.familyLocation;
			}
			param.city = location.cityName;
			param.date = DateUtil.getCurrentDateString();
			param.time = DateUtil.getCurrentTimeString();
			TrafficTransits trafficTransits = AmapUtil.<TrafficTransits, IntegratedParam> getTrafficInfo(param);

			String fastestLine = "❤ ";
			StringBuilder otherLineInfo = new StringBuilder();

			int i = 0;
			for (Transit transit : trafficTransits.route.transits) {
				if (i == 0) {
					fastestLine += getSimpleLineInfo(transit);
				} else {
					otherLineInfo.append(getSimpleLineInfo(transit));
				}
				i++;
			}

			alarmData = new AlarmData();
			alarmData.ticker = fastestLine;
			alarmData.title = timePeriod == TimePeriod.MORNING ? "上班乘车提醒" : "下班乘车提醒";
			alarmData.text = fastestLine;
			alarmData.subText = "☞ 点击查看其他乘车路线详细信息...";
			alarmData.iconLink = "traffic/images/bus.png";
			alarmData.intentAction = Constants.PACKAGE_NAME;
			alarmData.extraInfo = GsonUtil.toJson(trafficTransits);
			alarmData.musicLink = "test1.mp3";
		}

		return alarmData;
	}

	private static boolean checkIsTiped(TimePeriod timePeriod, Map<String, Data> serviceData,
			ServiceConfig serviceConfig) {
		boolean isTiped = false;
		if (serviceData != null) {
			if (!serviceData.containsKey(serviceConfig.serviceId)) {
				TrafficData data = new TrafficData();
				data.lastExcuteTimePeriod = timePeriod.getCode();
				serviceData.put(serviceConfig.serviceId, data);
			} else {
				TrafficData data = (TrafficData) serviceData.get(serviceConfig.serviceId);
				if (timePeriod.getCode().equals(data.lastExcuteTimePeriod)) {
					isTiped = true;
					loger.info(timePeriod.getValue()+"的提醒已经提示过了。");
				} else {
					data.lastExcuteTimePeriod = timePeriod.getCode();
				}
			}
		}
		return isTiped;
	}

	private static String getSimpleLineInfo(Transit transit) {
		StringBuilder lineInfo = new StringBuilder();
		int l = 0;
		for (Segment segment : transit.segments) {
			int i = 0;
			for (Busline busline : segment.bus.buslines) {
				lineInfo.append(busline.name.replaceAll("\\(.*?\\)", ""));
				if (i != segment.bus.buslines.size() - 1) {
					lineInfo.append("/");
				}
				i++;
			}
			if (l + 1 <= transit.segments.size() - 1) {
				Segment next = transit.segments.get(l + 1);
				if (l != transit.segments.size() - 1 && next.bus.buslines.size() > 0) {
					lineInfo.append(" >> ");
				}
			}

			l++;
		}
		lineInfo.append("≈");
		lineInfo.append((int) Integer.parseInt(transit.duration) / 60);
		lineInfo.append("分。");
		return lineInfo.toString();
	}

	private static String getLineInfo(Transit transit) {
		StringBuilder lineInfo = new StringBuilder();
		int l = 0;
		for (Segment segment : transit.segments) {
			int i = 0;
			for (Busline busline : segment.bus.buslines) {
				lineInfo.append(busline.name);
				lineInfo.append("[");
				lineInfo.append(busline.departure_stop.name);
				lineInfo.append("->");
				lineInfo.append(busline.arrival_stop.name);
				lineInfo.append("]");
				if (i != segment.bus.buslines.size() - 1) {
					lineInfo.append("/");
				}
				i++;
			}
			if (l + 1 <= transit.segments.size() - 1) {
				Segment next = transit.segments.get(l + 1);
				if (l != transit.segments.size() - 1 && next.bus.buslines.size() > 0) {
					lineInfo.append(" >> ");
				}
			}

			l++;
		}
		lineInfo.append(" ≈");
		lineInfo.append((int) Integer.parseInt(transit.duration) / 60);
		lineInfo.append("分。");
		return lineInfo.toString();
	}

	private static void initLocation() {
		try {
			if (FileUtil.isFileExists(trafficPath)) {
				String json = FileUtil.getFileContent(trafficPath);
				location = GsonUtil.getEntityFromJson(json, new TypeToken<Location>() {
				});
			} else {
				Call<ResponseBody> result = RetrofitUtil.retrofitService.get(trafficWebPath, "");

				retrofit2.Response<ResponseBody> response = result.execute();
				String json = response.body().string();
				location = GsonUtil.getEntityFromJson(json, new TypeToken<Location>() {
				});
				FileUtil.writeFileContent(json, trafficPath);
			}
		} catch (Exception e) {
			loger.info("Init location failed:" + ExceptionUtil.getStackTrace(e));
		}
	}

	public static class Location {
		public String cityName;
		public String familyLocation;
		public String companyLocation;
	}

}
