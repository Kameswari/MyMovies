package com.android.mymovies.viewmodels;

import com.android.mymovies.MyApplication;
import com.android.mymovies.R;
import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.network.NetworkService;
import com.android.mymovies.network.SearchMovieResults;
import com.android.mymovies.util.Constants;
import com.android.mymovies.util.db.DBHelper;
import com.android.mymovies.util.db.DBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Common view model used across application to handle the functionality of movie list
 */
public class MovieListViewModel {
    private UpdateListener updateListener;

    /**
     * Loads the favorite movies from DB
     */
    public void loadFavorites(){
        List<MovieListItem> favoriteMovies = prepareMovieList(DBUtil.getTableData(DBHelper.TABLE_FAVORITES));
        updateFavoritesInAppMemory(favoriteMovies);
        updateListener.onUpdate(favoriteMovies);
    }

    // search movies from api
    public void searchMovies(String query){
        NetworkService networkService = new NetworkService();
        networkService.getApi().fetchSearchResults(Constants.API_KEY, query, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchMovieResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SearchMovieResults searchMovieResults) {
                        mapFavoritesToSearchResults(searchMovieResults.movieListItems);
                        updateListener.onUpdate(searchMovieResults.movieListItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateListener.onError(R.string.problem_connecting_server);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    // method to update the isFavorite attribute in search results
    public void mapFavoritesToSearchResults(List<MovieListItem> movieListItems){
        Set<String> favoriteMovies = MyApplication.getInstance().getFavorites();
        if(movieListItems == null || movieListItems.size() == 0
                || favoriteMovies == null || favoriteMovies.isEmpty()){
            return;
        }

        for(MovieListItem item: movieListItems){
            item.isFavorite = favoriteMovies.contains(item.imdbID);
        }
    }

    /**
     * Method to update the user's favorite movies in app memory
     * This is to avoid frequent DB calls
     * @param movieListItems - list of favorite movies
     */
    private void updateFavoritesInAppMemory(List<MovieListItem> movieListItems){
        Set<String> favorites = new HashSet<>();
        for(MovieListItem item: movieListItems){
            favorites.add(item.imdbID);
        }
        MyApplication.getInstance().setFavorites(favorites);
    }

    public void setUpdateListener(UpdateListener listener){
        updateListener = listener;
    }

    /**
     * Prepares list of model objects from DB results
     *
     * @param favoriteMovies - DB results
     * @return - list of model objects
     */
    private List<MovieListItem> prepareMovieList(List<Map<String, String>> favoriteMovies){
        List<MovieListItem> movieList = new ArrayList<>();
        for(Map<String, String> map: favoriteMovies){
            MovieListItem item = new MovieListItem();
            item.isFavorite = true;
            item.poster = map.get(DBHelper.POSTER);
            item.imdbID = map.get(DBHelper.IMDB_ID);
            item.title = map.get(DBHelper.TITLE);
            item.year = map.get(DBHelper.YEAR);
            movieList.add(item);
        }

        return movieList;
    }

    /**
     * Converts item to hashmap for generic DB insertion in a table
     * @param item - movie item
     * @return - DB entry as hashmap with key as column name, value as column value
     */
    private Map<String, String> prepareMovieMap(MovieListItem item){
        Map<String, String> movie = new HashMap<>();
        movie.put(DBHelper.TITLE, item.title);
        movie.put(DBHelper.IMDB_ID, item.imdbID);
        movie.put(DBHelper.POSTER, item.poster);
        movie.put(DBHelper.YEAR, item.year);
        movie.put(DBHelper.TYPE, item.type);
        return movie;
    }

    /**
     * Adds favorite to DB
     *
     * @param item - item to be added
     * @param successListener - success listener
     */
    public void addFavorite(MovieListItem item, SuccessListener successListener){
        int result = DBUtil.insertTableRow(DBHelper.TABLE_FAVORITES, prepareMovieMap(item));
        if(result > 0){
            MyApplication.getInstance().getFavorites().add(item.imdbID);
            successListener.onSuccess();
        }
    }

    /**
     * Removes favorite entry from DB
     *
     * @param id - item id for which the favorite entry to be removed
     * @param successListener - success listener
     */
    public void removeFavorite(String id, SuccessListener successListener){
        int result = DBUtil.deleteTableRow(DBHelper.TABLE_FAVORITES, DBHelper.IMDB_ID + "=?", new String[]{id});
        if(result > 0){
            MyApplication.getInstance().getFavorites().remove(id);
            successListener.onSuccess();
        }
    }
}
