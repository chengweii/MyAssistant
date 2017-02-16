package weihua.myassistant.response;

public interface Response {
	
	public void setResponseData(String content) throws Exception;
	
	public String getResponseData() throws Exception;

	public void setNextHandler(Response response);

	public Response getNextHandler();
}
