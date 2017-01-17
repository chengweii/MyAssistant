package weihua.myassistant.response;

public interface Response {
	public String getResponseData(String content);

	public void setNextHandler(Response response);

	public Response getNextHandler();
}
