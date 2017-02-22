package weihua.myassistant.ui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ScreenUtil {

	/**
	 * Screen on off listener must be registed in service by dy dynamic way,Otherwise it will not work
	 * @param context
	 * @param receiver
	 */
	public static void registerScreenReceiver(Context context,BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(receiver, filter);
	}

}
