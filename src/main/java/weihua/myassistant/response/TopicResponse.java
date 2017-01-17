package weihua.myassistant.response;

import java.util.List;

import weihua.myassistant.common.Constants;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;

public class TopicResponse implements Response {

	private Response nextHandler;

	@Override
	public String getResponseData(String content) {
		String response = "";
		if (nextHandler != null) {
			response = nextHandler.getResponseData(content);
		}

		return response;
	}

	@Override
	public void setNextHandler(Response response) {
		nextHandler = response;
	}

	@Override
	public Response getNextHandler() {
		return nextHandler;
	}

	public void initHandlers() {
		CommonResponse commonResponse = new CommonResponse(true);
		nextHandler = commonResponse;
	}

	public String getContent(List<Topic> children, String topicName, TopicData topicData) {
		StringBuilder sb = new StringBuilder(topicData.listTopicMsg.replace(Constants.KEYWORD_SPACE, topicName));
		sb.append("<p>");
		for (Topic child : children) {
			sb.append("<span class='choice-item' ");
			sb.append("' choiceValue='");
			sb.append(child.topicId);
			sb.append("'>");
			sb.append(child.topicText);
			sb.append("</span>");
		}
		sb.append("</p>");
		return sb.toString();
	}
}
