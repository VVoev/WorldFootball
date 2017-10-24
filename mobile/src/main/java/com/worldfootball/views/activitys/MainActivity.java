package com.worldfootball.views.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.worldfootball.R;
import com.worldfootball.contracts.IEventListener;
import com.worldfootball.models.EventData;
import com.worldfootball.globalConstants.Constants;
import com.worldfootball.utils.L;
import com.worldfootball.views.fragments.FragmentFixtureInformation;
import com.worldfootball.views.fragments.FragmentFixtures;
import com.worldfootball.views.fragments.FragmentLeagues;
import com.worldfootball.views.fragments.FragmentScores;

public class MainActivity extends AppCompatActivity implements IEventListener {

	private Toolbar mToolbar;
	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		mFragmentManager = getFragmentManager();
		showContent();
	}

	@Override
	public void onBackPressed() {
		if (mFragmentManager.getBackStackEntryCount() > 0) {
			mFragmentManager.popBackStack();
		}
		else{
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToolbar != null) {
			mToolbar = null;
		}
		if (mFragmentManager != null){
			mFragmentManager = null;
		}
	}

	@Override
	public void onEvent(EventData event) {
		int code = event.gecCode();
		switch (code) {
			case Constants.EVENT_CODE_SELECT_LEAGUE:
				if (mFragmentManager.findFragmentByTag(FragmentFixtures.TAG) == null) {
					Bundle data = new Bundle();
					data.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentFixtures.getInstance(data))
							.addToBackStack(FragmentFixtures.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Constants.EVENT_CODE_SELECT_FIXTURE:
				if (mFragmentManager.findFragmentByTag(FragmentFixtureInformation.TAG) == null) {
					Bundle data = new Bundle();
					data.putParcelable("fixture", event.getFixture());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentFixtureInformation.getInstance(data))
							.addToBackStack(FragmentFixtureInformation.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Constants.EVENT_CODE_SHOW_SCORES_TABLE:
				if (mFragmentManager.findFragmentByTag(FragmentScores.TAG) == null) {
					Bundle data = new Bundle();
					data.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentScores.getInstance(data))
							.addToBackStack(FragmentScores.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			default:
				L.e(MainActivity.class, "default value");
		}
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
	}

	private void showContent() {
		if (mFragmentManager.findFragmentByTag(FragmentLeagues.TAG) == null) {
			mFragmentManager.beginTransaction()
					.replace(R.id.main_container, FragmentLeagues.getInstance(null), FragmentLeagues.TAG)
					.commit();
		}
	}

}
