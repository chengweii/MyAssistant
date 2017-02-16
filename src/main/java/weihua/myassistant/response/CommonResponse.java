package weihua.myassistant.response;

public class CommonResponse extends BaseResponse implements Response{

	public CommonResponse(boolean initHandlers) {
		if (initHandlers) {
			initHandlers();
		}
	}

	@Override
	String handleResponse(String content) {
		return content;
	}

	private void initHandlers() {
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

	public static void main(String[] args) throws Exception {
		Response response = new CommonResponse(true);
		response.setResponseData(
				"测试#{type:'image',link:'test1.jpg',text:'image'}#测试#{type:'audio',link:'test2.mp3',text:'audio'}#测试#{type:'video',link:'test.mp4',text:'video'}#测试{type:'url',link:'www.baidu.com',text:'url'}#测试");
		System.out.println(response.getResponseData());
	}
}
