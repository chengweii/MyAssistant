package weihua.myassistant.response;

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

	public static void main(String[] args) {
		System.out.println(new CommonResponse(true).getResponseData(
				"测试#{type:'image',link:'test1.jpg',text:'image'}#测试#{type:'audio',link:'test2.mp3',text:'audio'}#测试#{type:'video',link:'test.mp4',text:'video'}#测试"));
	}
}
