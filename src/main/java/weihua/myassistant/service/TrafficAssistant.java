package weihua.myassistant.service;

import java.util.Map;

import org.apache.log4j.Logger;

import weihua.myassistant.data.AlarmData;
import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.AmapUtil;
import weihua.myassistant.util.AmapUtil.Param;
import weihua.myassistant.util.AmapUtil.TrafficDirection;
import weihua.myassistant.util.AmapUtil.WalkParam;

public class TrafficAssistant implements AssistantService {

	private static Logger loger = Logger.getLogger(TrafficAssistant.class);


	@Override
	public Response getResponse(String request, Map<String,Data> serviceData,ServiceConfig serviceConfig) throws Exception {
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
