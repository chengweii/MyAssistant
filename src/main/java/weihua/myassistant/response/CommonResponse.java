package weihua.myassistant.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import weihua.myassistant.common.Constants;
import weihua.myassistant.data.ResponseData;
import weihua.myassistant.data.TopicData;
import weihua.myassistant.data.ResponseData.Response;
import weihua.myassistant.data.TopicData.Topic;
import weihua.myassistant.util.StringUtil;

public class CommonResponse extends BaseResponse {

	public CommonResponse(boolean initHandlers) {
		if (initHandlers) {
			initHandlers();
		}
	}

	@Override
	String handleResponse(String content) {
		return content;
	}

	public void initHandlers() {
		TextResponse textResponse = new TextResponse();
		ImageResponse imageResponse = new ImageResponse();
		textResponse.setNextHandler(imageResponse);

		MusicResponse musicResponse = new MusicResponse();
		imageResponse.setNextHandler(musicResponse);

		VideoResponse videoResponse = new VideoResponse();
		musicResponse.setNextHandler(videoResponse);

		LinkResponse linkResponse = new LinkResponse();
		videoResponse.setNextHandler(linkResponse);

		setNextHandler(imageResponse);
	}

	public String getContent(List<ResponseData> responseDataList, Response response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append(StringUtil.getRandomContent(response.content));
		sb.append("</p>");

		if (responseDataList != null && responseDataList.size() > 0) {
			ResponseData responseData = responseDataList.get(0);

			List<Response> choiceList = new ArrayList<Response>();
			for (Response child : responseData.responses) {
				if (response.id.equals(child.responseId)) {
					choiceList.add(child);
				}
			}

			if (choiceList.size() > 1) {
				sb.append("<p>");
				for (Response child : choiceList) {
					sb.append("<span class='choice-item' ");
					sb.append(" choiceValue='");
					sb.append(child.condition);
					sb.append("'>");
					sb.append(StringUtil.getRandomContent(child.content));
					sb.append("</span>");
				}
				sb.append("</p>");
			}

		}

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(new CommonResponse(true).getResponseData(
				"测试#{type:'image',link:'test1.jpg',text:'image'}#测试#{type:'audio',link:'test2.mp3',text:'audio'}#测试#{type:'video',link:'test.mp4',text:'video'}#测试"));
	}
}
