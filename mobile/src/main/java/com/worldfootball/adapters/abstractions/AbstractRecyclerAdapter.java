package com.worldfootball.adapters.abstractions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;

import com.worldfootball.listeners.RecyclerViewTouchHelperCallback;
import com.worldfootball.contracts.IOnItemTouchAdapter;
import com.worldfootball.utils.L;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @param <OL> object of list
 * @param <VH> ViewHolder
 *             This implementation of generic recycler adapter is truly inspired from Firebase Database UI Adapter.
 */
public abstract class AbstractRecyclerAdapter<OL, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements IOnItemTouchAdapter {

    protected ArrayList<OL> mList;
    private RecyclerViewTouchHelperCallback callback;

    public AbstractRecyclerAdapter(ArrayList<OL> list) {
        mList = list;
    }

    public AbstractRecyclerAdapter(ArrayList<OL> list, RecyclerView recyclerView) {
        mList = list;
        if (recyclerView != null) {
            callback = new RecyclerViewTouchHelperCallback(this);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    abstract public VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    abstract public void onBindViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        else {
            L.e(AbstractRecyclerAdapter.class, "mList == null");
            return 0;
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @NonNull
    public ArrayList<OL> getList() {
        if (mList != null) return mList;
        else return new ArrayList<>();
    }

    public OL getItem(int position) {
        if (mList != null) {
            if (position >= 0 && position < mList.size()) {
                return mList.get(position);
            }
            L.e(AbstractRecyclerAdapter.class, "no item[" + position + "]");
        }
        L.e(AbstractRecyclerAdapter.class, "error to get item");
        return null;
    }

    public void setDragEnabled(boolean dragEnabled) {
        if (callback != null) callback.setDragEnabled(dragEnabled);
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        if (callback != null) callback.setSwipeEnabled(swipeEnabled);
    }

    public void addItem(int index, OL item) {
        if (mList != null) {
            mList.add(index, item);
            notifyItemInserted(index);
        }
    }

    public void replaceItem(int index, OL item) {
        if (mList != null) {
            mList.set(index, item);
            notifyItemChanged(index);
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
