package com.android.mymovies;

import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.viewmodels.MovieListViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * unit test cases for different methods in MovieListViewModel
 */
public class MovieListViewModelTest {
    MovieListViewModel movieListViewModel;
    private List<MovieListItem> list;
    private Set<String> favorites;
    private MyApplication myApplication;

    @Before
    public void setUp(){
        movieListViewModel = new MovieListViewModel();

        favorites = new HashSet<>();
        favorites.add("1");
        myApplication = Mockito.mock(MyApplication.class);
        myApplication.setFavorites(favorites);
        
        // prepare dummy movie list
        list = new ArrayList<>();

        for(int i=1; i<4; ++i){
            MovieListItem item = new MovieListItem();
            item.imdbID = i+"";
            list.add(item);
        }
    }

    @Test
    public void verifyMapFavoritesToSearchResults(){
        movieListViewModel.mapFavoritesToSearchResults(list);
        for(MovieListItem item: list){
            if(favorites.contains(item.imdbID)){
                Assert.assertTrue(item.isFavorite);
            }
        }
        Assert.assertFalse(list.isEmpty());
    }
}
