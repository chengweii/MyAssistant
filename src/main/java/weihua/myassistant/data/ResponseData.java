package weihua.myassistant.data;

import java.util.List;

public class ResponseData implements Data {
	public String topicId;
	public String topicName;
	public List<Response> responses;

	public static class Response {
		public String id;
		public String responseId;
		public String condition;
		public List<String> content;
	}
}
