package me.worldfootball.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.worldfootball.R;

public class SplashscreenFragment extends Fragment {

    public static SplashscreenFragment getInstance(Bundle data) {
        SplashscreenFragment fragment = new SplashscreenFragment();
        fragment.setArguments(data != null ? data : new Bundle());
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splashscreen, null);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {

    }
}
