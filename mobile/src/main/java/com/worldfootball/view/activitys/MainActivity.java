package com.worldfootball.view.activitys;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.worldfootball.R;
import com.worldfootball.contracts.IOnGetLeagues;
import com.worldfootball.presenters.Presenter;
import com.worldfootball.preferances.PreferencesManager;
import com.worldfootball.adapters.LeaguesAdapter;
import com.worldfootball.model.League;
import com.worldfootball.globalConstants.Const;
import com.worldfootball.util.L;
import com.worldfootball.view.fragments.SplashscreenFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();
        PreferencesManager.getInstance(this).setBoolean(Const.PREF_FIRST_ENTER, true).commit();
        setupUI();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Presenter.getListOfLeagues(this, new Presenter() {

            @Override
            public void onError() {
                L.e("Error");
            }

            @Override
            public void onSuccess(ArrayList<League> data) {
                NavigationView navigation = (NavigationView) findViewById(R.id.navigation_view);
                if (navigation != null) {
                    RecyclerView recyclerView = (RecyclerView) navigation.findViewById(R.id.navigation_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(new LeaguesAdapter(data));
                } else {
                    L.e(this, "RuntimeException");
                    throw new RuntimeException("NavigationView have not find");
                }
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        if (Presenter.isFirstEnter()) {
            PreferencesManager.getInstance(this)
                    .setBoolean(Const.PREF_FIRST_ENTER, false)
                    .commit();
            mFragmentManager.beginTransaction()
                    .replace(R.id.main_container, SplashscreenFragment.getInstance(null))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragmentManager != null) mFragmentManager = null;
        if (mToolbar != null) mToolbar = null;
        if (mDrawer != null) mDrawer = null;
    }

    private void setupUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

}
