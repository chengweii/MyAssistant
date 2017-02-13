package weihua.myassistant.ui.alarm;

import android.content.Intent;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;
import weihua.myassistant.ui.common.Constans;

public class HolidayAlarmService extends AlarmService {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// String extraInfo = intent.getStringExtra("extraInfo");
		setForegroundService("明天是情人节，请记得买礼物和问候。", "明天是情人节，请记得买礼物和问候。", "明天是情人节，请记得买礼物和问候。", Constans.HOLIDAY_ALARM_SERVICE_ID,
				Constans.HOLIDAY_ALARM_SERVICE_ID);
		mediaPlayer = MediaIntent.playMusic(this, MusicPlaySource.WEB, "http://172.16.0.199/IXC7321d40d247713b278c1bc035a77324c/hot/2010/08-26/370453.mp3");
		return 0;
	}
}