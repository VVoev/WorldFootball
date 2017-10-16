package me.worldfootball.contracts;

public interface IOnResult<T> {
    void onSuccess(T data);
}
