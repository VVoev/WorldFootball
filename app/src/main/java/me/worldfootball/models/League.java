package me.worldfootball.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import me.worldfootball.database.constants.DbConstants;
import me.worldfootball.utils.L;

public class League {
    private int mID;
    private String mCaption;
    private String mLeague;
    private int mYear;
    private int mCurrentMatchday;
    private int mNumberOfMatchdays;
    private int mNumberOfTeams;
    private int mNumberOfGames;
    private long mLastUpdated;

    protected League() {}

    public League(
            int id,
            String caption,
            String league,
            int year,
            int currentMatchday,
            int numberOfMatchdays,
            int numberOfTeams,
            int numberOfGames,
            long lastUpdated) {
        this.mID = id;
        this.mCaption = caption;
        this.mLeague = league;
        this.mYear = year;
        this.mCurrentMatchday = currentMatchday;
        this.mNumberOfMatchdays = numberOfMatchdays;
        this.mNumberOfTeams = numberOfTeams;
        this.mNumberOfGames = numberOfGames;
        this.mLastUpdated = lastUpdated;
    }

    public static League parse(Cursor leagueCursor) {
        if (leagueCursor.moveToFirst()) {
            int col_id = leagueCursor.getColumnIndex(DbConstants.ID);
            int col_caption = leagueCursor.getColumnIndex(DbConstants.CAPTION);
            int col_league = leagueCursor.getColumnIndex(DbConstants.LEAGUE);
            int col_year = leagueCursor.getColumnIndex(DbConstants.YEAR);
            int col_currentMatchday = leagueCursor.getColumnIndex(DbConstants.CURRENT_MATCHDAY);
            int col_numberOfMatchdays = leagueCursor.getColumnIndex(DbConstants.NUMBER_OF_MATCHDAYS);
            int col_numberOfTeams = leagueCursor.getColumnIndex(DbConstants.NUMBER_OF_TEAMS);
            int col_numberOfGames = leagueCursor.getColumnIndex(DbConstants.NUMBER_OF_GAMES);
            int col_lastUpdated = leagueCursor.getColumnIndex(DbConstants.LAST_UPDATED);
            return new League(
                    leagueCursor.getInt(col_id),
                    leagueCursor.getString(col_caption),
                    leagueCursor.getString(col_league),
                    leagueCursor.getInt(col_year),
                    leagueCursor.getInt(col_currentMatchday),
                    leagueCursor.getInt(col_numberOfMatchdays),
                    leagueCursor.getInt(col_numberOfTeams),
                    leagueCursor.getInt(col_numberOfGames),
                    leagueCursor.getLong(col_lastUpdated));
        }
        return null;
    }

    public static League parse(JSONObject o) {
        League league = new League();
        try {
            if (!o.isNull("id")) league.mID = o.getInt("id");
            if (!o.isNull("caption")) league.mCaption = o.getString("caption");
            if (!o.isNull("league")) league.mLeague = o.getString("league");
            if (!o.isNull("year")) league.mYear = Integer.valueOf(o.getString("year"));
            if (!o.isNull("currentMatchday")) league.mCurrentMatchday = o.getInt("currentMatchday");
            if (!o.isNull("numberOfMatchdays")) league.mNumberOfMatchdays = o.getInt("numberOfMatchdays");
            if (!o.isNull("numberOfTeams")) league.mNumberOfTeams = o.getInt("numberOfTeams");
            if (!o.isNull("numberOfGames")) league.mNumberOfTeams = o.getInt("numberOfGames");
            if (!o.isNull("lastUpdated")) {
                String dateStr = o.getString("lastUpdated");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
                league.mLastUpdated = dateFormat.parse(dateStr).getTime();
            }
            return league;

        } catch (JSONException e) {
            L.e(League.class, e.toString());
            e.printStackTrace();
        } catch (ParseException e) {
            L.e(League.class, e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public int getID() {
        return mID;
    }

    public String getCaption() {
        return mCaption;
    }

    public String getLeague() {
        return mLeague;
    }

    public int getYear() {
        return mYear;
    }

    public int getCurrentMatchday() {
        return mCurrentMatchday;
    }

    public int getNumberOfMatchdays() {
        return mNumberOfMatchdays;
    }

    public int getNumberOfTeams() {
        return mNumberOfTeams;
    }

    public int getNumberOfGames() {
        return mNumberOfGames;
    }

    public long getLastUpdated() {
        return mLastUpdated;
    }
}
