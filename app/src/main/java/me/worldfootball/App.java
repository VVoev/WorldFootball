package me.worldfootball;


import android.app.Application;

import me.worldfootball.presenters.Presenter;

public class App extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        Presenter.init(getApplicationContext());
    }
}
