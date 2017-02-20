package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;

public class ScheduleAssistant implements Assistant {

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		getTaskListFromDida365("222@qq.com", "222");
	}

	public static List<Task> getTaskListFromDida365(String username, String password) throws Exception {
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

	public static class LoginInfo {
		public String username;
		public String password;
	}

	public static class Task {
		public String id;
		public String deleted;
		public String startDate;
		public String priority;
		public String title;
		public String content;
		public String status;
	}

}
