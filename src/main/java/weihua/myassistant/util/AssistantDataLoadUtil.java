package weihua.myassistant.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.ResponseData.Response;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.util.AssistantDataJson.Node;

public class AssistantDataLoadUtil {

	public static void main(String[] args) {
		generateResponse("whichChoice");
	}

	public static TopicData loadAllTopicData() {
		TopicData topicData = null;
		String topicPath = FileUtil.getInnerAssistantFileSDCardPath() + "topic/topic.json";
		String topicJsonData = FileUtil.getFileContent(topicPath);
		java.lang.reflect.Type type = new TypeToken<TopicData>() {
		}.getType();
		try {
			topicData = GsonUtil.gson.fromJson(topicJsonData, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topicData;
	}

	public static List<ResponseData> loadTopicData(String topicName, String currentTopic) {
		List<ResponseData> responseDataList = new ArrayList<ResponseData>();
		if (!topicName.equals(currentTopic)) {
			String responsePath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".json";
			String responseJsonData = FileUtil.getFileContent(responsePath);
			java.lang.reflect.Type type = new TypeToken<List<ResponseData>>() {
			}.getType();
			try {
				responseDataList = GsonUtil.gson.fromJson(responseJsonData, type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseDataList;
	}

	public static String generateResponse(String topicName) {
		String responseMmPath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".mm";
		String responseJsonPath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".json";
		generateResponse(responseMmPath, responseJsonPath, topicName);
		String topicMmPath = FileUtil.getInnerAssistantFileSDCardPath() + "topic/topic.mm";
		String topicJsonPath = FileUtil.getInnerAssistantFileSDCardPath() + "topic/topic.json";
		generateTopic(topicMmPath, topicJsonPath);
		return "Load success,please restart your app.";
	}

	private static String generateResponse(String mmPath, String jsonPath, String topicName) {
		String responseJsonString = "{}";
		String responseJsonData = FileUtil.getFileContent(mmPath);
		responseJsonData = responseJsonData.replaceAll(
				"\"objectClass\"\\s*?\\:\\s*?\"apple\\.cocoatouch\\.foundation\\.NSMutableArray\"\\s*?\\,", "");
		responseJsonData = responseJsonData
				.replaceAll("\"objectClass\"\\s*?\\:\\s*?\"apple\\.cocoatouch\\.foundation\\.NSMutableArray\"", "");
		java.lang.reflect.Type type = new TypeToken<AssistantDataJson>() {
		}.getType();
		try {
			AssistantDataJson dataJson = GsonUtil.gson.fromJson(responseJsonData, type);
			ResponseData responseData = dataJsonToResponseData(dataJson);
			responseData.topicId = "1";
			responseData.topicName = topicName;
			responseJsonString = GsonUtil.gson.toJson(responseData);
			responseJsonString = getNormalText(responseJsonString);
			responseJsonString = responseJsonString.replaceAll("null", "\"\"");
			responseJsonString = "[" + responseJsonString + "]";
			FileUtil.writeFileContent(responseJsonString, jsonPath);
		} catch (Exception e) {
			e.printStackTrace();
			responseJsonString = e.getMessage();
		}
		return responseJsonString;
	}

	private static ResponseData dataJsonToResponseData(AssistantDataJson dataJson) {
		ResponseData responseJson = new ResponseData();
		List<Response> responseList = new ArrayList<Response>();
		responseJson.responses = responseList;
		Count id = new Count();
		getResponses(dataJson.root.children, responseList, id, "");
		return responseJson;
	}

	private static void getResponses(Map<String, Node> children, List<Response> responseList, Count id,
			String responseId) {
		int conditionId = 1;
		int size = children.entrySet().size();
		for (Map.Entry<String, Node> entry : children.entrySet()) {
			Response reponse = new Response();
			reponse.id = id.getIndex();
			reponse.responseId = responseId;
			if (!"".equals(responseId) && size > 1) {
				reponse.condition = String.valueOf(conditionId);
			} else {
				reponse.condition = "";
			}
			List<String> content = new ArrayList<String>();
			content.add(entry.getValue().text);
			reponse.content = content;
			responseList.add(reponse);

			getResponses(entry.getValue().children, responseList, id, reponse.id);
			conditionId++;
		}
	}

	private static String generateTopic(String mmPath, String jsonPath) {
		String assistJsonString = "{}";
		String assistJsonData = FileUtil.getFileContent(mmPath);
		assistJsonData = assistJsonData.replaceAll(
				"\"objectClass\"\\s*?\\:\\s*?\"apple\\.cocoatouch\\.foundation\\.NSMutableArray\"\\s*?\\,", "");
		assistJsonData = assistJsonData
				.replaceAll("\"objectClass\"\\s*?\\:\\s*?\"apple\\.cocoatouch\\.foundation\\.NSMutableArray\"", "");
		java.lang.reflect.Type type = new TypeToken<AssistantDataJson>() {
		}.getType();
		try {
			AssistantDataJson dataJson = GsonUtil.gson.fromJson(assistJsonData, type);
			TopicData assistJson = dataJsonToTopicData(dataJson);
			assistJsonString = GsonUtil.gson.toJson(assistJson);
			assistJsonString = getNormalText(assistJsonString);
			assistJsonString = assistJsonString.replaceAll("null", "\"\"");
			FileUtil.writeFileContent(assistJsonString, jsonPath);
		} catch (Exception e) {
			e.printStackTrace();
			assistJsonString = e.getMessage();
		}
		return assistJsonString;
	}

	private static TopicData dataJsonToTopicData(AssistantDataJson dataJson) {
		Count count = new Count();
		TopicData topicData = new TopicData();
		topicData.listTopicMsg = "About <span class='parentTopic'>#keyword#</span>,I can provide the above help now:";
		topicData.welcomeMsg = "#timeHello#,#userName#,What can I do for you with the following:";
		topicData.errorMsg = "Sorry,I can only provide the above help.";
		topicData.topic = getTopic(dataJson.root.children, count);

		return topicData;
	}

	private static List<Topic> getTopic(Map<String, Node> children, Count menuId) {
		List<Topic> menuList = new ArrayList<Topic>();
		for (Map.Entry<String, Node> entry : children.entrySet()) {
			Topic menu = new Topic();
			menu.topicId = menuId.getIndex();
			String[] topicData = getTopicData(entry.getValue().text);
			menu.topicText = topicData[0];
			menu.topicName = topicData[1];
			menu.topicType = topicData[2] == null || "".equals(topicData[2]) ? TopicType.HELP.getCode() : topicData[2];
			menu.children = getTopic(entry.getValue().children, menuId);
			menuList.add(menu);
		}
		return menuList;
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

	public static String getNormalText(String content) {
		content = content.replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">");
		content = content.replaceAll("\\\\u0027", "'").replaceAll("\\\\u003d", "=");
		return content;
	}

	static class Count {
		private int index = 0;

		public String getIndex() {
			index = index + 1;
			return String.valueOf(index);
		}
	}
}
