package me.worldfootball.footballApi;

import android.content.Context;

import me.worldfootball.R;
import me.worldfootball.connections.Connection;

public class Api {
    public static String getLeaguesList(Context context, int year) {
        Connection connection = getConnection(context);
        return connection.request(getBaseUri() + "soccerseasons?season=" + String.valueOf(year));
    }

    private static Connection getConnection(Context context) {
        return new Connection.Builder()
                .putHeader("X-Auth-Token", context.getString(R.string.api_token))
                .putHeader("X-Response-Control", context.getString(R.string.api_response_control_mini))
                .build();
    }

    private static String getBaseUri() {
        return "http://api.football-data.org/v1/";
    }
}
