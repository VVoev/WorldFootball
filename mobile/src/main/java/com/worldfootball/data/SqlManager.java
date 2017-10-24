package com.worldfootball.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.worldfootball.globalConstants.DbConstants;

public class SqlManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "WorldFootball";
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
		String leagues = DbConstants.ID + " INTEGER NOT NULL," +
				DbConstants.CAPTION + " TEXT," +
				DbConstants.LEAGUE + " TEXT," +
				DbConstants.YEAR + " INTEGER," +
				DbConstants.CURRENT_MATCHDAY + " INTEGER," +
				DbConstants.NUMBER_OF_MATCHDAYS + " INTEGER," +
				DbConstants.NUMBER_OF_TEAMS + " INTEGER," +
				DbConstants.NUMBER_OF_GAMES + " INTEGER," +
				DbConstants.LAST_UPDATED + " INTEGER";

		String fixtures = DbConstants.ID + " INTEGER NOT NULL," +
				DbConstants.SOCCER_SEASON_ID + " INTEGER," +
				DbConstants.DATE + " INTEGER," +
				DbConstants.STATUS + " INTEGER," +
				DbConstants.MATCHDAY + " INTEGER," +
				DbConstants.HOME_TEAM_ID + " INTEGER NOT NULL," +
				DbConstants.HOME_TEAM_NAME + " TEXT," +
				DbConstants.AWAY_TEAM_ID + " INTEGER NOT NULL," +
				DbConstants.AWAY_TEAM_NAME + " TEXT," +
				DbConstants.GOALS_HOME_TEAM + " INTEGER," +
				DbConstants.GOALS_AWAY_TEAM + " INTEGER";

		String head2head = DbConstants.FIXTURE_ID + " INTEGER NOT NULL," +
				DbConstants.COUNT + " INTEGER," +
				DbConstants.TIME_FRAME_START + " INTEGER," +
				DbConstants.TIME_FRAME_END + " INTEGER," +
				DbConstants.HOME_TEAM_WINS + " INTEGER," +
				DbConstants.AWAY_TEAM_WINS + " INTEGER," +
				DbConstants.DRAWS + " INTEGER";

		String scores = DbConstants.SOCCER_SEASON_ID + " INTEGER NOT NULL," +
				DbConstants.RANK + " INTEGER," +
				DbConstants.TEAM + " TEXT," +
				DbConstants.TEAM_ID + " INTEGER," +
				DbConstants.PLAYED_GAMES + " INTEGER," +
				DbConstants.CREST_URI + " TEXT," +
				DbConstants.POINTS + " INTEGER," +
				DbConstants.GOALS + " INTEGER," +
				DbConstants.GOAL_AGAINST + " INTEGER," +
				DbConstants.GOAL_DIFFERENCE + " INTEGER";

		innitializeTable(db, DbConstants.TABLE_LEAGUES, leagues);
		innitializeTable(db, DbConstants.TABLE_FIXTURES, fixtures);
		innitializeTable(db, DbConstants.TABLE_HEAD2HEAD, head2head);
		innitializeTable(db, DbConstants.TABLE_SCORES, scores);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_LEAGUES);
		db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_FIXTURES);
		db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_HEAD2HEAD);
		db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_SCORES);
	}

	private void innitializeTable(SQLiteDatabase db, String tableName, String params) {
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

	/*
	Order columns ASC OR DSC
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

	/*
	Get all columns sorted
	 */
	public Cursor getAll(
			String tableName,
			String[] where,
			String[] arguments,
			String orderBy,
			boolean beginningAtLarge) {
		orderBy += beginningAtLarge ? " DESC" : " ASC";
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
