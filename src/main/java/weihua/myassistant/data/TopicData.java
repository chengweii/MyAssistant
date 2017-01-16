package weihua.myassistant.data;

import java.util.List;

public class TopicData implements Data {
	public List<String> welcomeMsg;
	public List<String> errorMsg;
	public String listTopicMsg;
	public List<Topic> topic;

	public static class Topic {
		public String topicId;
		public String topicType;
		public String topicText;
		public String topicName;
		public List<Topic> children;
	}
}
