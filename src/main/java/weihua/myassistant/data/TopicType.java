package weihua.myassistant.data;

import weihua.myassistant.service.HelpAssistant;
import weihua.myassistant.service.IntroduceAssistant;
import weihua.myassistant.service.ScheduleAssistant;
import weihua.myassistant.service.WikiAssistant;

public enum TopicType {

	/**
	 * 帮助、咨询类
	 */
	HELP("Help", HelpAssistant.class),

	/**
	 * 介绍类
	 */
	INTRODUCE("Introduce", IntroduceAssistant.class),
	
	/**
	 * 知识类
	 */
	WIKI("Wiki", WikiAssistant.class),

	/**
	 * 日程类
	 */
	SCHEDULE("Schedule", ScheduleAssistant.class);

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
