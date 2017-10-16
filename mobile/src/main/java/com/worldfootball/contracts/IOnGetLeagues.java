package com.worldfootball.contracts;

import com.worldfootball.model.League;

import java.util.ArrayList;

public interface IOnGetLeagues extends IOnResult<ArrayList<League>> {
    void onError();
}
