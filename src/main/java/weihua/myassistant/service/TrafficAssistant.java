package weihua.myassistant.service;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import weihua.myassistant.context.Context;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.AmapUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.RetrofitUtil;
import weihua.myassistant.util.AmapUtil.Param;
import weihua.myassistant.util.AmapUtil.TrafficDirection;
import weihua.myassistant.util.AmapUtil.WalkParam;

public class TrafficAssistant implements Assistant {

	private static Logger loger = Logger.getLogger(TrafficAssistant.class);


	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		AlarmData alarmData = getCurrentTraffic();
		loger.info(alarmData);
	}

	private static AlarmData getCurrentTraffic() throws Exception {
		AlarmData alarmData = null;
		WalkParam param = new WalkParam();
		param.origin = "116.546519,39.76229";
		param.destination = "116.572416,39.768533";
		TrafficDirection trafficDirection = AmapUtil.<TrafficDirection, Param> getTrafficInfo(param);

		alarmData = new AlarmData();
		alarmData.ticker = trafficDirection.route.paths.get(0).duration;
		return alarmData;
	}





}
