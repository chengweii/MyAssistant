package weihua.myassistant.response;

public interface Response {
	public String getResponseData(String content) throws Exception;

	public void setNextHandler(Response response);

	public Response getNextHandler();
}
