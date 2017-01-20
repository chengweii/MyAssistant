package weihua.myassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;
import weihua.myassistant.response.MediaType;
import weihua.myassistant.ui.CustomerWebChromeClient;
import weihua.myassistant.ui.MediaIntent;
import weihua.myassistant.util.AssistantDataLoadUtil;
import weihua.myassistant.util.ExceptionUtil;
import weihua.myassistant.util.FileUtil;

public class MainActivity extends Activity {
	private WebView m_wv1;
	private static String viewFilePath = "index.html";
	private Context assistantContext;

	@JavascriptInterface
	public String getResponse(String request, String requestType) {
		String msg = "";
		try {
			msg = assistantContext.getResponse(request, RequestType.fromCode(requestType));
		} catch (Exception e) {
			msg = ExceptionUtil.getStackTrace(e);
		}
		return msg;
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

	public void initView() {
		m_wv1 = (WebView) findViewById(R.id.wv1);
		m_wv1.getSettings().setJavaScriptEnabled(true);
		m_wv1.addJavascriptInterface(this, "mainActivity");
		m_wv1.loadUrl("file:///android_asset/" + viewFilePath);
		m_wv1.getSettings().setUseWideViewPort(true);
		m_wv1.getSettings().setLoadWithOverviewMode(true);
		m_wv1.setWebChromeClient(new CustomerWebChromeClient());
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
		setContentView(R.layout.main);

		FileUtil.assistantRootPath = Environment.getExternalStorageDirectory().getPath() + "/assistant/";

		assistantContext = new Context();

		initView();
	}

}
