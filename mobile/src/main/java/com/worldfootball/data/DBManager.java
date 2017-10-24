package com.worldfootball.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.worldfootball.models.Fixture;
import com.worldfootball.models.Head2head;
import com.worldfootball.models.League;
import com.worldfootball.models.Scores;
import com.worldfootball.globalConstants.DbConstants;

import java.util.ArrayList;

public class DBManager {

	private static SqlManager sSQLHelper;

	public static void init(Context context) {
		sSQLHelper = SqlManager.getInstance(context);
	}

	@Nullable
	public static ArrayList<League> getLeaguesList() {
		Cursor cursor = sSQLHelper.getAll(DbConstants.TABLE_LEAGUES);
		ArrayList<League> list = League.parseArray(cursor);
		if (cursor != null) cursor.close();
		return list;
	}

	@Nullable
	public static ArrayList<Fixture> getFixturesListByMatchday(int soccerSeasonID, int matchDay) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_FIXTURES,
				new String[] {DbConstants.SOCCER_SEASON_ID, DbConstants.MATCHDAY},
				new String[] {String.valueOf(soccerSeasonID), String.valueOf(matchDay)}, DbConstants.DATE, false);
		ArrayList<Fixture> list = Fixture.parseArray(cursor);
		if (cursor != null) cursor.close();
		return list;
	}

	@Nullable
	public static ArrayList<Fixture> getFixturesList(int soccerseasonId) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_FIXTURES,
				new String[] {DbConstants.SOCCER_SEASON_ID},
				new String[] {String.valueOf(soccerseasonId)}, DbConstants.DATE, true);
		ArrayList<Fixture> list = Fixture.parseArray(cursor);
		if (cursor != null) cursor.close();
		return list;
	}

	@Nullable
	public static ArrayList<Scores> getScoresList(int soccerseasonId) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_SCORES,
				new String[] {DbConstants.SOCCER_SEASON_ID},
				new String[] {String.valueOf(soccerseasonId)}, DbConstants.POINTS, true);
		ArrayList<Scores> list = Scores.parseArray(cursor);
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public static League getLeague(int id) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_LEAGUES,
				new String[] {DbConstants.ID},
				new String[] {String.valueOf(id)});
		League league = League.parse(cursor);
		if (cursor != null) cursor.close();
		return league;
	}

	public static Fixture getFixture(int id) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_FIXTURES,
				new String[] {DbConstants.ID},
				new String[] {String.valueOf(id)});
		Fixture fixture = Fixture.parse(cursor);
		if (cursor != null) cursor.close();
		return fixture;
	}

	public static Head2head getHead2head(int fixture_id) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_HEAD2HEAD,
				new String[] {DbConstants.FIXTURE_ID},
				new String[] {String.valueOf(fixture_id)});
		Head2head head2head = Head2head.parse(cursor);
		if (cursor != null) cursor.close();
		return head2head;
	}

	public static Scores getScores(int soccerSeasonId) {
		Cursor cursor = sSQLHelper.getAll(
				DbConstants.TABLE_SCORES,
				new String[] {DbConstants.SOCCER_SEASON_ID},
				new String[] {String.valueOf(soccerSeasonId)});
		Scores scores = Scores.parse(cursor);
		if (cursor != null) cursor.close();
		return scores;
	}

	public static void setLeaguesList(ArrayList<League> list) {
		if (list != null && !list.isEmpty()) {
			for (League league : list) {
				setLeague(league);
			}
		}
	}

	public static void setFixturesList(ArrayList<Fixture> list) {
		if (list != null && !list.isEmpty()) {
			for (Fixture fixture : list) {
				setFixture(fixture);
			}
		}
	}

	public static void setScoresList(ArrayList<Scores> list) {
		if (list != null && !list.isEmpty()) {
			for (Scores scores : list) {
				setScores(scores);
			}
		}
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
					new String[] {DbConstants.ID},
					new String[] {String.valueOf(league.getID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DbConstants.TABLE_LEAGUES,
						data, new String[] {DbConstants.ID},
						new String[] {String.valueOf(league.getID())});
			}
			else {
				sSQLHelper.insert(DbConstants.TABLE_LEAGUES, data);
			}
			cursor.close();
		}
	}

	public static void setFixture(Fixture fixture) {
		if (fixture != null) {
			ContentValues data = new ContentValues();
			data.put(DbConstants.ID, fixture.getID());
			data.put(DbConstants.SOCCER_SEASON_ID, fixture.getSoccerSeasonID());
			data.put(DbConstants.DATE, fixture.getDate());
			data.put(DbConstants.STATUS, fixture.getStatus());
			data.put(DbConstants.MATCHDAY, fixture.getMatchday());
			data.put(DbConstants.HOME_TEAM_ID, fixture.getHomeTeamID());
			data.put(DbConstants.HOME_TEAM_NAME, fixture.getHomeTeamName());
			data.put(DbConstants.AWAY_TEAM_ID, fixture.getAwayTeamID());
			data.put(DbConstants.AWAY_TEAM_NAME, fixture.getAwayTeamName());
			data.put(DbConstants.GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
			data.put(DbConstants.GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

			Cursor cursor = sSQLHelper.getAll(
					DbConstants.TABLE_FIXTURES,
					new String[] {DbConstants.ID},
					new String[] {String.valueOf(fixture.getID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DbConstants.TABLE_FIXTURES,
						data, new String[] {DbConstants.ID},
						new String[] {String.valueOf(fixture.getID())});
			}
			else {
				sSQLHelper.insert(DbConstants.TABLE_FIXTURES, data);
			}
			cursor.close();
		}
	}

	public static void setHead2head(Head2head head2head) {
		if (head2head != null) {
			ContentValues data = new ContentValues();
			data.put(DbConstants.FIXTURE_ID, head2head.getFixtureID());
			data.put(DbConstants.COUNT, head2head.getCount());
			data.put(DbConstants.TIME_FRAME_START, head2head.getTimeFrameStart());
			data.put(DbConstants.TIME_FRAME_END, head2head.getTimeFrameEnd());
			data.put(DbConstants.HOME_TEAM_WINS, head2head.getHomeTeamWins());
			data.put(DbConstants.AWAY_TEAM_WINS, head2head.getAwayTeamWins());
			data.put(DbConstants.DRAWS, head2head.getDraws());

			Cursor cursor = sSQLHelper.getAll(
					DbConstants.TABLE_HEAD2HEAD,
					new String[] {DbConstants.FIXTURE_ID},
					new String[] {String.valueOf(head2head.getFixtureID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DbConstants.TABLE_HEAD2HEAD,
						data, new String[] {DbConstants.FIXTURE_ID},
						new String[] {String.valueOf(head2head.getFixtureID())});
			}
			else {
				sSQLHelper.insert(DbConstants.TABLE_HEAD2HEAD, data);
			}
			cursor.close();
		}
	}

	public static void setScores(Scores scores) {
		if (scores != null) {
			ContentValues data = new ContentValues();
			data.put(DbConstants.SOCCER_SEASON_ID, scores.getSoccerSeasonID());
			data.put(DbConstants.RANK, scores.getRank());
			data.put(DbConstants.TEAM, scores.getTeam());
			data.put(DbConstants.TEAM_ID, scores.getTeamId());
			data.put(DbConstants.PLAYED_GAMES, scores.getPlayedGames());
			data.put(DbConstants.CREST_URI, scores.getCrestURI());
			data.put(DbConstants.POINTS, scores.getPoints());
			data.put(DbConstants.GOALS, scores.getGoals());
			data.put(DbConstants.GOAL_AGAINST, scores.getGoalAgainst());
			data.put(DbConstants.GOAL_DIFFERENCE, scores.getGoalDifference());

			Cursor cursor = sSQLHelper.getAll(
					DbConstants.TABLE_SCORES,
					new String[] {DbConstants.SOCCER_SEASON_ID, DbConstants.TEAM_ID},
					new String[] {String.valueOf(scores.getSoccerSeasonID()), String.valueOf(scores.getTeamId())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DbConstants.TABLE_SCORES,
						data, new String[] {DbConstants.SOCCER_SEASON_ID, DbConstants.TEAM_ID},
						new String[] {String.valueOf(scores.getSoccerSeasonID()), String.valueOf(scores.getTeamId())});
			}
			else {
				sSQLHelper.insert(DbConstants.TABLE_SCORES, data);
			}
			cursor.close();
		}
	}

}
