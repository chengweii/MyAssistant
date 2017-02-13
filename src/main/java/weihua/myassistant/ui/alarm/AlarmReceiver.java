package weihua.myassistant.ui.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import weihua.myassistant.ui.util.ServiceUtil;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String serviceCls = intent.getStringExtra("serviceCls");
		String extraInfo = intent.getStringExtra("extraInfo");
		if (ServiceUtil.isServiceRunning(context, serviceCls)) {
			ServiceUtil.stopService(context, serviceCls);
		}
		ServiceUtil.startService(context, serviceCls, extraInfo);
	}
}
