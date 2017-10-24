package com.worldfootball.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldfootball.R;
import com.worldfootball.presenters.Presenter;
import com.worldfootball.listeners.RecyclerViewItemTouchListener;
import com.worldfootball.adapters.FixturesAdapter;
import com.worldfootball.contracts.IOnItemClickListener;
import com.worldfootball.models.Fixture;
import com.worldfootball.models.Head2head;
import com.worldfootball.utils.UserInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FragmentFixtureInformation extends Fragment implements IOnItemClickListener, Presenter.OnGetFixtureDetails {

    public static final String TAG = "FixtureFragment";

    private RecyclerView mRecyclerView;
    private FrameLayout mProgressContent;
    private LinearLayout mErrorContent;
    private LinearLayout mEmptyContent;
    private TextView mHome;
    private TextView mAway;
    private TextView mResult;
    private TextView mWinRates;
    private Snackbar mSnackbar;
    private FixturesAdapter mAdapter;
    private Presenter mPresenter;
    private Fixture mFixture;


    public static FragmentFixtureInformation getInstance(@Nullable Bundle data) {
        FragmentFixtureInformation fragment = new FragmentFixtureInformation();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mFixture == null) mFixture = getArguments().getParcelable("fixture");
        if (savedInstanceState != null) {
            ArrayList<Fixture> list = savedInstanceState.getParcelableArrayList("list");
            if (list != null) mAdapter = new FixturesAdapter(getResources(), list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fixture_details, container, false);
        setupUI(view);
        if (mPresenter == null) mPresenter = Presenter.getInstance();
        showContent();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecyclerView != null) mRecyclerView = null;
        if (mErrorContent != null) mErrorContent = null;
        if (mEmptyContent != null) mEmptyContent = null;
        if (mProgressContent != null) mProgressContent = null;
        if (mHome != null) mHome = null;
        if (mAway != null) mAway = null;
        if (mResult != null) mResult = null;
        if (mSnackbar != null) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            ArrayList<Fixture> list = mAdapter.getList();
            if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) mAdapter = null;
        if (mPresenter != null) mPresenter = null;
        if (mFixture != null) mFixture = null;
        if (mWinRates != null) mWinRates = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_fixture_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                loadData();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println(view);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onError(int code) {
        if (mAdapter == null || mAdapter.getList().isEmpty()) {
            UserInterface.hide(mRecyclerView, mEmptyContent, mProgressContent);
            UserInterface.show(mErrorContent);
            mSnackbar = Snackbar.make(
                    getActivity().findViewById(R.id.main_container),
                    R.string.snackbar_result_null_text,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(
                            R.string.snackbar_result_null_action,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadData();
                                }
                            }
                    );
            mSnackbar.show();
        }
    }

    @Override
    public void onSuccess(Head2head data) {
        if (mSnackbar != null) mSnackbar.dismiss();
        if (data.getFixtures() == null || data.getFixtures().isEmpty()) {
            UserInterface.hide(mErrorContent, mProgressContent, mRecyclerView);
            UserInterface.show(mEmptyContent);
        } else {
            UserInterface.hide(mErrorContent, mEmptyContent, mProgressContent);
            UserInterface.show(mRecyclerView);
            calculateWinRatesByGivenTeams(data);
            if (mAdapter == null) {
                mAdapter = new FixturesAdapter(getResources(), data.getFixtures());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.changeData(data.getFixtures());
            }
        }
    }

    private void calculateWinRatesByGivenTeams(Head2head data) {
        if (mFixture.getGoalsHomeTeam() < 0 && mFixture.getGoalsAwayTeam() < 0) {
            int goalsHome = 0;
            int goalsAway = 0;
            int drawGoals = 0;

            int homeWin = 0;
            int awayWin = 0;
            int drawMatch =0;

            ArrayList<Fixture> list = data.getFixtures();
            int i = 0;
            if (mFixture.getGoalsHomeTeam() >= 0 && mFixture.getGoalsAwayTeam() >= 0)
            {
                i = 1;
            }
            for (; i < list.size(); i++) {
                goalsHome = list.get(i).getGoalsHomeTeam();
                goalsAway = list.get(i).getGoalsAwayTeam();
                if(goalsHome > goalsAway){
                    homeWin++;
                }else if(goalsAway > goalsHome){
                    awayWin++;
                }else {
                    drawMatch++;
                }
            }


            int homeWinRate = homeWin*(100/list.size());
            int awayWinRate = awayWin*(100/list.size());
            int drawRate = 100 - homeWinRate - awayWinRate;
            mWinRates.setText(String.format(
                    getString(R.string.fixture_info_winRates),
                    homeWinRate,drawRate,
                    awayWinRate)
            );
            UserInterface.show(mWinRates);
        }
    }

    private void setupUI(View view) {
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.fixture_details_title));
            if (mFixture.getStatus() == Fixture.TIMED) {
                toolbar.setSubtitle(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                .format(new Date(mFixture.getDate())));
            }
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(
                getActivity(),
                mRecyclerView,
                this));
        mWinRates = (TextView) view.findViewById(R.id.winRates);
        mRecyclerView.setHasFixedSize(true);
        mHome = (TextView) view.findViewById(R.id.home);
        mAway = (TextView) view.findViewById(R.id.away);
        mResult = (TextView) view.findViewById(R.id.result);
        mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
        mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
        mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
        UserInterface.hide(mRecyclerView, mErrorContent, mEmptyContent, mProgressContent, mWinRates);
    }

    private void showContent() {
        mHome.setText(mFixture.getHomeTeamName());
        mAway.setText(mFixture.getAwayTeamName());
        if (mFixture.getStatus() != Fixture.TIMED) {
            mResult.setText(String.format(
                    getString(R.string.fixture_details_result),
                    mFixture.getGoalsHomeTeam(),
                    mFixture.getGoalsAwayTeam()));
        } else {
            mResult.setText(getString(R.string.fixture_details_empty_result));
        }
        if (mAdapter == null) loadData();
        else {
            UserInterface.hide(mEmptyContent, mErrorContent, mProgressContent);
            UserInterface.show(mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void loadData() {
        UserInterface.hide(mRecyclerView, mEmptyContent, mErrorContent);
        UserInterface.show(mProgressContent);
        mPresenter.getFixtureDetails(getActivity(), mFixture.getID(), this);
    }

}
