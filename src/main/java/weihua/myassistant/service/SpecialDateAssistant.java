package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.response.Response;
import weihua.myassistant.response.TextResponse;
import weihua.myassistant.util.DateUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class SpecialDateAssistant implements AssistantService {

	private static Logger loger = Logger.getLogger(SpecialDateAssistant.class);

	private static final String specialDatePath = FileUtil.getInnerAssistantFileSDCardPath()
			+ "specialdate/specialdate.json";

	private static final String specialDateWebPath = "https://raw.githubusercontent.com/chengweii/myassistant/develop/src/main/source/assistant/specialdate/specialdate.json";

	public static List<SpecialDate> specialDateList = null;

	private static final String dateTagsPath = FileUtil.getInnerAssistantFileSDCardPath() + "specialdate/datetags.json";

	private static final String dateTagsWebPath = "https://raw.githubusercontent.com/chengweii/myassistant/develop/src/main/source/assistant/specialdate/datetags.json";

	public static Map<String, DateTag> dateTags = new HashMap<String, DateTag>();

	static {
		initSpecialDateList();
		initDateTags();
	}

	@Override
	public Response getResponse(String request, Map<String,Data> serviceData,ServiceConfig serviceConfig) throws Exception {
		Response response = null;
		List<AlarmData> dataList = getCurrentSpecialDate();
		if (dataList != null && dataList.size() > 0) {
			response = new TextResponse();
			response.setResponseData(GsonUtil.toJson(dataList));
		}

		return response;
	}

	private static List<AlarmData> getCurrentSpecialDate() {
		List<AlarmData> dataList = new ArrayList<AlarmData>();
		String currentDate = DateUtil.getCurrentDateString();
		DateTag dateTag = null;
		for (SpecialDate specialDate : SpecialDateAssistant.specialDateList) {
			if (currentDate.equals(specialDate.date)) {
				for (String tag : specialDate.specialTags) {
					dateTag = dateTags.get(tag);
					if (dateTag != null) {
						AlarmData alarmData = new AlarmData();
						alarmData.ticker = dateTag.ticker;
						alarmData.title = dateTag.title;
						alarmData.text = dateTag.text;
						alarmData.subText = dateTag.subText;
						alarmData.iconLink = dateTag.iconLink;
						alarmData.intentAction = dateTag.intentAction;
						alarmData.musicLink = dateTag.musicLink;
						dataList.add(alarmData);
					}
				}
			}
		}

		return dataList;
	}

	private static void initSpecialDateList() {
		try {
			if (FileUtil.isFileExists(specialDatePath)) {
				String json = FileUtil.getFileContent(specialDatePath);
				specialDateList = GsonUtil.getEntityFromJson(json, new TypeToken<List<SpecialDate>>() {
				});
			} else {
				Call<ResponseBody> result = RetrofitUtil.retrofitService.get(specialDateWebPath, "");

				retrofit2.Response<ResponseBody> response = result.execute();
				String json = response.body().string();
				specialDateList = GsonUtil.getEntityFromJson(json, new TypeToken<List<SpecialDate>>() {
				});
				FileUtil.writeFileContent(json, specialDatePath);
			}
		} catch (Exception e) {
			loger.info(ExceptionUtil.getStackTrace(e));
		}
	}

	private static void initDateTags() {
		try {
			if (FileUtil.isFileExists(dateTagsPath)) {
				String json = FileUtil.getFileContent(dateTagsPath);
				dateTags = GsonUtil.getEntityFromJson(json, new TypeToken<Map<String, DateTag>>() {
				});
			} else {
				Call<ResponseBody> result = RetrofitUtil.retrofitService.get(dateTagsWebPath, "");

				retrofit2.Response<ResponseBody> response = result.execute();
				String json = response.body().string();
				dateTags = GsonUtil.getEntityFromJson(json, new TypeToken<Map<String, DateTag>>() {
				});
				FileUtil.writeFileContent(json, dateTagsPath);
			}
		} catch (Exception e) {
			loger.info(ExceptionUtil.getStackTrace(e));
		}
	}

	static class DateTag {
		public String ticker;
		public String title;
		public String text;
		public String subText;
		public String iconLink;
		public String intentAction;
		public String musicLink;
	}

	public static class SpecialDate {
		public String date;
		public boolean isHoliday;
		public List<String> specialTags;
	}

}
