package weihua.myassistant.context;

import java.util.Date;
import java.util.List;

import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.request.RequestType;
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
		changeTopic(request, requestType);
		return assistant.getResponse(request, requestType, responseDataList);
	}

	private void changeTopic(String request, RequestType requestType) {

		if (responseHistory.lastTopicName == null) {
			Topic topic = null;
			if (requestType == RequestType.CHOICE) {
				topic = SearchTopic.searchTopicDataById(request, topicData);
			} else {
				topic = SearchTopic.searchTopicDataByWord(request, topicData);
			}
			if (topic != null) {
				AssistantDataLoadUtil.loadTopicData(topic.topicName, responseHistory.lastTopicName, responseDataList);
				responseHistory.lastTopicName = topic.topicName;
				try {
					assistant = (Assistant) TopicType.valueOf(topic.topicType).getClz().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		if (assistant == null) {
			try {
				assistant = (Assistant) TopicType.HELP.getClz().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
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
