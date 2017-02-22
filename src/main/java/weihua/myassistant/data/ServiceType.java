package weihua.myassistant.data;

import weihua.myassistant.service.DailyDietAssistant;
import weihua.myassistant.service.ScheduleAssistant;
import weihua.myassistant.service.SpecialDateAssistant;
import weihua.myassistant.service.TrafficAssistant;
import weihua.myassistant.service.WetherAssistant;
import weihua.myassistant.ui.common.Constans;

public enum ServiceType {

	DAILYDIET(Constans.DAILYDIET_ALARM_ID, DailyDietAssistant.class),

	SCHEDULE(Constans.SCHEDULE_ALARM_ID, ScheduleAssistant.class),

	SPECIALDATE(Constans.SPECIALDATE_ALARM_ID, SpecialDateAssistant.class),

	TRAFFIC(Constans.TRAFFIC_ALARM_ID, TrafficAssistant.class),

	WETHER(Constans.WETHER_ALARM_ID, WetherAssistant.class);

	private ServiceType(int code, Class<?> clz) {
		this.code = code;
		this.clz = clz;
	}

	private int code;
	private Class<?> clz;

	public int getCode() {
		return code;
	}

	public Class<?> getClz() {
		return clz;
	}

	public static ServiceType fromCode(int code) {
		for (ServiceType entity : ServiceType.values()) {
			if (entity.getCode() == code) {
				return entity;
			}
		}
		return null;
	}
}
