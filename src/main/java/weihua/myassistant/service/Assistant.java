package weihua.myassistant.service;

import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.Response;

/**
 * 助手服务
 * 
 * @author chengwei2
 *
 */
public interface Assistant {
	public Response getResponse(String request, RequestType requestType, Context context) throws Exception;
}
