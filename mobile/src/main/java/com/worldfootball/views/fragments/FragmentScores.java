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

import com.worldfootball.R;
import com.worldfootball.globalConstants.Constants;
import com.worldfootball.presenters.Presenter;
import com.worldfootball.listeners.RecyclerViewItemTouchListener;
import com.worldfootball.adapters.ScoresAdapter;
import com.worldfootball.contracts.IEventListener;
import com.worldfootball.contracts.IOnItemClickListener;
import com.worldfootball.models.League;
import com.worldfootball.models.Scores;
import com.worldfootball.utils.UserInterface;

import java.util.ArrayList;

public class FragmentScores extends Fragment
		implements Presenter.OnGetScores, IOnItemClickListener {

	public static final String TAG = "FragmentScores";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private ScoresAdapter mAdapter;
	private Presenter mPresenter;
	private IEventListener mEventListener;
	private League mLeague;
	private Snackbar mSnackbar;

	public static FragmentScores getInstance(@Nullable Bundle data) {
		FragmentScores fragment = new FragmentScores();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mLeague == null) mLeague = getArguments().getParcelable("league");
		if (savedInstanceState != null) {
			ArrayList<Scores> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null) mAdapter = new ScoresAdapter(getResources(), list);
		}
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scores, container, false);
		setupUI(view);
		if (mEventListener == null) mEventListener = (IEventListener) getActivity();
		if (mPresenter == null) mPresenter = Presenter.getInstance();
		showContent();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mRecyclerView != null) mRecyclerView = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mSnackbar != null) {
			mSnackbar.dismiss();
			mSnackbar = null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAdapter != null) {
			ArrayList<Scores> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mEventListener != null) mEventListener = null;
		if (mPresenter != null) mPresenter = null;
		if (mLeague != null) mLeague = null;
		if (mAdapter != null) mAdapter = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_scores, menu);
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
	public void onError(int code) {
		if (mAdapter == null || mAdapter.getList().isEmpty()) {
			if (code == Constants.ERROR_CODE_RESULT_EMPTY) {
				UserInterface.hide(mRecyclerView, mErrorContent, mProgressContent);
				UserInterface.show(mEmptyContent);
			} else {
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
	}

	@Override
	public void onSuccess(ArrayList<Scores> data) {
		UserInterface.hide(mErrorContent, mEmptyContent, mProgressContent);
		UserInterface.show(mRecyclerView);
		if (mSnackbar != null) mSnackbar.dismiss();
		if (mAdapter == null) {
			mAdapter = new ScoresAdapter(getResources(), data);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.changeData(data);
		}
	}

	@Override
	public void onItemClick(View view, int position) {

	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mLeague.getCaption());
			toolbar.setSubtitle(null);
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
		mRecyclerView.setHasFixedSize(true);
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		UserInterface.hide(mEmptyContent, mErrorContent, mProgressContent, mRecyclerView);
	}

	private void showContent() {
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
		if (mAdapter != null) mAdapter = null;
		mPresenter.getScores(getActivity(), mLeague.getID(), this);
	}
}
