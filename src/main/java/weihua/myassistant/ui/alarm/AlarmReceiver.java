package weihua.myassistant.ui.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.widget.Toast;
import weihua.myassistant.R;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;
import weihua.myassistant.ui.util.ServiceUtil;

public class AlarmReceiver extends BroadcastReceiver {



	@Override
	public void onReceive(Context context, Intent intent) {

		Toast toast = Toast.makeText(context, "sdfsdf", Toast.LENGTH_SHORT);
		toast.show();

		ServiceUtil.startService(context, MusicPlayerService.class.getName());
	}
}
