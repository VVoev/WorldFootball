package com.worldfootball.contracts;

public interface IOnItemTouchAdapter {
	boolean onItemMove(int fromPosition, int toPosition);
	void onItemDismiss(int position);
}
