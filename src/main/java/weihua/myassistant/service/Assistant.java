package weihua.myassistant.service;

import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;

/**
 * 助手服务
 * 
 * @author chengwei2
 *
 */
public interface Assistant {
	public String getResponse(String request, RequestType requestType, Context context);
}
