package com.android.mymovies;

import android.app.Application;

import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    // to store the current activity instance
    private BaseActivity currentActivity;

    // keep track of user's favorites across app to avoid multiple db calls
    private Set<String> favorites;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        favorites = new HashSet<>();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity activity) {
        currentActivity = activity;
    }

    public Set<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<String> favorites) {
        this.favorites = favorites;
    }

}
