package com.worldfootball.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.worldfootball.globalConstants.DBConst;

public class SqlManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "openarena.ApplicationDB";
	private static final int DB_VERSION = 1;

	private static volatile SqlManager sInstance;

	public static synchronized SqlManager getInstance(Context context) {
		if (sInstance == null) synchronized (SqlManager.class) {
			if (sInstance == null) sInstance = new SqlManager(context);
		}
		return sInstance;
	}

	protected SqlManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder params1 = new StringBuilder()
				.append(DBConst.ID).append(" INTEGER NOT NULL,")
				.append(DBConst.CAPTION).append(" TEXT,")
				.append(DBConst.LEAGUE).append(" TEXT,")
				.append(DBConst.YEAR).append(" INTEGER,")
				.append(DBConst.CURRENT_MATCHDAY).append(" INTEGER,")
				.append(DBConst.NUMBER_OF_MATCHDAYS).append(" INTEGER,")
				.append(DBConst.NUMBER_OF_TEAMS).append(" INTEGER,")
				.append(DBConst.NUMBER_OF_GAMES).append(" INTEGER,")
				.append(DBConst.LAST_UPDATED).append(" INTEGER");

		createTable(db, DBConst.TABLE_LEAGUES, params1.toString());
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

	public void dropData(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_TEAMS);
	}

	public void clean() {
		if (sInstance != null) sInstance = null;
	}

}
