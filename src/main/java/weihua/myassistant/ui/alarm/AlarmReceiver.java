package weihua.myassistant.ui.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.ServiceUtil;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
		ServiceUtil.startService(context, AlarmService.class, extraInfo);
	}
}
