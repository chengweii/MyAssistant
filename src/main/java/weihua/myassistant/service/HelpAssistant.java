package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weihua.myassistant.context.Context.ResponseHistory;
import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.ResponseData.Response;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.CommonResponse;
import weihua.myassistant.util.StringUtil;

/**
 * 帮助、咨询助手服务
 * 
 * @author chengwei2
 * 
 */
public class HelpAssistant implements Assistant {

	@Override
	public String getResponse(String request, RequestType requestType, List<ResponseData> responseDataList,
			ResponseHistory responseHistory) {
		Response response = findResponseData(request, requestType, responseDataList, responseHistory);
		if (response != null) {
			responseHistory.lastResponseId = response.id;
			responseHistory.lastRequestType = requestType;
			responseHistory.lastResponseContent = StringUtil.getRandomContent(response.content);
			responseHistory.lastResponseTime = new Date();

			CommonResponse commonResponse = new CommonResponse(true);
			String responseContent = commonResponse.getContent(responseDataList, response);
			return commonResponse.getResponseData(responseContent);
		} else {
			return null;
		}
	}

	private Response findResponseData(String request, RequestType requestType, List<ResponseData> responseDataList,
			ResponseHistory responseHistory) {
		Response response = null;
		if (responseDataList != null && responseDataList.size() > 0) {
			ResponseData responseData = responseDataList.get(0);

			List<Response> responseList = getResponseList(responseData.responses, responseHistory.lastResponseId);

			if (responseList.size() == 1) {
				response = responseList.get(0);
			} else if (responseList.size() > 1) {
				if (requestType == RequestType.TEXT) {
					response = new Response();
					response.content = new ArrayList<String>();
					response.content.add("Please choice one item at least.");
					response.id = responseHistory.lastResponseId;
				} else if (requestType == RequestType.CHOICE) {
					for (Response entity : responseList) {
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

	private List<Response> getResponseList(List<Response> responseList, String responseId) {
		if (responseId == null) {
			responseId = "";
		}
		List<Response> list = new ArrayList<Response>();
		for (Response entity : responseList) {
			if (entity.responseId.equals(responseId)) {
				list.add(entity);
			}
		}
		return list;
	}

}
