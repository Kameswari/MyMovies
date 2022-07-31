package com.android.mymovies.viewmodels;

/**
 * Listener to handle api updates in app
 */
public interface UpdateListener {
    public void onUpdate(Object movieListItemList);
    public void onError(int error);
}
