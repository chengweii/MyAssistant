package weihua.myassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.ui.MediaIntentType;
import weihua.myassistant.ui.MyWebChromeClient;

public class MainActivity extends Activity {
	private WebView m_wv1;
	private static String assistantRootPath = "/assistant/";
	private static String viewFilePath = "index.html";

	@JavascriptInterface
	public String getResponse(String request, String requestType) {
		// TODO
		String json = "";
		return json;
	}

	@JavascriptInterface
	public void showMedia(String request, String mediaType) {
		Intent it = MediaIntent.getMediaIntent(request, MediaIntentType.fromCode(mediaType));
		startActivity(it);
	}

	@JavascriptInterface
	public String loadDataFile(String dataName) {
		// TODO
		String json = "";
		return json;
	}

	@JavascriptInterface
	public String backMenu() {
		// TODO
		String json = "";
		return json;
	}

	@JavascriptInterface
	public void showMsg(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static String getInnerAssistantFileSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath() + assistantRootPath;
	}

	public void initView() {
		m_wv1 = (WebView) findViewById(R.id.wv1);
		m_wv1.getSettings().setJavaScriptEnabled(true);
		m_wv1.addJavascriptInterface(this, "JavascriptInterface");
		m_wv1.loadUrl("file:///android_asset/" + viewFilePath);
		m_wv1.getSettings().setUseWideViewPort(true);
		m_wv1.getSettings().setLoadWithOverviewMode(true);
		m_wv1.setWebChromeClient(new MyWebChromeClient());
		m_wv1.setWebViewClient(new WebViewClient() {
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
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initView();
	}

}
