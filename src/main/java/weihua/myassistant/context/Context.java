package weihua.myassistant.context;

import java.util.Date;
import java.util.List;

import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.Response;
import weihua.myassistant.response.TopicResponse;
import weihua.myassistant.search.SearchTopic;
import weihua.myassistant.service.Assistant;

public class Context {

	private Assistant assistant = null;

	public ResponseHistory responseHistory = new ResponseHistory();

	public static TopicData topicData = null;

	public Context() throws Exception {
		if (topicData == null) {
			topicData = TopicDataLoadUtil.loadAllTopicDataFromLocal();
		}
	}

	public String getResponse(String request, RequestType requestType) throws Exception {
		if (responseHistory.lastTopic == null) {
			// no topic,choice or search topic to response to user
			if (requestType == RequestType.CHOICE) {
				// response the choice topic to user
				Topic topic = changeTopic(request, requestType);
				if (responseHistory.lastTopic == null) {
					// change the topic in context failed,response the topic to
					// user
					return getTopicResponse(request, requestType, topic.topicText, topic.children);
				} else {
					// change the topic in context success,response to user
					// directly
					return getAssistantResponse(request, requestType);
				}
			} else {
				// response the search topics to user
				List<Topic> topicList = SearchTopic.searchTopicDataByWord(request, topicData);
				return getTopicResponse(request, requestType, request, topicList);
			}
		} else {
			// have topic,response to user directly
			return getAssistantResponse(request, requestType);
		}
	}

	public String backHome() throws Exception {
		responseHistory = new ResponseHistory();
		return getResponse("", RequestType.TEXT);
	}

	private String getTopicResponse(String request, RequestType requestType, String keyWord, List<Topic> topicList)
			throws Exception {
		String content = TopicResponse.getContent(topicList, keyWord, topicData);
		Response topicResponse = new TopicResponse(true);
		topicResponse.setResponseData(content);
		return topicResponse.getResponseData();
	}

	private String getAssistantResponse(String request, RequestType requestType) throws Exception {
		Response response = assistant.getResponse(request, requestType, this);
		// no response will back home
		if (response == null) {
			return backHome();
		}
		return response.getResponseData();
	}

	private Topic changeTopic(String request, RequestType requestType) {
		Topic topic = SearchTopic.searchTopicDataById(request, topicData);
		// find the final topic success
		if (topic != null && topic.topicName != null && !"".equals(topic.topicName)) {
			// update the current topic in context
			responseHistory.lastTopic = topic;
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
		public Topic lastTopic;
		public String lastResponseId;
		public String lastResponseContent;
		public String lastRequestContent;
		public RequestType lastRequestType;
	}
}
