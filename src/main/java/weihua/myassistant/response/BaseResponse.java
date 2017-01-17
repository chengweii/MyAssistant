package weihua.myassistant.response;

public class BaseResponse implements Response {

	@Override
	public String getResponseData(String content) {
		return content;
	}
}
