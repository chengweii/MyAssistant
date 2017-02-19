package weihua.myassistant.service;

import weihua.myassistant.context.Context;
import weihua.myassistant.data.AlarmData;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.GsonUtil;

/**
 * 日常饮食助手服务
 * 
 * @author chengwei2
 * 
 */
public class DailyDietAssistant implements Assistant {

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		AlarmData data=new AlarmData();
		data.ticker="晚上建议您吃[燕麦牛奶]";
		data.title="晚上建议您吃[燕麦牛奶]";
		data.text="它可以帮您充饥，并起到减肥效果。";
		data.subText="请您一定要记得吃，拜托了。";
		data.musicLink="test1.mp3";
		data.iconLink="http://upload-images.jianshu.io/upload_images/2986704-56424ec781375ab7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
		Response response = new CommonResponse(true);
		response.setResponseData(GsonUtil.toJson(data));
		return response;
	}

}
