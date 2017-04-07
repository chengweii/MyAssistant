package weihua.myassistant.util.dbhelper.handler;

import java.util.Map;

import android.database.Cursor;
import weihua.myassistant.util.dbhelper.BasicRowProcessor;
import weihua.myassistant.util.dbhelper.CursorHandler;
import weihua.myassistant.util.dbhelper.RowProcessor;

public class MapHandler implements CursorHandler<Map<String, Object>> {

    private final RowProcessor convert;

    public MapHandler() {
        this(BasicRowProcessor.ROW_PROCESSOR);
    }

    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    @Override
    public Map<String, Object> handle(Cursor cs) {
        return this.convert.toMap(cs);
    }

}
