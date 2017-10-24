package com.worldfootball;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.worldfootball.configurations.Configs;
import com.worldfootball.presenters.Presenter;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Presenter.init(getApplicationContext());
		ImageLoader.getInstance().init(Configs.getImageLoaderConfig(getApplicationContext()));
	}

}
