package my.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Iterator;

public class DbHelper extends SQLiteOpenHelper {
    private DbConstants dbConst;

    protected DbHelper(Context context, String dbName, CursorFactory cursorFactory, int version) {
        super(context, dbName, cursorFactory, version);
    }

    public DbHelper(Context context, DbConstants dbConst) {
        super(context, dbConst.getDatabaseName(), null, dbConst.getDatabaseVersion());
        this.dbConst = dbConst;
    }

    public void onCreate(SQLiteDatabase db) {
        Iterator it = this.dbConst.getTables().iterator();
        while (it.hasNext()) {
            db.execSQL(((POJO) it.next()).getTableCreateSql());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Iterator it = this.dbConst.getTables().iterator();
        while (it.hasNext()) {
            db.execSQL("drop table if exists " + ((POJO) it.next()).getTableName());
        }
        onCreate(db);
    }
}
