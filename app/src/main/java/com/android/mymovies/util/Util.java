package com.android.mymovies.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.mymovies.BaseActivity;
import com.android.mymovies.MyApplication;
import com.android.mymovies.R;
import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class
 */
public class Util {
    public static boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = ((ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Method to get image drawable for the given resource id
     * @param id - resource id
     * @return - image drawable
     */
    public static Drawable getDrawable(int id){
        BaseActivity context = MyApplication.getInstance().getCurrentActivity();
        return context.getResources().getDrawable(id);
    }

    public static void loadImageWithGlide(String url, ImageView imageView){
        if(!isValidURL(url)){
            return;
        }
        BaseActivity context = MyApplication.getInstance().getCurrentActivity();
        Glide.with(context).load(url).into(imageView);
    }

    // Function to validate URL
    // using regular expression
    public static boolean isValidURL(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }
}
