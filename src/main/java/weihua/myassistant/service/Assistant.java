package weihua.myassistant.service;

import java.util.List;

import weihua.myassistant.data.ResponseData;
import weihua.myassistant.request.RequestType;

/**
 * 助手服务
 * 
 * @author chengwei2
 *
 */
public interface Assistant {
	public String getResponse(String request, RequestType requestType, List<ResponseData> responseDataList);
}
