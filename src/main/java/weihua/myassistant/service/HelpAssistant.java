package weihua.myassistant.service;

import java.util.ArrayList;
import java.util.List;

import weihua.myassistant.context.Context.ResponseHistory;
import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.ResponseData.Response;
import weihua.myassistant.request.RequestType;

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
		return null;
	}

	private Response findResponseData(String request, RequestType requestType, List<ResponseData> responseDataList,
			ResponseHistory responseHistory) {
		Response response = null;
		if (responseDataList != null && responseDataList.size() > 0) {
			ResponseData responseData = responseDataList.get(0);

			List<Response> responseList = getResponseList(responseData.responses, responseHistory.lastResponseId);

			if (responseList.size() == 1) {
				for (Response entity : responseData.responses) {
					if (entity.responseId.equals(responseHistory.lastResponseId)) {
						response = entity;
						break;
					}
				}
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
					}
				}
			}
		}

		return response;
	}

	private List<Response> getResponseList(List<Response> responseList, String responseId) {
		List<Response> list = new ArrayList<Response>();
		for (Response entity : responseList) {
			if (entity.responseId.equals(responseId)) {
				list.add(entity);
			}
		}
		return list;
	}

}
