package weihua.myassistant.request;

public enum RequestType {

	/**
	 * 文本
	 */
	TEXT("0", "文本"),

	/**
	 * 选择
	 */
	CHOICE("1", "选择");

	private RequestType(String code, String value) {
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

	public static RequestType fromCode(String code) {
		for (RequestType entity : RequestType.values()) {
			if (entity.getCode() == code) {
				return entity;
			}
		}
		return null;
	}
}
