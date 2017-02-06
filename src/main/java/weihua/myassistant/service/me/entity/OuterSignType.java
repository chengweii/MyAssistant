package weihua.myassistant.service.me.entity;

public enum OuterSignType  implements IEnum{

	/**
	 * 色
	 */
	SEE("1", "色"),

	/**
	 * 声
	 */
	HEAR("2", "声"),

	/**
	 * 香
	 */
	SMELL("3", "香"),

	/**
	 * 味
	 */
	TASTE("4", "味"),

	/**
	 * 触
	 */
	TOUCH("5", "触"),

	/**
	 * 法
	 */
	THOUGHT("6", "法");

	private OuterSignType(String code, String value) {
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

	public static OuterSignType fromCode(String code) {
		for (OuterSignType entity : OuterSignType.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
