package me.worldfootball.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import me.worldfootball.database.constants.DbConstants;
import me.worldfootball.models.League;

public class DbManager {
    private static SqlManager sSQLHelper;

    public void init(Context context) {
        sSQLHelper = SqlManager.getInstance(context);
    }

    public static void setLeague(League league) {
        if (league != null) {
            ContentValues data = new ContentValues();
            data.put(DbConstants.ID, league.getID());
            data.put(DbConstants.CAPTION, league.getCaption());
            data.put(DbConstants.LEAGUE, league.getLeague());
            data.put(DbConstants.YEAR, league.getYear());
            data.put(DbConstants.CURRENT_MATCHDAY, league.getCurrentMatchday());
            data.put(DbConstants.NUMBER_OF_MATCHDAYS, league.getNumberOfMatchdays());
            data.put(DbConstants.NUMBER_OF_TEAMS, league.getNumberOfTeams());
            data.put(DbConstants.NUMBER_OF_GAMES, league.getNumberOfGames());
            data.put(DbConstants.LAST_UPDATED, league.getLastUpdated());

            Cursor cursor = sSQLHelper.getAll(
                    DbConstants.TABLE_LEAGUES,
                    new String[]{DbConstants.ID},
                    new String[]{String.valueOf(league.getID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DbConstants.TABLE_LEAGUES,
                        data, new String[]{DbConstants.ID},
                        new String[]{String.valueOf(league.getID())});
            } else {
                sSQLHelper.insert(DbConstants.TABLE_LEAGUES, data);
            }
            cursor.close();
        }
    }

    public static League getLeague(int id) {
        Cursor cursor = sSQLHelper.getAll(
                DbConstants.TABLE_LEAGUES,
                new String[]{DbConstants.ID},
                new String[]{String.valueOf(id)});
        League league = League.parse(cursor);
        cursor.close();
        return league;
    }
}
