package weihua.myassistant.data;

import weihua.myassistant.service.DailyDietAssistant;
import weihua.myassistant.service.ScheduleAssistant;
import weihua.myassistant.service.SpecialDateAssistant;
import weihua.myassistant.service.TrafficAssistant;
import weihua.myassistant.service.WetherAssistant;
import weihua.myassistant.ui.common.Constans;

public enum ServiceType {

	DAILYDIET(Constans.DAILYDIET_ALARM_ID, DailyDietAssistant.class.getName()),

	SCHEDULE(Constans.SCHEDULE_ALARM_ID, ScheduleAssistant.class.getName()),

	SPECIALDATE(Constans.SPECIALDATE_ALARM_ID, SpecialDateAssistant.class.getName()),

	TRAFFIC(Constans.TRAFFIC_ALARM_ID, TrafficAssistant.class.getName()),

	WETHER(Constans.WETHER_ALARM_ID, WetherAssistant.class.getName());

	private ServiceType(int code, String clz) {
		this.code = code;
		this.clz = clz;
	}

	private int code;
	private String clz;

	public int getCode() {
		return code;
	}

	public String getClz() {
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
