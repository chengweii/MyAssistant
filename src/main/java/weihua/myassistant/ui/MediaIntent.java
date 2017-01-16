package weihua.myassistant.ui;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import weihua.myassistant.response.MediaType;
import weihua.myassistant.util.FileUtil;

public class MediaIntent {

	/**
	 * 获取媒体引用Intent
	 * 
	 * @param mediaLink
	 *            目标名称
	 * @param mediaType
	 *            媒体类型
	 * @return
	 */
	public static Intent getMediaIntent(String mediaLink, MediaType mediaType) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		if (mediaType == MediaType.IMAGE) {
			mediaLink = FileUtil.getInnerAssistantFileSDCardPath() + mediaLink;
			File file = new File(mediaLink);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "image/*");
		} else if (mediaType == MediaType.VIDEO) {
			mediaLink = FileUtil.getInnerAssistantFileSDCardPath() + mediaLink;
			File file = new File(mediaLink);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "video/*");
		} else if (mediaType == MediaType.AUDIO) {
			mediaLink = FileUtil.getInnerAssistantFileSDCardPath() + mediaLink;
			File file = new File(mediaLink);
			it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), "audio/*");
		} else if (mediaType == MediaType.URL) {
			it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(mediaLink));
		}
		return it;
	}

}
