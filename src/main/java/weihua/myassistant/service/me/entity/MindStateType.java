package weihua.myassistant.service.me.entity;

import weihua.myassistant.util.IEnum;

public enum MindStateType  implements IEnum{

	FINE("1", "良好"),

	TERRIBLE("2", "糟糕");

	private MindStateType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	private String code;
	private String value;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getValue() {
		return value;
	}

	public static MindStateType fromCode(String code) {
		for (MindStateType entity : MindStateType.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
