package weihua.myassistant.response;

public abstract class BaseResponse implements Response {

	private Response nextHandler;

	private String content;

	abstract String handleResponse(String content) throws Exception;

	@Override
	public void setResponseData(String content) throws Exception {
		this.content = content;
	}

	@Override
	public String getResponseData() throws Exception {
		if (nextHandler != null) {
			nextHandler.setResponseData(content);
			content = nextHandler.getResponseData();
		}

		return handleResponse(content);
	}

	@Override
	public void setNextHandler(Response response) {
		nextHandler = response;
	}

	@Override
	public Response getNextHandler() {
		return nextHandler;
	}
}
