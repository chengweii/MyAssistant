package weihua.myassistant.ui.screen;

import org.apache.log4j.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import weihua.myassistant.ui.alarm.AlarmService;
import weihua.myassistant.ui.common.Constans;
import weihua.myassistant.ui.util.Log4JUtil;
import weihua.myassistant.ui.util.ServiceUtil;

public class ScreenReceiver extends BroadcastReceiver {

	private static Logger loger = Logger.getLogger(ScreenReceiver.class);
	
	static{
		Log4JUtil.configure();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		loger.info("ScreenReceiver action:" + action);
		if (action.equals(Intent.ACTION_USER_PRESENT)) {
			String extraInfo = intent.getStringExtra(Constans.ALARM_EXTRA_INFO);
			ServiceUtil.startService(context, AlarmService.class, extraInfo);
		} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
		} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
		}
	}

}