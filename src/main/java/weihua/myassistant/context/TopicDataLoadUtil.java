package weihua.myassistant.context;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.reflect.TypeToken;

import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;

public class TopicDataLoadUtil {

	private static Logger loger = Logger.getLogger(TopicDataLoadUtil.class);

	public static void main(String[] args) throws Exception {
		loadAllTopicDataFromWeb();
	}

	public static void loadAllTopicDataFromWeb() throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Context.topicData = getTopicDataFromGitHup();
					loger.info("Topic data refresh success.");
				} catch (Exception e) {
					loger.error("Topic data refresh failed:"+ExceptionUtil.getStackTrace(e));
				}
			}
		}).start();
	}

	public static TopicData loadAllTopicDataFromLocal() throws Exception {
		TopicData topicData = null;
		String topicPath = FileUtil.getInnerAssistantFileSDCardPath() + "topic/topic.json";
		String topicJsonData = FileUtil.getFileContent(topicPath);
		java.lang.reflect.Type type = new TypeToken<TopicData>() {
		}.getType();
		topicData = GsonUtil.gson.fromJson(topicJsonData, type);
		return topicData;
	}

	private static TopicData getTopicDataFromGitHup() throws Exception {
		TopicData topicData = new TopicData();
		topicData.listTopicMsg = "About <span class='parentTopic'>#keyword#</span>,I can provide the above help now:";
		topicData.welcomeMsg = "#timeHello#,#userName#,What can I do for you with the following:";
		topicData.errorMsg = "Sorry,I can only provide the above help.";

		String topicPath = FileUtil.getInnerAssistantFileSDCardPath() + "topic/topic.json";

		Document doc = Jsoup
				.connect(
						"https://github.com/chengweii/myassistant/blob/develop/src/main/source/assistant/topic/topic.md")
				.get();
		Elements showContent = doc.getElementsByAttributeValue("class", "markdown-body entry-content");
		Element rootUl = showContent.get(0).getElementsByTag("ul").get(1);
		Elements liList = rootUl.children();
		List<Topic> children = new ArrayList<Topic>();
		Count topicId = new Count();
		getChildrenTopic(children, liList, topicId);
		topicData.topic = children;

		String assistJsonString = GsonUtil.gson.toJson(topicData);
		assistJsonString = getNormalText(assistJsonString);
		FileUtil.writeFileContent(assistJsonString, topicPath);

		return topicData;
	}

	private static void getChildrenTopic(List<Topic> children, Elements liList, Count topicId) {
		for (Element element : liList) {
			Topic parent = new Topic();
			parent.topicId = topicId.getIndex();
			parent.topicName = "wiki" + parent.topicId;
			if (element.children().size() > 0 && "a".equals(element.child(0).tagName())) {
				parent.topicType = TopicType.WIKI.getCode();
				parent.topicText = element.child(0).ownText();
				parent.topicLink = element.child(0).attr("href");
			} else {
				String[] topicData = getTopicData(element.ownText());
				parent.topicText = topicData[0];
				parent.topicName = topicData[1];
				parent.topicType = topicData[2] == null || "".equals(topicData[2]) ? TopicType.HELP.getCode()
						: topicData[2];
			}
			children.add(parent);

			if (element.children().size() > 0 && "ul".equals(element.child(0).tagName())) {
				List<Topic> childrenList = new ArrayList<Topic>();
				parent.children = childrenList;
				getChildrenTopic(childrenList, element.child(0).children(), topicId);
			}
		}
	}

	private static String[] getTopicData(String text) {
		String[] result = new String[3];
		String[] topicData = text.split("#");
		int index = 0;
		for (String s : topicData) {
			result[index] = s;
			index++;
		}
		return result;
	}

	private static String getNormalText(String content) {
		content = content.replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">");
		content = content.replaceAll("\\\\u0027", "'").replaceAll("\\\\u003d", "=");
		return content;
	}

	private static class Count {
		private int index = 0;

		public String getIndex() {
			index = index + 1;
			return String.valueOf(index);
		}
	}
}
