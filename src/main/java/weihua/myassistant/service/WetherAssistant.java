package weihua.myassistant.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import weihua.myassistant.context.Context;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.GsonUtil;

public class WetherAssistant implements Assistant {

	private static Logger loger = Logger.getLogger(DailyDietAssistant.class);

	private static final String wetherInfoUrl = "https://tianqi.moji.com/weather/china/beijing/tongzhou-district";

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		Response response = null;
		AlarmData data = getCurrentWether();
		if (data != null) {
			List<AlarmData> dataList = new ArrayList<AlarmData>();
			dataList.add(data);
			response = new CommonResponse(true);
			response.setResponseData(GsonUtil.toJson(dataList));
		}

		return response;
	}

	public static void main(String[] args) throws Exception {
		WetherInfo wetherInfo = getWetherInfoFromMoji();
		loger.info("WetherInfo:" + wetherInfo);
	}

	private static AlarmData getCurrentWether() {
		AlarmData alarmData = null;
		return alarmData;
	}

	private static WetherInfo getWetherInfoFromMoji() throws IOException {
		Document doc = Jsoup.connect(wetherInfoUrl).get();
		Element wetherMeta = doc.getElementsByTag("meta").get(2);
		String wetherMetaContent = wetherMeta.attr("content");
		wetherMetaContent = wetherMetaContent.replaceAll("今天实况", "").replaceAll("墨迹天气", "");

		WetherInfo wetherInfo = new WetherInfo();
		int lastIndex = wetherMetaContent.indexOf("：");
		wetherInfo.areaName = wetherMetaContent.substring(0, lastIndex);

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf(" ");
		wetherInfo.realTimeTemperature = wetherMetaContent.substring(0, lastIndex);

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("，");
		wetherInfo.realTimeWeather = wetherMetaContent.substring(0, lastIndex);

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("，");
		wetherInfo.humidity = wetherMetaContent.substring(0, lastIndex);
		wetherInfo.humidity = wetherInfo.humidity.replace("湿度：", "").replace("%", "");

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("：");
		wetherInfo.windDirection = wetherMetaContent.substring(0, lastIndex);

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("。");
		wetherInfo.windLevel = wetherMetaContent.substring(0, lastIndex);
		wetherInfo.windLevel = wetherInfo.windLevel.replace("级", "");

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf(",");
		wetherInfo.daytimeTemperature = wetherMetaContent.substring(0, lastIndex);
		wetherInfo.daytimeTemperature = wetherInfo.daytimeTemperature.replace("白天：", "").replace("度", "");

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("。");
		wetherInfo.daytimeWeather = wetherMetaContent.substring(0, lastIndex);

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("，");
		wetherInfo.nightWeather = wetherMetaContent.substring(0, lastIndex);
		wetherInfo.nightWeather = wetherInfo.nightWeather.replace("夜间：", "");

		wetherMetaContent = wetherMetaContent.substring(lastIndex + 1);
		lastIndex = wetherMetaContent.indexOf("，");
		wetherInfo.nightTemperature = wetherMetaContent.substring(0, lastIndex);
		wetherInfo.nightTemperature = wetherInfo.nightTemperature.replace("度", "");

		wetherInfo.tips = wetherMetaContent.substring(lastIndex + 1);

		Element airMeta = doc.select(".wea_alert>ul>li>a>em").get(0);
		String[] airInfo = airMeta.ownText().split(" ");
		wetherInfo.airPoint = airInfo[0];
		wetherInfo.airLevel = airInfo[1];

		Elements limitingNumMeta = doc.select(".wea_about>b");
		if (limitingNumMeta.size() > 0) {
			wetherInfo.limitingNum = limitingNumMeta.get(0).ownText();
		}

		return wetherInfo;
	}

	static class WetherInfo {
		public String areaName;
		public String realTimeTemperature;
		public String realTimeWeather;
		public String daytimeTemperature;
		public String daytimeWeather;
		public String nightTemperature;
		public String nightWeather;
		public String humidity;
		public String windDirection;
		public String windLevel;
		public String limitingNum;
		public String airPoint;
		public String airLevel;
		public String tips;

		@Override
		public String toString() {
			return "[areaName:" + areaName + ",realTimeTemperature:" + realTimeTemperature + ",realTimeWeather:"
					+ realTimeWeather + ",daytimeTemperature:" + daytimeTemperature + ",daytimeWeather:"
					+ daytimeWeather + ",nightTemperature:" + nightTemperature + ",nightWeather:" + nightWeather
					+ ",humidity:" + humidity + ",windDirection:" + windDirection + ",windLevel:" + windLevel
					+ ",limitingNum:" + limitingNum + ",airPoint:" + airPoint + ",airLevel:" + airLevel + ",tips:"
					+ tips + "]";
		}
	}

}
