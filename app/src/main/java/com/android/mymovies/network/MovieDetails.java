package com.android.mymovies.network;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {
    @SerializedName("Plot")
    public String plot;
    @SerializedName("Rated")
    public String rating;
}
