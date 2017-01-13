package weihua.myassistant.ui;

public enum MediaIntentType {

	/**
	 * 图片
	 */
	IMAGE("0", "图片"),

	/**
	 * 视频
	 */
	VIDEO("1", "视频"),

	/**
	 * 音频
	 */
	AUDIO("2", "音频"),

	/**
	 * 链接
	 */
	URL("3", "链接");

	private MediaIntentType(String code, String value) {
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

	public static MediaIntentType fromCode(String code) {
		for (MediaIntentType entity : MediaIntentType.values()) {
			if (entity.getCode() == code) {
				return entity;
			}
		}
		return null;
	}
}
