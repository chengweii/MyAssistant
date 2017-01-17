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

	static {
		topicData = AssistantDataLoadUtil.loadAllTopicData();
	}

	public String getResponse(String request, RequestType requestType) {
		if (responseHistory.lastTopicName == null) {
			if (requestType == RequestType.CHOICE) {
				// response the choice topic to user
				Topic topic = changeTopic(request, requestType);
				// update the topic in context failed,response the topic to user
				if (responseHistory.lastTopicName == null) {
					TopicResponse response = new TopicResponse();
					String content = response.getContent(topic.children, topic.topicName);
					return response.getResponseData(content);
				} else {
					// update the topic in context success,response to user
					return assistant.getResponse(request, requestType, responseDataList, responseHistory);
				}
			} else {
				// response the search topics to user
				List<Topic> topicList = SearchTopic.searchTopicDataByWord(request, topicData);
				TopicResponse response = new TopicResponse();
				String content = response.getContent(topicList, request);
				return response.getResponseData(content);
			}
		} else {
			return assistant.getResponse(request, requestType, responseDataList, responseHistory);
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
				assistant = (Assistant) TopicType.valueOf(topic.topicType).getClz().newInstance();
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
