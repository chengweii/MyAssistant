package weihua.myassistant.search;

import java.util.ArrayList;
import java.util.List;

import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;

public class SearchTopic implements Search {

	public static Topic searchTopicDataById(String id, TopicData topicData) {
		Topic topic = new Topic();
		getTopic(id, topicData.topic, topic);
		return topic;
	}

	private static void getTopic(String condition, List<Topic> topicList, Topic result) {
		for (Topic topic : topicList) {
			if (topic.topicId.equals(condition)) {
				result.topicId = topic.topicId;
				result.topicText = topic.topicText;
				result.topicName = topic.topicName;
				result.topicType=topic.topicType;
				result.children = topic.children;
				break;
			} else {
				if (topic.children != null && topic.children.size() > 0) {
					getTopic(condition, topic.children, result);
					if (result.topicId != null) {
						break;
					}
				}
			}
		}
	}

	public static List<Topic> searchTopicDataByWord(String word, TopicData topicData) {
		List<Topic> result = new ArrayList<Topic>();
		if ("".equals(word)) {
			for (Topic topic : topicData.topic) {
				result.add(topic);
			}
		} else {
			getTopicList(word, topicData.topic, result);
		}

		return result;
	}

	private static void getTopicList(String condition, List<Topic> topicList, List<Topic> result) {
		for (Topic topic : topicList) {
			if (topic.topicText.contains(condition)) {
				result.add(topic);
			} else {
				if (topic.children != null && topic.children.size() > 0) {
					getTopicList(condition, topic.children, result);
				}
			}
		}
	}

}
