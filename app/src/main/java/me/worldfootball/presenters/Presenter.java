package me.worldfootball.presenters;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.worldfootball.contracts.IOnResult;
import me.worldfootball.footballApi.Api;
import me.worldfootball.globalconstants.Constants;
import me.worldfootball.models.League;
import me.worldfootball.models.PreferencesManager;
import me.worldfootball.utils.L;

public class Presenter {
    private static final int NUMBERS_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor sExecutor;
    private static Handler sHandler;
    private static Context sContext;

    public static void init(Context context) {
        if (Presenter.sContext == null) Presenter.sContext = context;
        if (sHandler == null) sHandler = new Handler();
        if (sExecutor == null || sExecutor.isShutdown()) {
            sExecutor = new ThreadPoolExecutor(
                    1,
                    NUMBERS_OF_CORES,
                    5000,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
    }

    public static void getListOfLeagues(final Context context, final OnGetLeagues callback) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                String resultCurrent = Api.getLeaguesList(context, year);
                String resultLast = Api.getLeaguesList(context, year - 1);
                if (resultCurrent != null && resultLast != null) {
                    ArrayList<League> list = null;
                    try {
                        JSONArray currentArray = new JSONArray(resultCurrent);
                        JSONArray lastArray = new JSONArray(resultLast);
                        list = parseLeagues(currentArray);
                        list.addAll(parseLeagues(lastArray));

                    } catch (JSONException e) {
                        L.e(Presenter.class, e.toString());
                        e.printStackTrace();
                    }
                    if (list != null) {
                        final ArrayList<League> finalList = list;
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(finalList);
                            }
                        });
                    }
                    else {
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError();
                            }
                        });
                    }
                }
                else sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        });
    }
    public static boolean isFirstEnter() {
        return PreferencesManager.getInstance(sContext).getBoolean(Constants.PREF_FIRST_ENTER, true);
    }

    @NonNull
    private static ArrayList<League> parseLeagues(JSONArray array) {
        ArrayList<League> list = new ArrayList<>();
        if (array != null) {
            int count = array.length();
            for (int i = 0; i < count; i++) {
                try {
                    list.add(League.parse(array.getJSONObject(i)));
                } catch (JSONException e) {
                    L.e(Presenter.class, e.toString());
                    e.printStackTrace();
                }
            }
            return list;
        }
        else return list;
    }

    public interface OnGetLeagues extends IOnResult<ArrayList<League>> {
        void onError();
    }
}
