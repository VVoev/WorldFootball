package me.worldfootball.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.worldfootball.R;
import me.worldfootball.abstractions.AbstractRecyclerAdapter;
import me.worldfootball.models.League;


public class LeaguesAdapter extends AbstractRecyclerAdapter<League, LeaguesAdapter.LeagueViewHolder> {

    public LeaguesAdapter(ArrayList<League> list) {
        super(list);
    }

    @Override
    public LeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeagueViewHolder holder, int position) {
        League item = mList.get(position);
        //temp image
        holder.getIcon().setImageResource(R.drawable.alert);
        holder.getName().setText(item.getCaption());
    }

    public static class LeagueViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mName;

        public LeagueViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mName = (TextView) itemView.findViewById(R.id.name);
        }

        public ImageView getIcon() {
            return mIcon;
        }

        public TextView getName() {
            return mName;
        }
    }

}
