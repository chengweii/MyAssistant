package weihua.myassistant.response;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weihua.myassistant.util.GsonUtil;

public class VideoResponse extends BaseResponse {

	@Override
	String handleResponse(String content) {
		Pattern pattern = Pattern.compile("#.*?#");
		Matcher matcher = pattern.matcher(content);
		String link;
		Map<String, String> map;
		while (matcher.find()) {
			link = matcher.group();
			if (link.matches("#.*?(type\\s*\\:\\s*\\'video\\').*?#")) {
				map = GsonUtil.getMapFromJson(link.replace("#", ""));
				content = content.replace(link, "<a href='javascript:void(0)' class='" + map.get("type")
						+ "-link' link='" + map.get("link") + "'>" + map.get("text") + "</a>");
			}
		}
		return content;
	}

}
