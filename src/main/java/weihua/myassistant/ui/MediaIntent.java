package weihua.myassistant.ui;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import weihua.myassistant.util.FileUtil;

public class MediaIntent {

	/**
	 * 获取媒体引用Intent
	 * 
	 * @param request
	 *            目标名称
	 * @param mediaIntentType
	 *            媒体类型
	 * @return
	 */
	public static Intent getMediaIntent(String request, MediaIntentType mediaIntentType) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		if (mediaIntentType == MediaIntentType.IMAGE) {
			request = FileUtil.getInnerAssistantFileSDCardPath() + request;
			File file = new File(request);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "image/*");
		} else if (mediaIntentType == MediaIntentType.VIDEO) {
			request = FileUtil.getInnerAssistantFileSDCardPath() + request;
			File file = new File(request);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "video/*");
		} else if (mediaIntentType == MediaIntentType.AUDIO) {
			request = FileUtil.getInnerAssistantFileSDCardPath() + request;
			File file = new File(request);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "audio/*");
		} else if (mediaIntentType == MediaIntentType.URL) {
			it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(request));
		}
		return it;
	}

}
