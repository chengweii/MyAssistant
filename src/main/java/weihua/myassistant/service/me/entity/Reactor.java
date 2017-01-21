package weihua.myassistant.service.me.entity;

import weihua.myassistant.service.me.parts.Body;
import weihua.myassistant.service.me.parts.Mind;
import weihua.myassistant.service.me.parts.Subconscious;
import weihua.myassistant.service.me.parts.Values;

public enum Reactor {

	BODY(Body.class.getName(), "Body"),

	MIND(Mind.class.getName(), "Mind"),

	SUBCONSCIOUS(Subconscious.class.getName(), "Subconscious"),

	VALUES(Values.class.getName(), "Values");

	private Reactor(String code, String value) {
		this.code = code;
		this.value = value;
	}

	private String code;
	private String value;

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static Reactor fromCode(String code) {
		for (Reactor entity : Reactor.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
