package weihua.myassistant.response;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weihua.myassistant.util.GsonUtil;

public class MusicResponse extends BaseResponse {

	@Override
	String handleResponse(String content) throws Exception {
		Pattern pattern = Pattern.compile("#.*?#");
		Matcher matcher = pattern.matcher(content);
		String link;
		Map<String, String> map;
		while (matcher.find()) {
			link = matcher.group();
			if (link.matches("#.*?(type\\s*\\:\\s*\\'" + MediaType.AUDIO.getValue() + "\\').*?#")) {
				map = GsonUtil.getMapFromJson(link.replace("#", ""));
				content = content.replace(link,
						"<a href='javascript:void(0)' class='media-link " + map.get("type") + "-link' mediaLink='"
								+ map.get("link") + "' mediaType='" + MediaType.AUDIO.getCode() + "'>"
								+ map.get("text") + "</a>");
			}
		}
		return content;
	}

}
