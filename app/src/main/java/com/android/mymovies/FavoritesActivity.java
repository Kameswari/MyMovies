package com.android.mymovies;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.util.Util;
import com.android.mymovies.viewmodels.MovieListViewModel;
import com.android.mymovies.viewmodels.UpdateListener;

import java.util.List;

/**
 * Activity to show the user's favorite movies
 * Can be used though device is not connected with internet
 */
public class FavoritesActivity extends BaseActivity {
    private MovieListViewModel favoritesViewModel;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private TextView noFavoritesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // initialize view model
        favoritesViewModel = new MovieListViewModel();
        initUI();

        favoritesViewModel.setUpdateListener(new UpdateListener() {
            @Override
            public void onUpdate(Object movieListItemList) {
                updateMovieList((List<MovieListItem>)movieListItemList);
            }

            @Override
            public void onError(int error) {
                // No need to handle as hint text already shown
            }
        });
    }

    // initialize UI elements
    public void initUI(){
        recyclerView = findViewById(R.id.recycler_view);
        noFavoritesView = findViewById(R.id.no_results_view);

        setSupportActionBar(findViewById(R.id.toolbar));

        itemAdapter = new ItemAdapter(favoritesViewModel);
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_favorites_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);

        searchItem.setOnMenuItemClickListener(menuItem -> {
            // navigate to search screen
            if(Util.isNetworkConnected()){
                navigateToScreen(SearchActivity.class);
            }else{
                showToast(R.string.please_connect_internet);
            }
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesViewModel.loadFavorites();
    }

    // update recycler view
    private void updateMovieList(List<MovieListItem> list){
        if(list == null || list.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            noFavoritesView.setVisibility(View.VISIBLE);
        }else{
            noFavoritesView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            itemAdapter.updateList(list);
            itemAdapter.notifyDataSetChanged();
        }
    }
}