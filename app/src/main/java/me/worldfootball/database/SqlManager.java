package me.worldfootball.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import me.worldfootball.database.constants.DbConstants;
import me.worldfootball.globalconstants.Constants;

public class SqlManager extends SQLiteOpenHelper {

    private static final String DB_NAME = Constants.DATABASE_NAME;
    private static final int DB_VERSION = 1;
    private static SqlManager sInstance;

    protected SqlManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SqlManager getInstance(Context context) {
        if (sInstance == null) synchronized (SqlManager.class) {
            if (sInstance == null) {
                sInstance = new SqlManager(context);
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder params1 = new StringBuilder()
                .append(DbConstants.TABLE_TEAMS).append(" INTEGER NOT NULL,")
                .append(DbConstants.TABLE_TEAMS).append(" INTEGER");

        createTable(db, DbConstants.TABLE_TEAMS, params1.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropData(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropData(db);
        onCreate(db);
    }

    private void createTable(SQLiteDatabase db, String tableName, String params) {
        StringBuilder query = new StringBuilder()
                .append("CREATE TABLE ").append(tableName)
                .append("(").append(params).append(");");
        db.execSQL(query.toString());
    }

    public void dropData(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_TEAMS);
    }

    public void insert(String tableName, ContentValues values) {
        getWritableDatabase().insert(tableName, null, values);
    }

    public void update(String tableName, ContentValues values, String[] where, String[] arguments) {
        StringBuilder sb = new StringBuilder();
        int len = where.length;
        sb.append(where[0]).append("=?");
        for (int i = 1; i < len; i++)
            sb.append(" AND ").append(where[i]).append("=?");
        getWritableDatabase().update(tableName, values, sb.toString(), arguments);
    }

    /**
     *
     * @param orderBy your column + <b>ASC</b> or <b>DESC</b>
     */
    public Cursor get(String tableName, String[] columns, String[] where,
                      String[] arguments, String groupBy, String having, String orderBy) {
        StringBuilder sb = null;
        if (where != null) {
            sb = new StringBuilder();
            int len = where.length;
            sb.append(where[0]).append("=?");
            for (int i = 1; i < len; i++)
                sb.append(" AND ").append(where[i]).append("=?");
        }
        String where_str = null;
        if (sb != null) where_str = sb.toString();
        //order [ASC|DESC]
        return getReadableDatabase()
                .query(tableName, columns, where_str, arguments, groupBy, having, orderBy);
    }

    public Cursor getAll(String tableName) {
        return get(tableName, null, null, null, null, null, null);
    }

    public Cursor getAll(String tableName, String[] where, String[] arguments) {
        return get(tableName, null, where, arguments, null, null, null);
    }

    /**
     * Get all columns and sort
     * @param orderBy column to order
     * @param reverse if <b>true</b> from large to small
     * @return Cursor
     */
    public Cursor getAll(
            String tableName,
            String[] where,
            String[] arguments,
            String orderBy,
            boolean reverse) {
        orderBy += reverse ? " DESC" : " ASC";
        return get(tableName, null, where, arguments, null, null, orderBy);
    }

    public void deleteAll(String tableName, String[] where, String[] argument) {
        StringBuilder sb = null;
        if (where != null) {
            sb = new StringBuilder();
            int len = where.length;
            sb.append(where[0]).append("=?");
            for (int i = 1; i < len; i++)
                sb.append(" AND ").append(where[i]).append("=?");
        }
        String where_str = null;
        if (sb != null) where_str = sb.toString();
        getWritableDatabase().delete(tableName, where_str, argument);
    }

    public void deleteAll(String tableName) {
        getWritableDatabase().delete(tableName, null, null);
    }


}
