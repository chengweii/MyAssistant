package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import weihua.myassistant.context.Context;
import weihua.myassistant.context.Context.ResponseHistory;
import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.TopicType;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.response.Response;
import weihua.myassistant.util.FileUtil;
import weihua.myassistant.util.GsonUtil;
import weihua.myassistant.util.StringUtil;

public class HelpAssistant implements Assistant {

	private List<ResponseData> responseDataList = null;

	@Override
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception {
		if (context.responseHistory.lastTopic.topicType.equals(TopicType.HELP.getCode())) {
			responseDataList = loadTopicData(context.responseHistory.lastTopic.topicName);
			ResponseData.Response response = findResponseData(request, requestType, context.responseHistory);
			if (response != null) {
				context.responseHistory.lastResponseId = response.id;
				context.responseHistory.lastRequestType = requestType;
				context.responseHistory.lastResponseContent = StringUtil.getRandomContent(response.content);
				context.responseHistory.lastResponseTime = new Date();

				String responseContent = getContent(response);

				CommonResponse commonResponse = new CommonResponse(true);
				commonResponse.setResponseData(responseContent);
				return commonResponse;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	private ResponseData.Response findResponseData(String request, RequestType requestType,
			ResponseHistory responseHistory) {
		ResponseData.Response response = null;
		if (responseDataList != null && responseDataList.size() > 0) {
			ResponseData responseData = responseDataList.get(0);

			List<ResponseData.Response> responseList = getResponseList(responseData.responses,
					responseHistory.lastResponseId);

			if (responseList.size() == 1) {
				response = responseList.get(0);
			} else if (responseList.size() > 1) {
				if (requestType == RequestType.TEXT) {
					response = new ResponseData.Response();
					response.content = new ArrayList<String>();
					response.content.add("Please choice one item at least.");
					response.id = responseHistory.lastResponseId;
				} else if (requestType == RequestType.CHOICE) {
					for (ResponseData.Response entity : responseList) {
						if (entity.condition.equals(request)) {
							response = entity;
							break;
						}
					}
					responseList = getResponseList(responseData.responses, response.id);
					if (responseList.size() > 0) {
						response = responseList.get(0);
					} else {
						response = null;
					}
				}
			}
		}

		return response;
	}

	private List<ResponseData> loadTopicData(String topicName) throws Exception {
		List<ResponseData> responseDataList = new ArrayList<ResponseData>();
		String responsePath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".json";
		String responseJsonData = FileUtil.getFileContent(responsePath);
		java.lang.reflect.Type type = new TypeToken<List<ResponseData>>() {
		}.getType();
		try {
			responseDataList = GsonUtil.gson.fromJson(responseJsonData, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDataList;
	}

	private String getContent(ResponseData.Response response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append(StringUtil.getRandomContent(response.content));
		sb.append("</p>");

		if (responseDataList != null && responseDataList.size() > 0) {
			ResponseData responseData = responseDataList.get(0);

			List<ResponseData.Response> choiceList = new ArrayList<ResponseData.Response>();
			for (ResponseData.Response child : responseData.responses) {
				if (response.id.equals(child.responseId)) {
					choiceList.add(child);
				}
			}

			if (choiceList.size() > 1) {
				sb.append("<p>");
				for (ResponseData.Response child : choiceList) {
					sb.append("<span class='choice-item' ");
					sb.append(" choiceValue='");
					sb.append(child.condition);
					sb.append("'>");
					sb.append(StringUtil.getRandomContent(child.content));
					sb.append("</span>");
				}
				sb.append("</p>");
			}

		}

		return sb.toString();
	}

	private List<ResponseData.Response> getResponseList(List<ResponseData.Response> responseList, String responseId) {
		if (responseId == null) {
			responseId = "";
		}
		List<ResponseData.Response> list = new ArrayList<ResponseData.Response>();
		for (ResponseData.Response entity : responseList) {
			if (entity.responseId.equals(responseId)) {
				list.add(entity);
			}
		}
		return list;
	}

	public static String generateResponse(String topicName) throws Exception {
		String responseMmPath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".mm";
		String responseJsonPath = FileUtil.getInnerAssistantFileSDCardPath() + "response/" + topicName + ".json";
		generateResponse(responseMmPath, responseJsonPath, topicName);
		return "Load success,please restart your app.";
	}

	private static String generateResponse(String mmPath, String jsonPath, String topicName) throws Exception {
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
		List<ResponseData.Response> responseList = new ArrayList<ResponseData.Response>();
		responseJson.responses = responseList;
		Count id = new Count();
		getResponses(dataJson.root.children, responseList, id, "");
		return responseJson;
	}

	private static void getResponses(Map<String, AssistantDataJson.Node> children,
			List<ResponseData.Response> responseList, Count id, String responseId) {
		int conditionId = 1;
		int size = children.entrySet().size();
		for (Map.Entry<String, AssistantDataJson.Node> entry : children.entrySet()) {
			ResponseData.Response reponse = new ResponseData.Response();
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

	private static String getNormalText(String content) {
		content = content.replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">");
		content = content.replaceAll("\\\\u0027", "'").replaceAll("\\\\u003d", "=");
		return content;
	}

	static class AssistantDataJson {
		public Node root;

		public static class Node {
			public String text;
			public LinkedHashMap<String, Node> children;
		}
	}

	private static class Count {
		private int index = 0;

		public String getIndex() {
			index = index + 1;
			return String.valueOf(index);
		}
	}
}
