package weihua.myassistant.context;

import java.util.Date;
import java.util.List;

import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.TopicResponse;
import weihua.myassistant.search.SearchTopic;
import weihua.myassistant.service.Assistant;
import weihua.myassistant.util.AssistantDataLoadUtil;

public class Context {

	private Assistant assistant = null;

	private ResponseHistory responseHistory = new ResponseHistory();

	private static TopicData topicData = null;

	private List<ResponseData> responseDataList = null;

	public Context() {
		if (topicData == null) {
			topicData = AssistantDataLoadUtil.loadAllTopicData();
		}
	}

	public String getResponse(String request, RequestType requestType) {
		String responseContent = null;
		if (responseHistory.lastTopicName == null) {
			if (requestType == RequestType.CHOICE) {
				// response the choice topic to user
				Topic topic = changeTopic(request, requestType);
				// update the topic in context failed,response the topic to user
				if (responseHistory.lastTopicName == null) {
					TopicResponse topicResponse = new TopicResponse(true);
					String content = topicResponse.getContent(topic.children, topic.topicText, topicData);
					return topicResponse.getResponseData(content);
				} else {
					// update the topic in context success,response to user
					responseContent = assistant.getResponse(request, requestType, responseDataList, responseHistory);
					// no response will back home
					if (responseContent == null) {
						responseContent = backHome();
					}
					return responseContent;
				}
			} else {
				// response the search topics to user
				List<Topic> topicList = SearchTopic.searchTopicDataByWord(request, topicData);
				TopicResponse topicResponse = new TopicResponse(true);
				String content = topicResponse.getContent(topicList, request, topicData);
				return topicResponse.getResponseData(content);
			}
		} else {
			responseContent = assistant.getResponse(request, requestType, responseDataList, responseHistory);
			// no response will back home
			if (responseContent == null) {
				responseContent = backHome();
			}
			return responseContent;
		}
	}

	public String backHome() {
		responseHistory = new ResponseHistory();
		return getResponse("", RequestType.TEXT);
	}

	private Topic changeTopic(String request, RequestType requestType) {
		Topic topic = SearchTopic.searchTopicDataById(request, topicData);
		// find the final topic success
		if (topic != null && topic.topicName != null && !"".equals(topic.topicName)) {
			// load topic data by topic name
			responseDataList = AssistantDataLoadUtil.loadTopicData(topic.topicName, responseHistory.lastTopicName);
			// update the current topic in context
			responseHistory.lastTopicName = topic.topicName;
			// instance topic's assistant by topic type
			try {
				assistant = (Assistant) TopicType.fromCode(topic.topicType).getClz().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return topic;
	}

	public static class ResponseHistory {
		public String userName;
		public Date lastResponseTime;
		public String lastTopicName;
		public String lastResponseId;
		public String lastResponseContent;
		public String lastRequestContent;
		public RequestType lastRequestType;
	}
}
