package com.worldfootball.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.worldfootball.R;
import com.worldfootball.adapters.abstractions.AbstractRecyclerAdapter;
import com.worldfootball.contracts.IOnItemTouchAdapter;
import com.worldfootball.models.League;
import java.util.ArrayList;

public class LeaguesAdapter extends AbstractRecyclerAdapter<League, LeaguesAdapter.LeagueViewHolder> implements IOnItemTouchAdapter {

	public LeaguesAdapter(ArrayList<League> list) {
		super(list);
	}

	public LeaguesAdapter(ArrayList<League> list, RecyclerView recyclerView) {
		super(list, recyclerView);
	}

	@Override
	public LeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
		return new LeagueViewHolder(view);
	}

	@Override
	public void onBindViewHolder(LeagueViewHolder holder, int position) {
		League item = mList.get(position);
		String name = item.getCaption();
			holder.mIcon.setImageResource(R.drawable.ic_alert_circle_outline);
			holder.mName.setText(item.getCaption());
	}

	public static class LeagueViewHolder extends RecyclerView.ViewHolder {

		private ImageView mIcon;
		private TextView mName;

		public LeagueViewHolder(View itemView) {
			super(itemView);
			mIcon = (ImageView) itemView.findViewById(R.id.icon);
			mName = (TextView) itemView.findViewById(R.id.name);
		}
	}

}
