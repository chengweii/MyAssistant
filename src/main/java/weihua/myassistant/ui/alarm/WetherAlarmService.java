package weihua.myassistant.ui.alarm;

import android.content.Intent;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;
import weihua.myassistant.ui.common.Constans;

public class WetherAlarmService extends AlarmService {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// String extraInfo = intent.getStringExtra("extraInfo");
		setForegroundService("北京市通州区今天实况：4度 晴，湿度：31%，西北风：3级。", "天凉了，多穿点。", "天凉了，墨迹天气建议您在羊毛衫外面套上厚外套，年老体弱者可以穿着呢大衣增加保暖系数。", Constans.WETHER_ALARM_SERVICE_ID,
				Constans.WETHER_ALARM_SERVICE_ID);
		mediaPlayer = MediaIntent.playMusic(this, MusicPlaySource.WEB, "http://42.81.26.18/mp3.9ku.com/m4a/637791.m4a");
		return 0;
	}
}