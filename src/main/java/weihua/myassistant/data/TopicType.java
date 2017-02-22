package weihua.myassistant.data;

import weihua.myassistant.service.DailyDietAssistant;
import weihua.myassistant.service.HelpAssistant;
import weihua.myassistant.service.IntroduceAssistant;
import weihua.myassistant.service.ScheduleAssistant;
import weihua.myassistant.service.WikiAssistant;

public enum TopicType {

	HELP("Help", HelpAssistant.class),

	INTRODUCE("Introduce", IntroduceAssistant.class),

	WIKI("Wiki", WikiAssistant.class);

	private TopicType(String code, Class<?> clz) {
		this.code = code;
		this.clz = clz;
	}

	private String code;
	private Class<?> clz;

	public String getCode() {
		return code;
	}

	public Class<?> getClz() {
		return clz;
	}

	public static TopicType fromCode(String code) {
		for (TopicType entity : TopicType.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
