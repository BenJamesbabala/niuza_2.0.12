package my.base.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbProvider {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public static DbProvider open(Context context, DbConstants dbConst) {
        DbProvider dbProvider = new DbProvider();
        dbProvider.dbHelper = new DbHelper(context, dbConst);
        dbProvider.db = dbProvider.dbHelper.getWritableDatabase();
        return dbProvider;
    }

    public void insert(POJO pojo) {
        ContentValues newValues = new ContentValues();
        for (String columnName : pojo.getAllColumns()) {
            newValues.put(columnName, pojo.getColumnValue(columnName));
        }
        this.db.insert(pojo.getTableName(), null, newValues);
    }

    public void update(POJO pojo) {
        ContentValues updateValues = new ContentValues();
        for (String columnName : pojo.getSelectColumns()) {
            updateValues.put(columnName, pojo.getColumnValue(columnName));
        }
        this.db.update(pojo.getTableName(), updateValues, "_id=" + pojo.getRowId(), null);
    }

    public void delete(POJO pojo) {
        this.db.delete(pojo.getTableName(), "_id=" + pojo.getRowId(), null);
    }

    public Cursor fetchAll(POJO pojo) {
        return this.db.query(pojo.getTableName(), pojo.getSelectColumns(), null, null, pojo.getGroupBy(), pojo.getHaving(), pojo.getOrderBy(), null);
    }

    public Cursor fetch(POJO pojo) {
        return this.db.query(pojo.getTableName(), pojo.getSelectColumns(), pojo.getSelection(), pojo.getSelectionArgs(), pojo.getGroupBy(), pojo.getHaving(), pojo.getOrderBy(), pojo.getLimit());
    }

    public void close() {
        this.db.close();
    }
}
