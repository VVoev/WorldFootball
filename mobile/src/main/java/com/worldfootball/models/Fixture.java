package com.worldfootball.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.worldfootball.globalConstants.DbConstants;
import com.worldfootball.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Fixture implements Parcelable {

    public static final int FINISHED = 0;
    public static final int TIMED = 1;
    public static final int INDEFINITE = 2;

    private int mID;
    private int mSoccerSeasonID;
    private long mDate;
    private int mStatus = INDEFINITE;
    private int mMatchday;
    private int mHomeTeamID;
    private int mAwayTeamID;
    private String mHomeTeamName;
    private String mAwayTeamName;
    private int mGoalsHomeTeam = -3;
    private int mGoalsAwayTeam = -3;

    protected Fixture() {
    }

    protected Fixture(Parcel in) {
        mID = in.readInt();
        mSoccerSeasonID = in.readInt();
        mDate = in.readLong();
        mStatus = in.readInt();
        mMatchday = in.readInt();
        mHomeTeamID = in.readInt();
        mAwayTeamID = in.readInt();
        mHomeTeamName = in.readString();
        mAwayTeamName = in.readString();
        mGoalsHomeTeam = in.readInt();
        mGoalsAwayTeam = in.readInt();
    }

    public static final Creator<Fixture> CREATOR = new Creator<Fixture>() {
        @Override
        public Fixture createFromParcel(Parcel in) {
            return new Fixture(in);
        }

        @Override
        public Fixture[] newArray(int size) {
            return new Fixture[size];
        }
    };

    @Nullable
    public static Fixture parse(Cursor fixtureCursor) {
        Fixture fixture = null;
        if (fixtureCursor.moveToFirst()) {
            int col_id = fixtureCursor.getColumnIndex(DbConstants.ID);
            int col_homeTeamId = fixtureCursor.getColumnIndex(DbConstants.HOME_TEAM_ID);
            int col_soccerSeasonId = fixtureCursor.getColumnIndex(DbConstants.SOCCER_SEASON_ID);
            int col_date = fixtureCursor.getColumnIndex(DbConstants.DATE);
            int col_awayTeamId = fixtureCursor.getColumnIndex(DbConstants.AWAY_TEAM_ID);
            int col_awayTeamName = fixtureCursor.getColumnIndex(DbConstants.AWAY_TEAM_NAME);
            int col_goalsHomeTeam = fixtureCursor.getColumnIndex(DbConstants.GOALS_HOME_TEAM);
            int col_status = fixtureCursor.getColumnIndex(DbConstants.STATUS);
            int col_matchday = fixtureCursor.getColumnIndex(DbConstants.MATCHDAY);
            int col_homeTeamName = fixtureCursor.getColumnIndex(DbConstants.HOME_TEAM_NAME);
            int col_goalsAwayTeam = fixtureCursor.getColumnIndex(DbConstants.GOALS_AWAY_TEAM);
            fixture = new Fixture();
            fixture.mID = fixtureCursor.getInt(col_id);
            fixture.mSoccerSeasonID = fixtureCursor.getInt(col_soccerSeasonId);
            fixture.mDate = fixtureCursor.getLong(col_date);
            fixture.mStatus = fixtureCursor.getInt(col_status);
            fixture.mMatchday = fixtureCursor.getInt(col_matchday);
            fixture.mHomeTeamID = fixtureCursor.getInt(col_homeTeamId);
            fixture.mHomeTeamName = fixtureCursor.getString(col_homeTeamName);
            fixture.mAwayTeamID = fixtureCursor.getInt(col_awayTeamId);
            fixture.mAwayTeamName = fixtureCursor.getString(col_awayTeamName);
            fixture.mGoalsHomeTeam = fixtureCursor.getInt(col_goalsHomeTeam);
            fixture.mGoalsAwayTeam = fixtureCursor.getInt(col_goalsAwayTeam);
        }
        return fixture;
    }

    @Nullable
    public static ArrayList<Fixture> parseArray(Cursor fixturesCursor) {
        if (fixturesCursor.moveToFirst()) {
            int col_id = fixturesCursor.getColumnIndex(DbConstants.ID);
            int col_soccerSeasonId = fixturesCursor.getColumnIndex(DbConstants.SOCCER_SEASON_ID);
            int col_date = fixturesCursor.getColumnIndex(DbConstants.DATE);
            int col_status = fixturesCursor.getColumnIndex(DbConstants.STATUS);
            int col_matchday = fixturesCursor.getColumnIndex(DbConstants.MATCHDAY);
            int col_homeTeamId = fixturesCursor.getColumnIndex(DbConstants.HOME_TEAM_ID);
            int col_homeTeamName = fixturesCursor.getColumnIndex(DbConstants.HOME_TEAM_NAME);
            int col_awayTeamId = fixturesCursor.getColumnIndex(DbConstants.AWAY_TEAM_ID);
            int col_awayTeamName = fixturesCursor.getColumnIndex(DbConstants.AWAY_TEAM_NAME);
            int col_goalsHomeTeam = fixturesCursor.getColumnIndex(DbConstants.GOALS_HOME_TEAM);
            int col_goalsAwayTeam = fixturesCursor.getColumnIndex(DbConstants.GOALS_AWAY_TEAM);
            ArrayList<Fixture> list = new ArrayList<>();
            do {
                Fixture fixture = new Fixture();
                fixture.mID = fixturesCursor.getInt(col_id);
                fixture.mSoccerSeasonID = fixturesCursor.getInt(col_soccerSeasonId);
                fixture.mDate = fixturesCursor.getLong(col_date);
                fixture.mStatus = fixturesCursor.getInt(col_status);
                fixture.mMatchday = fixturesCursor.getInt(col_matchday);
                fixture.mHomeTeamID = fixturesCursor.getInt(col_homeTeamId);
                fixture.mHomeTeamName = fixturesCursor.getString(col_homeTeamName);
                fixture.mAwayTeamID = fixturesCursor.getInt(col_awayTeamId);
                fixture.mAwayTeamName = fixturesCursor.getString(col_awayTeamName);
                fixture.mGoalsHomeTeam = fixturesCursor.getInt(col_goalsHomeTeam);
                fixture.mGoalsAwayTeam = fixturesCursor.getInt(col_goalsAwayTeam);
                list.add(fixture);
            } while (fixturesCursor.moveToNext());
            return list;
        } else return null;
    }

    @Nullable
    public static Fixture parse(JSONObject o) {
        if (o != null) {
            Fixture fixture = new Fixture();
            fixture.mID = o.optInt("id");
            fixture.mSoccerSeasonID = o.optInt("soccerseasonId");
            String date = o.optString("date", null);
            if (date != null) {
                try {
                    fixture.mDate = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'",
                            Locale.getDefault())
                            .parse(date)
                            .getTime();
                } catch (ParseException e) {
                    L.e(Fixture.class, e.toString());
                }
            }
            String status = o.optString("status", null);
            if (status != null) {
                if (status.equals("FINISHED")) fixture.mStatus = FINISHED;
                else if (status.equals("TIMED")) fixture.mStatus = TIMED;
            }
            fixture.mMatchday = o.optInt("matchday");
            fixture.mHomeTeamID = o.optInt("homeTeamId");
            fixture.mHomeTeamName = o.optString("homeTeamName");
            fixture.mAwayTeamID = o.optInt("awayTeamId");
            fixture.mAwayTeamName = o.optString("awayTeamName");
            JSONObject result = o.optJSONObject("result");
            if (result != null) {
                fixture.mGoalsHomeTeam = result.optInt("goalsHomeTeam", -5);
                fixture.mGoalsAwayTeam = result.optInt("goalsAwayTeam", -5);
            }
            return fixture;
        } else return null;
    }

    @Nullable
    public static ArrayList<Fixture> parseArray(JSONArray array) {
        if (array != null) {
            ArrayList<Fixture> list = new ArrayList<>();
            int count = array.length();
            for (int i = 0; i < count; i++) {
                try {
                    list.add(Fixture.parse(array.getJSONObject(i)));
                } catch (JSONException e) {
                    L.e(Fixture.class, e.toString());
                }
            }
            return list;
        } else return null;
    }

    public int getID() {
        return mID;
    }

    public int getSoccerSeasonID() {
        return mSoccerSeasonID;
    }

    public long getDate() {
        return mDate;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getMatchday() {
        return mMatchday;
    }

    public int getHomeTeamID() {
        return mHomeTeamID;
    }

    public int getAwayTeamID() {
        return mAwayTeamID;
    }

    public String getHomeTeamName() {
        return mHomeTeamName;
    }

    public String getAwayTeamName() {
        return mAwayTeamName;
    }

    public Integer getGoalsHomeTeam() {
        return mGoalsHomeTeam;
    }

    public Integer getGoalsAwayTeam() {
        return mGoalsAwayTeam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mID);
        dest.writeInt(mSoccerSeasonID);
        dest.writeLong(mDate);
        dest.writeInt(mStatus);
        dest.writeInt(mMatchday);
        dest.writeInt(mHomeTeamID);
        dest.writeInt(mAwayTeamID);
        dest.writeString(mHomeTeamName);
        dest.writeString(mAwayTeamName);
        dest.writeInt(mGoalsHomeTeam);
        dest.writeInt(mGoalsAwayTeam);
    }
}
