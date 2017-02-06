package weihua.myassistant.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
	public static Gson gson = null;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.setPrettyPrinting().serializeNulls().create();
	}

	public static Map<String, String> getMapFromJson(String json) throws Exception {
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> map = GsonUtil.gson.fromJson(json, type);
		return map;
	}

	public static <T> T getEntityFromJson(String json, TypeToken<?> typeToken) throws Exception {
		T data = GsonUtil.gson.fromJson(json, typeToken.getType());
		return data;
	}

	public static String toJson(Object src) throws Exception {
		return GsonUtil.gson.toJson(src);
	}

	public static void main(String[] args) {
	}

}
