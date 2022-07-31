package com.android.mymovies.network;

import com.google.gson.annotations.SerializedName;

public class MovieListItem {
    @SerializedName("Title")
    public String title;
    @SerializedName("Year")
    public String year;
    @SerializedName("imdbID")
    public String imdbID;
    @SerializedName("Type")
    public String type;
    @SerializedName("Poster")
    public String poster;
    public boolean isFavorite;
}
