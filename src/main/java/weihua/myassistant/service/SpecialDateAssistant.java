package weihua.myassistant.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.context.Context;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;
import weihua.myassistant.service.DailyDietAssistant.SpecialDate;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class SpecialDateAssistant implements Assistant {

	private static Logger loger = Logger.getLogger(SpecialDateAssistant.class);

	private static final String specialDatePath = FileUtil.getInnerAssistantFileSDCardPath()
			+ "specialdate/specialdate.json";

	private static final String specialDateWebPath = "https://raw.githubusercontent.com/chengweii/myassistant/develop/src/main/source/assistant/specialdate/specialdate.json";

	public static List<SpecialDate> specialDateList = null;

	static {
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

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		Response response = null;
		List<AlarmData> dataList = getCurrentSpecialDate();
		if (dataList != null && dataList.size() > 0) {
			response = new CommonResponse(true);
			response.setResponseData(GsonUtil.toJson(dataList));
		}

		return response;
	}

	private static List<AlarmData> getCurrentSpecialDate() {
		return null;
	}

}
