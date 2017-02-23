package weihua.myassistant.response;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import weihua.myassistant.common.Constants;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;

public class TopicResponse implements Response {

	private Response nextHandler;

	private String content;

	public TopicResponse(boolean initHandlers) {
		if (initHandlers) {
			initHandlers();
		}
	}

	@Override
	public void setResponseData(String content) throws Exception {
		this.content = content;
	}

	@Override
	public String getResponseData() throws Exception {
		if (nextHandler != null) {
			nextHandler.setResponseData(content);
			content = nextHandler.getResponseData();
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

	private void initHandlers() {
		CommonResponse commonResponse = new CommonResponse(true);
		nextHandler = commonResponse;
	}

	public static String getContent(List<Topic> children, String topicName, TopicData topicData) {
		String narrationContent;
		StringBuilder sb = new StringBuilder();
		if (topicName != null && !"".equals(topicName)) {
			narrationContent = topicData.listTopicMsg.replace(Constants.KEYWORD_SPACE, topicName);

			if (children != null && children.size() > 0) {
				sb.append(narrationContent);
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
			} else {
				sb.append(Constants.NOTOPIC_MSG.replace(Constants.KEYWORD_SPACE, topicName));
			}
		} else {
			Date date = new Date();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			narrationContent = topicData.welcomeMsg.replace(Constants.TIMEHELLO,
					cal.get(GregorianCalendar.AM_PM) == 0 ? "Good morning" : "Good afternoon");
			narrationContent = narrationContent.replace(Constants.USERNAME, Constants.MASTER);

			if (children != null && children.size() > 0) {
				sb.append(narrationContent);
				sb.append("<p>");
				int i = 0;
				for (Topic child : children) {
					sb.append("<span class='choice-item' ");
					sb.append(" choiceValue='");
					sb.append(child.topicId);
					sb.append("' style='");
					if (i < Constants.COLORS_ARRAY.length) {
						sb.append(Constants.MAIN_TOPIC_STYLE.replace(Constants.KEYWORD_SPACE, Constants.COLORS_ARRAY[i]));
					} else {
						sb.append(Constants.MAIN_TOPIC_STYLE.replace(Constants.KEYWORD_SPACE, Constants.COLORS_ARRAY[0]));
					}
					sb.append("'>");
					sb.append(child.topicText);
					sb.append("</span>");
					i++;
				}
				sb.append("</p>");
			} else {
				sb.append(Constants.NOTOPIC_MSG.replace(Constants.KEYWORD_SPACE, topicName));
			}
		}

		return sb.toString();
	}

}
