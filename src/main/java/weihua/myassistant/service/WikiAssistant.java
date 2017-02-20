package weihua.myassistant.service;

import weihua.myassistant.context.Context;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;

public class WikiAssistant implements Assistant {

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		if (context.responseHistory.lastTopic.topicType.equals(TopicType.WIKI.getCode())
				&& requestType == RequestType.CHOICE && context.responseHistory.lastTopic.topicId.equals(request)) {
			StringBuilder responseContent = new StringBuilder();
			responseContent.append("Please click on this link to obtain the details:");
			responseContent.append("#{type:'url',link:'");
			responseContent.append(context.responseHistory.lastTopic.topicLink);
			responseContent.append("',text:'");
			responseContent.append(context.responseHistory.lastTopic.topicText);
			responseContent.append("'}#");
			CommonResponse commonResponse = new CommonResponse(true);
			commonResponse.setResponseData(responseContent.toString());
			return commonResponse;
		} else {
			return null;
		}
	}

}
