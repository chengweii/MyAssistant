package weihua.myassistant.data;

public class ServiceConfig {
	public String serviceId;
	public boolean enable;
	public String excuteTimePeriod;

	@Override
	public String toString() {
		return "[serviceId:" + serviceId + ",enable:" + enable + ",excuteTimePeriod:" + excuteTimePeriod + "]";
	}

	public static boolean matchTimePeriod(String excuteTimePeriod, String currentTimePeriod) {
		boolean result = false;
		String[] excuteTimePeriods = excuteTimePeriod.split(",");
		for (String timePeriod : excuteTimePeriods) {
			if (timePeriod.equals(currentTimePeriod)) {
				result = true;
				break;
			}
		}
		return result;
	}
}
