package weihua.myassistant;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.MediaType;
import weihua.myassistant.ui.CustomerWebChromeClient;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntent.MusicPlaySource;
import weihua.myassistant.ui.alarm.AlarmReceiver;
import weihua.myassistant.ui.util.ServiceUtil;
import weihua.myassistant.util.AssistantDataLoadUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;

public class MainActivity extends Activity {
	private WebView webView;
	private static String viewFilePath = "index.html";
	private Context assistantContext;

	@JavascriptInterface
	public String getResponse(String request, String requestType) {
		String msg = "";
		try {
			msg = assistantContext.getResponse(request, RequestType.fromCode(requestType));
			showNotification(msg, request, msg);
			alarmShow();
		} catch (Exception e) {
			msg = ExceptionUtil.getStackTrace(e);
		}
		return msg;
	}
	
	private void alarmShow(){
		// 进行闹铃注册
		Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

		// 过10s 执行这个闹铃
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);

		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}

	/**
	 * @param musicPlaySource
	 * @param mediaLink
	 *            local_demo:/test.mp3;
	 *            web_demo:http://42.81.26.18/mp3.9ku.com/m4a/637791.m4a
	 */
	@JavascriptInterface
	public void playMusic(String musicPlaySource, String mediaLink) {
		MediaIntent.playMusic(getApplicationContext(), MusicPlaySource.fromCode(musicPlaySource), mediaLink);
	}

	@JavascriptInterface
	public String backHome() {
		String msg = "";
		try {
			msg = assistantContext.backHome();
		} catch (Exception e) {
			msg = ExceptionUtil.getStackTrace(e);
		}
		return msg;
	}

	@JavascriptInterface
	public void showMedia(String mediaLink, String mediaType) {
		Intent it = MediaIntent.getMediaIntent(mediaLink, MediaType.fromCode(mediaType));
		startActivity(it);
	}

	@JavascriptInterface
	public String loadDataFile(String topicName) {
		String msg = "";
		try {
			msg = AssistantDataLoadUtil.generateResponse(topicName);
		} catch (Exception e) {
			msg = ExceptionUtil.getStackTrace(e);
		}
		return msg;
	}

	@JavascriptInterface
	public void showMsg(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	private static final int NO_1 = 0x1;

	public void showNotification(String ticker, String title, String text) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(
				android.content.Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setWhen(System.currentTimeMillis());
		builder.setDefaults(Notification.DEFAULT_ALL);
		Notification notification = builder.build();
		notificationManager.notify(NO_1, notification);

		//Intent intent = new Intent();
		//intent.setClass(getApplicationContext(), NotificationActivity.class);
		// intent.putExtra(); // 需要时 传入相应的参数
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		//startActivity(intent);
	}

	public void showRichNotification(String ticker, String title, String text) {

	}

	/**
	 * app package name can get from .apk file by apkhelper.exe
	 * 
	 * @param packageName
	 *            open wikiHow demo:com.wikihow.wikihowapp
	 */
	@JavascriptInterface
	public void startAppByPackageName(String packageName) {
		PackageManager packageManager = getPackageManager();
		Intent intent = new Intent();
		intent = packageManager.getLaunchIntentForPackage(packageName);
		startActivity(intent);
	}

	public void initView() {
		webView = (WebView) findViewById(R.id.wv1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(this, "mainActivity");
		webView.loadUrl("file:///android_asset/" + viewFilePath);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebChromeClient(new CustomerWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - exitTime > 2000) {
			Toast.makeText(this, "Press once more exit", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.isCheckable()) {
			menuItem.setChecked(true);
		}
		switch (menuItem.getItemId()) {
		case R.id.action_loadresponse:
			showMsg("action_loadresponse");
			break;
		case R.id.action_edittopic:
			showMsg("action_edittopic");
			break;
		case R.id.action_aboutme:
			showMsg("action_aboutme");
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.main);

		FileUtil.assistantRootPath = Environment.getExternalStorageDirectory().getPath() + "/assistant/";

		assistantContext = new Context();

		initView();

	}

}
