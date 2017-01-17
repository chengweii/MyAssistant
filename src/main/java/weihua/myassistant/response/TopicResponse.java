package weihua.myassistant.response;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import weihua.myassistant.common.Constants;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;

public class TopicResponse implements Response {

	private Response nextHandler;

	@Override
	public String getResponseData(String content) {
		if (nextHandler != null) {
			content = nextHandler.getResponseData(content);
		}

		return content;
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
		String narrationContent;
		if (topicName != null && !"".equals(topicName)) {
			narrationContent = topicData.listTopicMsg.replace(Constants.KEYWORD_SPACE, topicName);
		} else {
			Date date = new Date();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			narrationContent = topicData.welcomeMsg.replace(Constants.TIMEHELLO,
					cal.get(GregorianCalendar.AM_PM) == 0 ? "Good morning" : "Good afternoon");
			narrationContent = narrationContent.replace(Constants.USERNAME, Constants.MASTER);
		}

		StringBuilder sb = new StringBuilder(narrationContent);
		sb.append("<p>");
		for (Topic child : children) {
			sb.append("<span class='choice-item' ");
			sb.append(" choiceValue='");
			sb.append(child.topicId);
			sb.append("'>");
			sb.append(child.topicText);
			sb.append("</span>");
		}
		sb.append("</p>");
		return sb.toString();
	}
}
