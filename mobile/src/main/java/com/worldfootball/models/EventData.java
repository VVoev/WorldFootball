package com.worldfootball.models;

public class EventData {

	private int mCode;
	private int mID;
	private League mLeague;
	private Fixture mFixture;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public int getID() {
		return mID;
	}

	public League getLeague() {
		return mLeague;
	}

	public Fixture getFixture() {
		return mFixture;
	}

	public EventData setID(int id) {
		mID = id;
		return this;
	}

	public EventData setLeague(League league) {
		mLeague = league;
		return this;
	}

	public EventData setFixture(Fixture fixture) {
		mFixture = fixture;
		return this;
	}
}
