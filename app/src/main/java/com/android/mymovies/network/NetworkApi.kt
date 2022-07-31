package com.android.mymovies.network

import com.android.mymovies.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("/")
    fun fetchSearchResults(@Query("apikey") apiKey: String = Constants.API_KEY, @Query("s") query: String,
                           @Query("page") id: String): Observable<SearchMovieResults>


    @GET("/")
    fun fetchDetails(@Query("apikey") apiKey: String = Constants.API_KEY, @Query("i") imdbID: String): Observable<MovieDetails>
}