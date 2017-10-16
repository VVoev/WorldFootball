package com.worldfootball;

import android.app.Application;

import com.worldfootball.presenters.Presenter;

public class Engine extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Presenter.init(getApplicationContext());
	}

}
