package weihua.myassistant.util.dbhelper;

import java.util.Map;

import android.database.Cursor;

public interface RowProcessor {
	Map<String, Object> toMap(Cursor cs);
}
