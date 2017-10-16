package com.worldfootball.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.worldfootball.model.League;
import com.worldfootball.globalConstants.DBConst;

public class DBManager {

	private static SqlManager sSQLHelper;

	public void init(Context context) {
		sSQLHelper = SqlManager.getInstance(context);
	}

	public static void setLeague(League league) {
		if (league != null) {
			ContentValues data = new ContentValues();
			data.put(DBConst.ID, league.getID());
			data.put(DBConst.CAPTION, league.getCaption());
			data.put(DBConst.LEAGUE, league.getLeague());
			data.put(DBConst.YEAR, league.getYear());
			data.put(DBConst.CURRENT_MATCHDAY, league.getCurrentMatchday());
			data.put(DBConst.NUMBER_OF_MATCHDAYS, league.getNumberOfMatchdays());
			data.put(DBConst.NUMBER_OF_TEAMS, league.getNumberOfTeams());
			data.put(DBConst.NUMBER_OF_GAMES, league.getNumberOfGames());
			data.put(DBConst.LAST_UPDATED, league.getLastUpdated());

			Cursor cursor = sSQLHelper.getAll(
					DBConst.TABLE_LEAGUES,
					new String[] {DBConst.ID},
					new String[] {String.valueOf(league.getID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DBConst.TABLE_LEAGUES,
						data, new String[] {DBConst.ID},
						new String[] {String.valueOf(league.getID())});
			}
			else {
				sSQLHelper.insert(DBConst.TABLE_LEAGUES, data);
			}
			cursor.close();
		}
	}

	public static League getLeague(int id) {
		Cursor cursor = sSQLHelper.getAll(
				DBConst.TABLE_LEAGUES,
				new String[] {DBConst.ID},
				new String[] {String.valueOf(id)});
		League league = League.parse(cursor);
		cursor.close();
		return league;
	}

	public void clean() {
		if (sSQLHelper != null) {
			sSQLHelper.clean();
			sSQLHelper = null;
		}
	}

}
