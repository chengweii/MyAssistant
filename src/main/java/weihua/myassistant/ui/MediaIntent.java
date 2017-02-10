package weihua.myassistant.ui;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import weihua.myassistant.response.MediaType;
import weihua.myassistant.util.FileUtil;

public class MediaIntent {

	private static MediaPlayer mediaPlayer;

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

	public static MediaPlayer playMusic(Context context, MusicPlaySource musicPlaySource, String mediaLink) {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}

		if (musicPlaySource == MusicPlaySource.LOCAL) {
			mediaLink = FileUtil.getInnerAssistantFileSDCardPath() + mediaLink;
		}

		mediaPlayer = MediaPlayer.create(context, Uri.parse(mediaLink));
		mediaPlayer.start();

		return mediaPlayer;
	}

	public static enum MusicPlaySource {

		LOCAL("0", "本地"),

		WEB("1", "网络");

		private MusicPlaySource(String code, String value) {
			this.code = code;
			this.value = value;
		}

		private String code;
		private String value;

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static MusicPlaySource fromCode(String code) {
			for (MusicPlaySource entity : MusicPlaySource.values()) {
				if (entity.getCode().equals(code)) {
					return entity;
				}
			}
			return null;
		}
	}

}
