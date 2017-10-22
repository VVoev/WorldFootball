package com.worldfootball.contracts;

public interface IOnResultListener<S> extends IOnResult<S> {
	void onError(int code);
}
