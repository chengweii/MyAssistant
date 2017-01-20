package weihua.myassistant.response;

public abstract class BaseResponse implements Response {

	private Response nextHandler;

	abstract String handleResponse(String content);

	@Override
	public String getResponseData(String content) {
		if (nextHandler != null) {
			content = nextHandler.getResponseData(content);
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
