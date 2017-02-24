package weihua.myassistant.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import weihua.myassistant.common.Constants;
import weihua.myassistant.util.FileUtil;

public class ImageUtil {

	private static Logger loger = Logger.getLogger(ImageUtil.class);

	public static Bitmap getBitmap(String path) throws Exception {
		String originPath = path;
		if (path != null && !path.startsWith("http")) {
			if (FileUtil.isFileExists(FileUtil.getInnerAssistantFileSDCardPath() + path)) {
				Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getInnerAssistantFileSDCardPath() + path);
				return bitmap;
			} else {
				path = Constants.WEB_SOURCE_ROOT_PATH + path;
			}
		}

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			saveBitmap(bitmap, originPath);
			return bitmap;
		} else {
			loger.info("图片加载失败：" + path);
		}

		return null;
	}

	public static Bitmap fillet(Bitmap bitmap, int roundPx) throws Exception {
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();

		Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(paintingBoard);
		canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		final RectF rectF = new RectF(0, 0, width, height);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		final Rect src = new Rect(0, 0, width, height);
		final Rect dst = src;
		canvas.drawBitmap(bitmap, src, dst, paint);
		return paintingBoard;
	}

	public static void saveBitmap(Bitmap bitmap, String path) throws Exception {
		String filePath = FileUtil.getInnerAssistantFileSDCardPath() + path;
		File file = new File(filePath);
		FileOutputStream out;
		out = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.flush();
		out.close();
	}

}
