package weihua.myassistant.service.me.entity;

public enum BodyStateType implements IEnum{

	FINE("1", "良好"),

	TIRED("2", "累");

	private BodyStateType(String code, String value) {
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

	public static BodyStateType fromCode(String code) {
		for (BodyStateType entity : BodyStateType.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
