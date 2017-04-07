package weihua.myassistant.util.dbhelper;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.weihua.util.DBUtil.DBHelper;
import com.weihua.util.ExceptionUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import weihua.myassistant.util.dbhelper.handler.MapHandler;
import weihua.myassistant.util.dbhelper.handler.MapListHandler;

public class MobileDBHelper extends SQLiteOpenHelper implements DBHelper {
	private static MobileDBHelper dBHelper = null;

	public MobileDBHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		if (dBHelper == null) {
			dBHelper = new MobileDBHelper(context, dbName, dbVersion);
		}
	}

	public void execSQL(String sql, Object[] bindArgs) {
		synchronized (dBHelper) {
			SQLiteDatabase database = dBHelper.getWritableDatabase();
			database.execSQL(sql, bindArgs);
		}
	}

	public Cursor rawQuery(String sql, String[] bindArgs) {
		synchronized (dBHelper) {
			SQLiteDatabase database = dBHelper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, bindArgs);
			return cursor;
		}
	}

	@Override
	public Map<String, Object> queryMap(Logger logger, String sql, Object... params) {
		synchronized (dBHelper) {
			Map<String, Object> map = null;
			try {
				SQLiteDatabase database = dBHelper.getReadableDatabase();
				Cursor cursor = database.rawQuery(sql, getStringArray(params));
				CursorHandler<Map<String, Object>> cursorHandler = new MapHandler();
				map = cursorHandler.handle(cursor);
			} catch (Exception e) {
				ExceptionUtil.propagate(logger, e);
			}
			return map;
		}
	}

	@Override
	public List<Map<String, Object>> queryMapList(Logger logger, String sql, Object... params) {
		synchronized (dBHelper) {
			List<Map<String, Object>> mapList = null;
			try {
				SQLiteDatabase database = dBHelper.getReadableDatabase();
				Cursor cursor = database.rawQuery(sql, getStringArray(params));
				CursorHandler<List<Map<String, Object>>> cursorHandler = new MapListHandler();
				mapList = cursorHandler.handle(cursor);
			} catch (Exception e) {
				ExceptionUtil.propagate(logger, e);
			}
			return mapList;
		}
	}

	@Override
	public int queryUpdate(Logger logger, String sql, Object... params) {
		synchronized (dBHelper) {
			try {
				SQLiteDatabase database = dBHelper.getWritableDatabase();
				database.execSQL(sql, params);
			} catch (Exception e) {
				ExceptionUtil.propagate(logger, e);
			}
			return 0;
		}
	}

	@Override
	public int[] queryBatch(Logger logger, String sql, Object[][] params) {
		synchronized (dBHelper) {
			try {
				SQLiteDatabase database = dBHelper.getWritableDatabase();
				database.beginTransaction();

				for (Object[] param : params) {
					database.execSQL(sql, param);
				}

				database.endTransaction();
			} catch (Exception e) {
				ExceptionUtil.propagate(logger, e);
			}
			return null;
		}
	}

	private String[] getStringArray(Object[] params) {
		String[] result = null;
		if (params.length > 0) {
			int len = params.length;
			result = new String[len];
			for (int i = 0; i < len; i++) {
				result[i] = String.valueOf(params[i]);
			}
		}
		return result;
	}

	public void close() {
		dBHelper.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
