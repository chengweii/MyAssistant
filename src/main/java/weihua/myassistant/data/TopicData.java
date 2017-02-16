package weihua.myassistant.data;

import java.util.List;

public class TopicData implements Data {
	public String welcomeMsg;
	public String errorMsg;
	public String listTopicMsg;
	public List<Topic> topic;

	public static class Topic {
		public String topicId;
		public String topicType;
		public String topicText;
		public String topicLink;
		public String topicName;
		public List<Topic> children;
	}
}
