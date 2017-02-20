package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.context.Context;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;
import weihua.myassistant.service.DailyDietAssistant.DietData.MealData;
import weihua.myassistant.util.DateUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class DailyDietAssistant implements Assistant {

	private static Logger loger = Logger.getLogger(DailyDietAssistant.class);

	private static final String dietPath = FileUtil.getInnerAssistantFileSDCardPath() + "dailydiet/dailydiet.json";

	private static final String specialDatePath = FileUtil.getInnerAssistantFileSDCardPath()
			+ "specialdate/specialdate.json";

	private static List<SpecialDate> specialDateList = null;

	static {
		try {
			if (FileUtil.isFileExists(specialDatePath)) {
				String json = FileUtil.getFileContent(specialDatePath);
				specialDateList = GsonUtil.getEntityFromJson(json, new TypeToken<List<SpecialDate>>() {
				});
			} else {
				Call<ResponseBody> result = RetrofitUtil.retrofitService.get(
						"https://raw.githubusercontent.com/chengweii/myassistant/develop/src/main/source/assistant/specialdate/specialdate.json",
						"");

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

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		AlarmData data = getCurrentDiet();
		Response response = new CommonResponse(true);
		response.setResponseData(GsonUtil.toJson(data));
		return response;
	}

	public static void main(String[] args) throws Exception {
		AlarmData alarmData = getCurrentDiet();
		loger.info(alarmData);
	}

	private static AlarmData getCurrentDiet() throws Exception {
		AlarmData data = new AlarmData();

		List<DietData> dietDataList = null;
		if (FileUtil.isFileExists(dietPath)) {
			String json = FileUtil.getFileContent(dietPath);
			dietDataList = GsonUtil.getEntityFromJson(json, new TypeToken<List<DietData>>() {
			});
		} else {
			dietDataList = getDietDataFromJianshu();
		}

		MealData currentMealData = null;
		MealType mealType = timeMatching();
		for (DietData dietData : dietDataList) {
			if (dateMatching(dietData.dateType)) {
				if (mealType == MealType.BREAKFAST) {
					currentMealData = dietData.breakfast.get(0);
					data.musicLink = MealType.BREAKFAST.getValue();
				} else if (mealType == MealType.LUNCH) {
					currentMealData = dietData.lunch.get(0);
					data.musicLink = MealType.LUNCH.getValue();
				} else if (mealType == MealType.DINNER) {
					currentMealData = dietData.dinner.get(0);
					data.musicLink = MealType.DINNER.getValue();
				}
				if (currentMealData != null) {
					data.ticker = currentMealData.mealName;
					data.title = currentMealData.mealName;
					data.text = currentMealData.features;
					data.subText = currentMealData.tips;
					data.iconLink = currentMealData.imageLink;
					data.intentAction = currentMealData.tutorialLink;
				}
			}
		}
		return data;
	}

	private static boolean dateMatching(String dateType) throws Exception {

		boolean isWorkDate = true;

		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			isWorkDate = false;
		}

		String currentDate = DateUtil.getCurrentDateString();
		for (SpecialDate specialDate : specialDateList) {
			if (currentDate.equals(specialDate.date) && !specialDate.isHoliday) {
				isWorkDate = true;
				break;
			}
		}

		boolean result = false;

		if (isWorkDate) {
			if (DateType.fromCode(dateType) == DateType.WORKDATE) {
				result = true;
			}
		} else {
			if (DateType.fromCode(dateType) == DateType.RESTDATE) {
				result = true;
			}
		}

		return result;
	}

	private static MealType timeMatching() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 7 && hour < 8) {
			return MealType.BREAKFAST;
		} else if (hour >= 11 && hour < 12) {
			return MealType.LUNCH;
		} else if (hour >= 17 && hour < 21) {
			return MealType.DINNER;
		}
		return null;
	}

	private static List<DietData> getDietDataFromJianshu() throws Exception {
		List<DietData> dietDataList = new ArrayList<DietData>();

		Document doc = Jsoup.connect("http://www.jianshu.com/p/7fb9dfe42c53").get();
		Elements showContent = doc.getElementsByAttributeValue("class", "show-content");
		Element rootUl = showContent.get(0).getElementsByTag("ul").get(0);
		Elements liList = rootUl.children();

		for (Element li : liList) {
			DietData dietData = new DietData();
			dietData.dateType = li.ownText();
			Element rootli = li.getElementsByTag("ul").get(0);
			Elements dateTypeLiList = rootli.children();

			for (Element dateType : dateTypeLiList) {
				if (MealType.fromCode(dateType.ownText().trim()) == MealType.BREAKFAST) {
					dietData.breakfast = new ArrayList<MealData>();
					fillMealDataList(dietData.breakfast, dateType);
				} else if (MealType.fromCode(dateType.ownText().trim()) == MealType.LUNCH) {
					dietData.lunch = new ArrayList<MealData>();
					fillMealDataList(dietData.lunch, dateType);
				} else if (MealType.fromCode(dateType.ownText().trim()) == MealType.DINNER) {
					dietData.dinner = new ArrayList<MealData>();
					fillMealDataList(dietData.dinner, dateType);
				}
			}
			dietDataList.add(dietData);
		}

		String jsonString = GsonUtil.gson.toJson(dietDataList);
		FileUtil.writeFileContent(jsonString, dietPath);

		return dietDataList;
	}

	private static void fillMealDataList(List<MealData> mealDataList, Element rootLi) {
		Element rootUl = rootLi.getElementsByTag("ul").get(0);
		Elements liList = rootUl.children();
		String[] mealDataArray;
		for (Element li : liList) {
			MealData mealData = new MealData();
			mealDataArray = getMealData(li.getElementsByAttributeValue("class", "image-caption").get(0).ownText());
			mealData.mealName = mealDataArray[0];
			mealData.features = mealDataArray[1];
			mealData.tips = mealDataArray[2];
			mealData.tutorialLink = mealDataArray[3];
			mealData.imageLink = li.getElementsByTag("img").get(0).attr("src");
			mealDataList.add(mealData);
		}
	}

	private static String[] getMealData(String text) {
		String[] result = new String[4];
		String[] topicData = text.split("##");
		int index = 0;
		for (String s : topicData) {
			result[index] = s;
			index++;
		}
		return result;
	}

	static class SpecialDate {
		public String date;
		public boolean isHoliday;
		public List<String> specialTags;
	}

	static class DietData {
		public String dateType;
		public List<MealData> breakfast;
		public List<MealData> lunch;
		public List<MealData> dinner;

		static class MealData {
			public String mealName;
			public String imageLink;
			public String features;
			public String tips;
			public String tutorialLink;
		}
	}

	static enum DateType {
		/**
		 * 工作日
		 */
		WORKDATE("工作日", "工作日"),

		/**
		 * 休息日
		 */
		RESTDATE("休息日", "休息日");

		private DateType(String code, String value) {
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

		public static DateType fromCode(String code) {
			for (DateType entity : DateType.values()) {
				if (entity.getCode().equals(code)) {
					return entity;
				}
			}
			return null;
		}
	}

	static enum MealType {

		/**
		 * 早餐
		 */
		BREAKFAST("早餐", "test1.mp3"),

		/**
		 * 午餐
		 */
		LUNCH("午餐", "test2.mp3"),

		/**
		 * 晚餐
		 */
		DINNER("晚餐", "test3.mp3");

		private MealType(String code, String value) {
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

		public static MealType fromCode(String code) {
			for (MealType entity : MealType.values()) {
				if (entity.getCode().equals(code)) {
					return entity;
				}
			}
			return null;
		}
	}

}
