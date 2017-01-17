package weihua.myassistant.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

	public static Map<String, String> getMapFromJson(String json) {
		Map<String, String> map = new HashMap<String, String>();
		java.lang.reflect.Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		try {
			map = GsonUtil.gson.fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
