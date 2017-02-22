package weihua.myassistant.data;

public class ServiceConfig {
	public String serviceId;
	public boolean enable;
	public String excuteTimePeriod;

	@Override
	public String toString() {
		return "[serviceId:" + serviceId + ",enable:" + enable + ",excuteTimePeriod:" + excuteTimePeriod + "]";
	}
}
