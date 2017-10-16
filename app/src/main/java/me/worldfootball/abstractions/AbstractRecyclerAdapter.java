package me.worldfootball.abstractions;


import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import me.worldfootball.utils.L;

public abstract class AbstractRecyclerAdapter<OL,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected ArrayList<OL> mList;

    public AbstractRecyclerAdapter(ArrayList<OL> list) {
        mList = list;
    }

    @Override
    abstract public VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    abstract public void onBindViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        else {
            L.e(this, "mList == null");
            return 0;
        }
    }

    public void addItem(int index, OL item) {
        if (mList != null) {
            mList.add(index, item);
            notifyItemInserted(index);
        }
    }

    public void removeItem(int index) {
        if (mList != null) {
            mList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void changeData(ArrayList<OL> list) {
        if (list != null) {
            mList = list;
            notifyDataSetChanged();
        }
    }

}
