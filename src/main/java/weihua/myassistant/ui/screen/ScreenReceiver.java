package weihua.myassistant.ui.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import weihua.myassistant.ui.alarm.AlarmService;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.ServiceUtil;

public class ScreenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
			if (action.equals(Intent.ACTION_USER_PRESENT)) {
				String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
				ServiceUtil.startService(context, AlarmService.class, extraInfo);
			} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
			}
	}

}