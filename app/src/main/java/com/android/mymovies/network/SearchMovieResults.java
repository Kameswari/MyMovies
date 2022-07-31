package com.android.mymovies.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchMovieResults {
    @SerializedName("Search")
    public ArrayList<MovieListItem> movieListItems;
    @SerializedName("totalResults")
    public String totalResults;
    @SerializedName("Response")
    public String response;
}