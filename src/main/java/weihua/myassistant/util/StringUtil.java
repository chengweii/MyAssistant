package weihua.myassistant.util;

import java.util.List;

public class StringUtil {
	public static String getRandomContent(List<String> content) {
		String msg = "No content could show.";
		if (content != null && content.size() > 0) {
			int index = (int) (Math.random() * content.size());
			msg = content.get(index);
		}
		return msg;
	}
}
