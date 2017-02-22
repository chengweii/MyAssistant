package weihua.myassistant.data;

public class AlarmData implements Data {
	public String title;
	public String text;
	public String subText;
	public String ticker;
	public String iconLink;
	public String musicLink;
	public String intentAction;

	@Override
	public String toString() {
		return "[title:" + title + ",text:" + text + ",subText:" + subText + ",ticker:" + ticker + ",iconLink:"
				+ iconLink + ",musicLink:" + musicLink + ",intentAction:" + intentAction + "]";
	}
}
