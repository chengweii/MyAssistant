package weihua.myassistant.service;

import java.util.Map;

import weihua.myassistant.data.Data;
import weihua.myassistant.data.ServiceConfig;
import weihua.myassistant.response.Response;

public interface AssistantService {
	public Response getResponse(String request, Map<String,Data> serviceData,ServiceConfig serviceConfig) throws Exception;
}
