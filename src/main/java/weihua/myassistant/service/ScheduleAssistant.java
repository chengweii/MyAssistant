package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.response.Response;
import weihua.myassistant.response.TextResponse;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class ScheduleAssistant implements AssistantService {

	@Override
	public Response getResponse(String request, Map<String,Data> serviceData,ServiceConfig serviceConfig) throws Exception {
		Response response = null;
		AlarmData data = getCurrentTask();
		if (data != null) {
			List<AlarmData> dataList = new ArrayList<AlarmData>();
			dataList.add(data);
			response = new TextResponse();
			response.setResponseData(GsonUtil.toJson(dataList));
		}

		return response;
	}

	public static void main(String[] args) throws Exception {
		getTaskListFromDida365("222@qq.com", "222");
	}

	private static AlarmData getCurrentTask() {
		return null;
	}

	private static List<Task> getTaskListFromDida365(String username, String password) throws Exception {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.username = username;
		loginInfo.password = password;
		Call<ResponseBody> loginResult = RetrofitUtil.retrofitService.post(
				"https://dida365.com/api/v2/user/signon?wc=true&remember=true",
				RetrofitUtil.getJsonRequestBody(loginInfo), "");

		retrofit2.Response<ResponseBody> loginResponse = loginResult.execute();
		String loginContent = loginResponse.body().string();
		Map<String, String> map = GsonUtil.getMapFromJson(loginContent);

		Call<ResponseBody> taskResult = RetrofitUtil.retrofitService.get("https://dida365.com/api/v2/project/all/tasks",
				"t=" + map.get("token"));

		retrofit2.Response<ResponseBody> taskResponse = taskResult.execute();
		String taskContent = taskResponse.body().string();
		List<Task> taskList = GsonUtil.<ArrayList<Task>> getEntityFromJson(taskContent,
				new TypeToken<ArrayList<Task>>() {
				});

		for (Task t : taskList) {
			System.out.println(t.title);
		}

		return taskList;
	}

	static class LoginInfo {
		public String username;
		public String password;
	}

	static class Task {
		public String id;
		public String deleted;
		public String startDate;
		public String priority;
		public String title;
		public String content;
		public String status;
	}

}
