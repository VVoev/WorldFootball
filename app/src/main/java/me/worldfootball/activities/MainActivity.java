package me.worldfootball.activities;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.worldfootball.R;
import me.worldfootball.fragments.SplashscreenFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        if (mFragmentManager == null) mFragmentManager = getFragmentManager();

        mFragmentManager.beginTransaction()
                .replace(R.id.main_container, SplashscreenFragment.getInstance(null))
                .commit();
    }

    private void setupUI() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragmentManager != null) mFragmentManager = null;
    }
}
