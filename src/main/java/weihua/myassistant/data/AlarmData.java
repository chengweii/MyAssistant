package weihua.myassistant.data;

public class AlarmData implements Data {
	public String title;
	public String text;
	public String subText;
	public String contentInfo;
	public String ticker;
	public String iconLink;
	public String musicLink;
	public String intentAction;
	public String extraInfo;

	@Override
	public String toString() {
		return "[title:" + title + ",text:" + text + ",subText:" + subText + ",ticker:" + ticker + ",iconLink:"
				+ iconLink + ",musicLink:" + musicLink + ",intentAction:" + intentAction + ",extraInfo:" + extraInfo
				+ "]";
	}
}
