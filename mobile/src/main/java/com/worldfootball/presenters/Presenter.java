package com.worldfootball.presenters;

import android.content.Context;
import android.os.Handler;

import com.worldfootball.data.DBManager;
import com.worldfootball.contracts.IOnResultListener;
import com.worldfootball.globalConstants.Constants;
import com.worldfootball.models.Fixture;
import com.worldfootball.models.Head2head;
import com.worldfootball.models.League;
import com.worldfootball.models.Scores;
import com.worldfootball.utils.L;
import com.worldfootball.webApi.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Presenter {

	private static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
	private static Presenter mInstance;
	private ThreadPoolExecutor sExecutor;
	private Handler mHandler;

	public static synchronized void init(Context context) {
		DBManager.init(context);
		if (mInstance == null) synchronized (Presenter.class) {
			if (mInstance == null) {
				mInstance = new Presenter();
				mInstance.mHandler = new Handler();
				mInstance.sExecutor = new ThreadPoolExecutor(
						1,
						CPU_NUMBER,
						Constants.KEEP_ALIVE,
						TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>());
			}
		}
	}

	public static synchronized Presenter getInstance() {
		return mInstance;
	}

	protected Presenter() {}

	public void getListOfLeagues(final Context context, final OnGetLeagues callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<League> dbList = DBManager.getLeaguesList();
				if (dbList != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbList);
					}
				});
				int year = Calendar.getInstance().get(Calendar.YEAR);
				String resultCurrent,
						resultLast = null;
				resultCurrent= Api.getLeagueByYear(context, year);
				if (resultCurrent != null) resultLast = Api.getLeagueByYear(context, year);
				if (resultCurrent != null && resultLast != null) {
					try {
						JSONArray currentArray = new JSONArray(resultCurrent);
						JSONArray lastArray = new JSONArray(resultLast);
						final ArrayList<League> list = League.parseArray(currentArray);
						list.addAll(League.parseArray(lastArray));
						if (!list.isEmpty()) {
							DBManager.setLeaguesList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_RESULT_EMPTY);
							}
						});

					} catch (JSONException e) {
						L.e(Presenter.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Constants.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getListOfFixtures(
			final Context context,
			final int soccerseasonId,
			final int matchday,
			final OnGetFixtures callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Fixture> dbList = DBManager.getFixturesListByMatchday(soccerseasonId, matchday);
				if (dbList != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbList);
					}
				});
				String result = Api.getFixturesByMatchday(context, soccerseasonId, matchday);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (list != null && !list.isEmpty()) {
							DBManager.setFixturesList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Presenter.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Constants.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getScores(
			final Context context,
			final int soccerSeasonId,
			final OnGetScores callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Scores> dbList = DBManager.getScoresList(soccerSeasonId);
				if (dbList != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbList);
					}
				});
				String result = Api.getScores(context, soccerSeasonId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("standing");
						final ArrayList<Scores> list = Scores.parseArray(soccerSeasonId, array);
						if (list != null && !list.isEmpty()) {
							DBManager.setScoresList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Presenter.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Constants.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getFixtureDetails(
			final Context context,
			final int fixtureId,
			final OnGetFixtureDetails callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final Head2head dbHead2head = DBManager.getHead2head(fixtureId);
				if (dbHead2head != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbHead2head);
					}
				});
				String result = Api.getFixtureDetailsById(context, fixtureId);
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result).getJSONObject("head2head");
						final Head2head head2head = Head2head.parse(fixtureId, object);
						if (head2head != null) {
							DBManager.setHead2head(dbHead2head);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(head2head);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Presenter.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Constants.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Constants.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public interface OnGetLeagues extends IOnResultListener<ArrayList<League>> {}

	public interface OnGetFixtures extends IOnResultListener<ArrayList<Fixture>> {}

	public interface OnGetFixtureDetails extends IOnResultListener<Head2head> {}

	public interface OnGetScores extends IOnResultListener<ArrayList<Scores>> {}

}
