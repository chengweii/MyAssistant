package weihua.myassistant.response;

public enum MediaType {

	/**
	 * 图片
	 */
	IMAGE("0", "image"),

	/**
	 * 视频
	 */
	VIDEO("1", "video"),

	/**
	 * 音频
	 */
	AUDIO("2", "audio"),

	/**
	 * 链接
	 */
	URL("3", "url");

	private MediaType(String code, String value) {
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

	public static MediaType fromCode(String code) {
		for (MediaType entity : MediaType.values()) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}
